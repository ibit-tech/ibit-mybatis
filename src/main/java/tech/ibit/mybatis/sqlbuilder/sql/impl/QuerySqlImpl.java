package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.CountSql;
import tech.ibit.mybatis.sqlbuilder.sql.Page;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.sqlbuilder.sql.support.UseAliasSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.impl.*;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuerySql实现
 *
 * @author iBit程序猿
 * @version 2.0
 */
public class QuerySqlImpl<T> extends SqlLogImpl
        implements QuerySql<T>,
        UseAliasSupport, PrepareStatementBuildSupport {

    /**
     * 列支持
     */
    private final ColumnSupportImpl<QuerySql<T>> columnSupport;

    /**
     * From 支持
     */
    private final FromSupportImpl<QuerySql<T>> fromSupport;

    /**
     * distinct 支持
     */
    private final DistinctSupportImpl<QuerySql<T>> distinctSupport;

    /**
     * join on 支持
     */
    private final JoinOnSupportImpl<QuerySql<T>> joinOnSupport;

    /**
     * where 支持
     */
    private final WhereSupportImpl<QuerySql<T>> whereSupport;

    /**
     * group by 支持
     */
    private final GroupBySupportImpl<QuerySql<T>> groupBySupport;

    /**
     * having 支持
     */
    private final HavingSupportImpl<QuerySql<T>> havingSupport;

    /**
     * order by 支持
     */
    private final OrderBySupportImpl<QuerySql<T>> orderBySupport;

    /**
     * limit 支持
     */
    private final LimitSupportImpl<QuerySql<T>> limitSupport;


    /**
     * 基础mapper
     */
    private final Mapper<T> mapper;

    public QuerySqlImpl(Mapper<T> mapper) {
        this.mapper = mapper;
        this.columnSupport = new ColumnSupportImpl<>(this);
        this.fromSupport = new FromSupportImpl<>(this);
        this.joinOnSupport = new JoinOnSupportImpl<>(this);
        this.whereSupport = new WhereSupportImpl<>(this);
        this.groupBySupport = new GroupBySupportImpl<>(this);
        this.distinctSupport = new DistinctSupportImpl<>(this);
        this.havingSupport = new HavingSupportImpl<>(this);
        this.orderBySupport = new OrderBySupportImpl<>(this);
        this.limitSupport = new LimitSupportImpl<>(this);
    }

    @Override
    public QuerySql<T> column(List<? extends IColumn> columns) {
        return columnSupport.column(columns);
    }

    @Override
    public QuerySql<T> column(IColumn column) {
        return columnSupport.column(column);
    }

    @Override
    public QuerySql<T> columnPo(Class<?> poClass) {
        return columnSupport.columnPo(poClass);
    }

    @Override
    public QuerySql<T> from(Table table) {
        return fromSupport.from(table);
    }

    @Override
    public QuerySql<T> from(List<Table> tables) {
        return fromSupport.from(tables);
    }

    @Override
    public QuerySql<T> distinct() {
        return distinctSupport.distinct();
    }

    @Override
    public QuerySql<T> distinct(boolean distinct) {
        return distinctSupport.distinct(distinct);
    }

    @Override
    public QuerySql<T> having(Criteria having) {
        return havingSupport.having(having);
    }

    @Override
    public QuerySql<T> having(List<Criteria> havings) {
        return havingSupport.having(havings);
    }

    @Override
    public QuerySql<T> andHaving(CriteriaItem havingItem) {
        return havingSupport.andHaving(havingItem);
    }

    @Override
    public QuerySql<T> andHaving(List<Criteria> havings) {
        return havingSupport.andHaving(havings);
    }

    @Override
    public QuerySql<T> orHaving(CriteriaItem havingItem) {
        return havingSupport.orHaving(havingItem);
    }

    @Override
    public QuerySql<T> orHaving(List<Criteria> havings) {
        return havingSupport.orHaving(havings);
    }

    @Override
    public QuerySql<T> joinOn(JoinOn joinOn) {
        return joinOnSupport.joinOn(joinOn);
    }

    @Override
    public QuerySql<T> joinOn(List<JoinOn> joinOns) {
        return joinOnSupport.joinOn(joinOns);
    }

    @Override
    public QuerySql<T> joinOn(Table table, List<Column> columnPairs) {
        return joinOnSupport.joinOn(table, columnPairs);
    }

    @Override
    public QuerySql<T> leftJoinOn(Table table, List<Column> columnPairs) {
        return joinOnSupport.leftJoinOn(table, columnPairs);
    }

    @Override
    public QuerySql<T> rightJoinOn(Table table, List<Column> columnPairs) {
        return joinOnSupport.rightJoinOn(table, columnPairs);
    }

    @Override
    public QuerySql<T> fullJoinOn(Table table, List<Column> columnPairs) {
        return joinOnSupport.fullJoinOn(table, columnPairs);
    }

    @Override
    public QuerySql<T> innerJoinOn(Table table, List<Column> columnPairs) {
        return joinOnSupport.innerJoinOn(table, columnPairs);
    }

    @Override
    public QuerySql<T> complexLeftJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        return joinOnSupport.complexLeftJoinOn(table, criteriaItems);
    }

    @Override
    public QuerySql<T> complexRightJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        return joinOnSupport.complexRightJoinOn(table, criteriaItems);
    }

    @Override
    public QuerySql<T> complexFullJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        return joinOnSupport.complexFullJoinOn(table, criteriaItems);
    }

    @Override
    public QuerySql<T> complexInnerJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        return joinOnSupport.complexInnerJoinOn(table, criteriaItems);
    }

    @Override
    public QuerySql<T> where(Criteria criteria) {
        return whereSupport.where(criteria);
    }

    @Override
    public QuerySql<T> where(List<Criteria> criterion) {
        return whereSupport.where(criterion);
    }

    @Override
    public QuerySql<T> andWhere(CriteriaItem item) {
        return whereSupport.andWhere(item);
    }

    @Override
    public QuerySql<T> andWhere(List<Criteria> criterion) {
        return whereSupport.andWhere(criterion);
    }

    @Override
    public QuerySql<T> orWhere(CriteriaItem item) {
        return whereSupport.orWhere(item);
    }

    @Override
    public QuerySql<T> orWhere(List<Criteria> criterion) {
        return whereSupport.orWhere(criterion);
    }

    @Override
    public QuerySql<T> groupBy(Column groupBy) {
        return groupBySupport.groupBy(groupBy);
    }

    @Override
    public QuerySql<T> groupBy(List<Column> groupBys) {
        return groupBySupport.groupBy(groupBys);
    }

    @Override
    public QuerySql<T> orderBy(IOrderBy orderBy) {
        return orderBySupport.orderBy(orderBy);
    }

    @Override
    public QuerySql<T> orderBy(List<IOrderBy> orderBys) {
        return orderBySupport.orderBy(orderBys);
    }

    @Override
    public QuerySql<T> orderBy(IColumn column) {
        return orderBySupport.orderBy(column);
    }

    @Override
    public QuerySql<T> orderBy(IColumn column, boolean desc) {
        return orderBySupport.orderBy(column, desc);
    }


    @Override
    public QuerySql<T> limit(int start, int limit) {
        return limitSupport.limit(start, limit);
    }

    @Override
    public QuerySql<T> limit(int limit) {
        return limitSupport.limit(limit);
    }


    @Override
    public boolean isUseAlias() {
        return true;
    }

    @Override
    public PrepareStatement getPrepareStatement() {


        // 列为空的时候，补充默认列
        if (CollectionUtils.isEmpty(columnSupport.getColumn().getItems())) {
            columnPo(mapper.getPoClazz());
        }

        // 表为空的时候，补充默认表
        if (CollectionUtils.isEmpty(fromSupport.getTable().getItems())) {
            from(mapper.getDefaultTable());
        }

        boolean distinct = distinctSupport.getDistinct().isValue();

        StringBuilder prepareSql = new StringBuilder();
        prepareSql.append(distinct ? "SELECT DISTINCT " : "SELECT ");

        List<ColumnValue> values = new ArrayList<>();

        boolean useAlias = isUseAlias();

        append(
                Arrays.asList(
                        columnSupport.getColumnPrepareStatement(useAlias),
                        fromSupport.getFromPrepareStatement(useAlias),
                        joinOnSupport.getJoinOnPrepareStatement(useAlias),
                        whereSupport.getWherePrepareStatement(useAlias),
                        groupBySupport.getGroupByPrepareStatement(useAlias),
                        havingSupport.getHavingPrepareStatement(useAlias),
                        orderBySupport.getOrderByPrepareStatement(useAlias),
                        limitSupport.getLimitPrepareStatement()

                ), prepareSql, values
        );
        return new PrepareStatement(prepareSql.toString(), values);
    }

    @Override
    public CountSql toCountSql() {
        CountSqlImpl countSql = new CountSqlImpl(mapper, false);
        countSql.setDistinctSupport(distinctSupport.copy(countSql));
        countSql.setFromSupport(fromSupport.copy(countSql));
        countSql.setJoinOnSupport(joinOnSupport.copy(countSql));
        countSql.setWhereSupport(whereSupport.copy(countSql));
        countSql.setGroupBySupport(groupBySupport.copy(countSql));
        countSql.setHavingSupport(havingSupport.copy(countSql));
        countSql.setColumnSupport(columnSupport.copy(countSql));
        return countSql;
    }

    @Override
    public Page<T> executeQueryPage() {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, Collections.emptyList());
        }
        List<T> results = executeQuery();
        return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, results);
    }

    @Override
    public <P> Page<P> executeQueryPage(Class<P> clazz) {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, Collections.emptyList());
        }
        List<P> results = executeQuery(clazz);
        return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, results);
    }

    @Override
    public List<T> executeQuery() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelect(statement);
    }

    @Override
    public <P> List<P> executeQuery(Class<P> clazz) {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectWithType(statement, clazz);
    }

    @Override
    public T executeQueryOne() {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectOne(statement);
    }

    @Override
    public <P> P executeQueryOne(Class<P> clazz) {
        PrepareStatement statement = logAndGetPrepareStatement();
        return mapper.rawSelectOneWithType(statement, clazz);
    }

    @Override
    public <V> Page<V> executeQueryDefaultPage() {
        int total = toCountSql().executeCount();
        if (total <= 0) {
            return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, Collections.emptyList());
        }
        List<V> results = executeQueryDefault();
        return new Page<>(limitSupport.getLimit().getStart(), limitSupport.getLimit().getLimit(), total, results);
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
}
