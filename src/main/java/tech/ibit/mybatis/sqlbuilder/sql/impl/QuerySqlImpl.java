package tech.ibit.mybatis.sqlbuilder.sql.impl;

import lombok.Getter;
import lombok.Setter;
import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.CountSql;
import tech.ibit.mybatis.sqlbuilder.sql.Page;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.sqlbuilder.sql.field.BooleanField;
import tech.ibit.mybatis.sqlbuilder.sql.field.LimitField;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.*;

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
@Getter
@Setter
public class QuerySqlImpl<T> extends SqlLogImpl
        implements QuerySql<T>,
        DefaultColumnSupport<QuerySql<T>>,
        DefaultFromSupport<QuerySql<T>>,
        DefaultJoinOnSupport<QuerySql<T>>,
        DefaultWhereSupport<QuerySql<T>>,
        DefaultGroupBySupport<QuerySql<T>>,
        DefaultHavingSupport<QuerySql<T>>,
        DefaultOrderBySupport<QuerySql<T>>,
        DefaultLimitSupport<QuerySql<T>> {


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
    public List<T> executeQuery() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelect(statement);
    }

    @Override
    public T executeQueryOne() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectOne(statement);
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

    private PrepareStatement logAndGetPrepareStatement() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return statement;
    }
}
