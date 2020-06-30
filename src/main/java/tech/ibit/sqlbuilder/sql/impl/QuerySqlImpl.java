package tech.ibit.sqlbuilder.sql.impl;

import lombok.Getter;
import lombok.Setter;
import tech.ibit.mybatis.template.mapper.RawMapper;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.sql.CountSql;
import tech.ibit.sqlbuilder.sql.Page;
import tech.ibit.sqlbuilder.sql.QuerySql;
import tech.ibit.sqlbuilder.sql.field.BooleanField;
import tech.ibit.sqlbuilder.sql.field.LimitField;
import tech.ibit.sqlbuilder.sql.field.ListField;

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
public class QuerySqlImpl<T> extends SqlLogImpl implements QuerySql<T> {


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
    public CountSql<T> toCountSql() {
        CountSqlImpl<T> countSql = new CountSqlImpl<>(mapper);
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
    public Page<T> doQueryPage() {
        int total = toCountSql().doCount();
        if (total <= 0) {
            return new Page<>(limit.getStart(), limit.getLimit(), total, Collections.emptyList());
        }
        List<T> results = doQuery();
        return new Page<>(limit.getStart(), limit.getLimit(), total, results);
    }

    @Override
    public List<T> doQuery() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelect(statement);
    }

    @Override
    public T doQueryOne() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectOne(statement);
    }

    @Override
    public <V> Page<V> doQueryDefaultPage() {
        int total = toCountSql().doCount();
        if (total <= 0) {
            return new Page<>(limit.getStart(), limit.getLimit(), total, Collections.emptyList());
        }
        List<V> results = doQueryDefault();
        return new Page<>(limit.getStart(), limit.getLimit(), total, results);
    }

    @Override
    public <V> List<V> doQueryDefault() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectDefault(statement);
    }

    private PrepareStatement logAndGetPrepareStatement() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return statement;
    }
}
