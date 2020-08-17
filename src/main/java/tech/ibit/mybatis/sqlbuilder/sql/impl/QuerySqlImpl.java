package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.converter.EntityConverter;
import tech.ibit.mybatis.sqlbuilder.sql.CountSql;
import tech.ibit.mybatis.sqlbuilder.sql.Page;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.sqlbuilder.sql.field.BooleanField;
import tech.ibit.mybatis.sqlbuilder.sql.field.LimitField;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuerySqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class QuerySqlImpl<T> extends SqlLogImpl
        implements QuerySql<T>,
        DefaultDistinctSupport<QuerySql<T>>,
        DefaultColumnSupport<QuerySql<T>>,
        DefaultFromSupport<QuerySql<T>>,
        DefaultJoinOnSupport<QuerySql<T>>,
        DefaultWhereSupport<QuerySql<T>>,
        DefaultGroupBySupport<QuerySql<T>>,
        DefaultHavingSupport<QuerySql<T>>,
        DefaultOrderBySupport<QuerySql<T>>,
        DefaultLimitSupport<QuerySql<T>>,
        DefaultUseAliasSupport {


    /**
     * 是否distinct
     */
    private BooleanField distinct = new BooleanField(false);

    /**
     * form
     */
    private ListField<Table> from = new ListField<>();

    /**
     * Join on
     */
    private ListField<JoinOn> joinOn = new ListField<>();

    /**
     * where
     */
    private ListField<Criteria> where = new ListField<>();


    /**
     * group by
     */
    private ListField<Column> groupBy = new ListField<>();

    /**
     * Having
     */
    private ListField<Criteria> having = new ListField<>();


    /**
     * Order by
     */
    private ListField<IOrderBy> orderBy = new ListField<>();

    /**
     * limit
     */
    private LimitField limit = new LimitField();

    /**
     * 列
     */
    private ListField<IColumn> column = new ListField<>();

    /**
     * 基础mapper
     */
    private RawMapper<T> mapper;

    public QuerySqlImpl(RawMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public QuerySql<T> getSql() {
        return this;
    }

    @Override
    public boolean isUseAlias() {
        return true;
    }

    @Override
    public PrepareStatement getPrepareStatement() {
        boolean distinct = getDistinct().isValue();

        StringBuilder prepareSql = new StringBuilder();
        prepareSql.append(distinct ? "SELECT DISTINCT " : "SELECT ");

        List<ColumnValue> values = new ArrayList<>();

        boolean useAlias = isUseAlias();

        append(
                Arrays.asList(
                        getColumnPrepareStatement(useAlias),
                        getFromPrepareStatement(useAlias),
                        getJoinOnPrepareStatement(useAlias),
                        getWherePrepareStatement(useAlias),
                        getGroupByPrepareStatement(useAlias),
                        getHavingPrepareStatement(useAlias),
                        getOrderByPrepareStatement(useAlias),
                        getLimitPrepareStatement()

                ), prepareSql, values
        );
        return new PrepareStatement(prepareSql.toString(), values);
    }

    @Override
    public CountSql toCountSql() {
        CountSqlImpl countSql = new CountSqlImpl(mapper);
        countSql.setDistinct(distinct);
        countSql.setFrom(from);
        countSql.setJoinOn(joinOn);
        countSql.setWhere(where);
        countSql.setGroupBy(groupBy);
        countSql.setHaving(having);
        countSql.setColumn(column);
        return countSql;
    }

    @Override
    public Page<T> executeQueryPage() {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limit.getStart(), limit.getLimit(), total, Collections.emptyList());
        }
        List<T> results = executeQuery();
        return new Page<>(limit.getStart(), limit.getLimit(), total, results);
    }

    @Override
    public <P> Page<P> executeQueryPage(Class<P> clazz) {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limit.getStart(), limit.getLimit(), total, Collections.emptyList());
        }
        List<P> results = executeQuery(clazz);
        return new Page<>(limit.getStart(), limit.getLimit(), total, results);
    }

    @Override
    public List<T> executeQuery() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelect(statement);
    }

    @Override
    public <P> List<P> executeQuery(Class<P> clazz) {
        return EntityConverter.copyColumns(executeQuery(), clazz);
    }

    @Override
    public T executeQueryOne() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectOne(statement);
    }

    @Override
    public <P> P executeQueryOne(Class<P> clazz) {
        return EntityConverter.copyColumns(executeQueryOne(), clazz);
    }

    @Override
    public <V> Page<V> executeQueryDefaultPage() {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limit.getStart(), limit.getLimit(), total, Collections.emptyList());
        }
        List<V> results = executeQueryDefault();
        return new Page<>(limit.getStart(), limit.getLimit(), total, results);
    }

    @Override
    public <V> List<V> executeQueryDefault() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectDefault(statement);
    }

    @Override
    public QuerySql<T> columnDefaultPo() {
        return columnPo(mapper.getPoClazz());
    }

    @Override
    public QuerySql<T> fromDefault() {
        return from(mapper.getDefaultTable());
    }

    private PrepareStatement logAndGetPrepareStatement() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return statement;
    }

    @Override
    public BooleanField getDistinct() {
        return distinct;
    }

    /**
     * Sets the distinct
     * <p>You can use getDistinct() to get the value of distinct</p>
     *
     * @param distinct distinct
     */
    public void setDistinct(BooleanField distinct) {
        this.distinct = distinct;
    }

    @Override
    public ListField<Table> getFrom() {
        return from;
    }

    /**
     * Sets the from
     * <p>You can use getFrom() to get the value of from</p>
     *
     * @param from from
     */
    public void setFrom(ListField<Table> from) {
        this.from = from;
    }

    @Override
    public ListField<JoinOn> getJoinOn() {
        return joinOn;
    }

    /**
     * Sets the joinOn
     * <p>You can use getJoinOn() to get the value of joinOn</p>
     *
     * @param joinOn joinOn
     */
    public void setJoinOn(ListField<JoinOn> joinOn) {
        this.joinOn = joinOn;
    }

    @Override
    public ListField<Criteria> getWhere() {
        return where;
    }

    /**
     * Sets the where
     * <p>You can use getWhere() to get the value of where</p>
     *
     * @param where where
     */
    public void setWhere(ListField<Criteria> where) {
        this.where = where;
    }

    @Override
    public ListField<Column> getGroupBy() {
        return groupBy;
    }

    /**
     * Sets the groupBy
     * <p>You can use getGroupBy() to get the value of groupBy</p>
     *
     * @param groupBy groupBy
     */
    public void setGroupBy(ListField<Column> groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public ListField<Criteria> getHaving() {
        return having;
    }

    /**
     * Sets the having
     * <p>You can use getHaving() to get the value of having</p>
     *
     * @param having having
     */
    public void setHaving(ListField<Criteria> having) {
        this.having = having;
    }

    @Override
    public ListField<IOrderBy> getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the orderBy
     * <p>You can use getOrderBy() to get the value of orderBy</p>
     *
     * @param orderBy orderBy
     */
    public void setOrderBy(ListField<IOrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public LimitField getLimit() {
        return limit;
    }

    /**
     * Sets the limit
     * <p>You can use getLimit() to get the value of limit</p>
     *
     * @param limit limit
     */
    public void setLimit(LimitField limit) {
        this.limit = limit;
    }

    @Override
    public ListField<IColumn> getColumn() {
        return column;
    }

    /**
     * Sets the column
     * <p>You can use getColumn() to get the value of column</p>
     *
     * @param column column
     */
    public void setColumn(ListField<IColumn> column) {
        this.column = column;
    }

    /**
     * Gets the value of mapper
     *
     * @return the value of mapper
     */
    public RawMapper<T> getMapper() {
        return mapper;
    }

    /**
     * Sets the mapper
     * <p>You can use getMapper() to get the value of mapper</p>
     *
     * @param mapper mapper
     */
    public void setMapper(RawMapper<T> mapper) {
        this.mapper = mapper;
    }
}
