package tech.ibit.mybatis.sqlbuilder.sql.impl;

import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.CountSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.BooleanField;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CountSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class CountSqlImpl extends SqlLogImpl
        implements CountSql,
        DefaultDistinctSupport<CountSql>,
        DefaultColumnSupport<CountSql>,
        DefaultFromSupport<CountSql>,
        DefaultJoinOnSupport<CountSql>,
        DefaultWhereSupport<CountSql>,
        DefaultGroupBySupport<CountSql>,
        DefaultHavingSupport<CountSql>,
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
     * 列
     */
    private ListField<IColumn> column = new ListField<>();


    /**
     * 基础mapper
     */
    private final Mapper<?> mapper;


    public CountSqlImpl(Mapper<?> mapper) {
        this.mapper = mapper;
    }


    @Override
    public CountSql getSql() {
        return this;
    }

    @Override
    public boolean isUseAlias() {
        return true;
    }


    @Override
    public CountSql fromDefault() {
        return from(mapper.getDefaultTable());
    }

    @Override
    public PrepareStatement getPrepareStatement() {

        boolean useAlias = isUseAlias();
        boolean distinct = getDistinct().isValue();

        PrepareStatement columnPrepareStatement = getColumnPrepareStatement(useAlias);

        String columnStr = columnPrepareStatement.getPrepareSql();
        if (StringUtils.isBlank(columnStr) && distinct) {
            throw new SqlException("Columns cannot be empty while at distinct defaultimpl!");
        }

        // 构造count字段
        StringBuilder prepareSql = new StringBuilder();
        prepareSql.append("SELECT COUNT(")
                .append(distinct ? ("DISTINCT " + columnStr) : "*")
                .append(")");

        List<ColumnValue> values = new ArrayList<>();

        append(
                Arrays.asList(
                        getFromPrepareStatement(useAlias),
                        getJoinOnPrepareStatement(useAlias),
                        getWherePrepareStatement(useAlias),
                        getGroupByPrepareStatement(useAlias),
                        getHavingPrepareStatement(useAlias)
                ), prepareSql, values
        );
        return new PrepareStatement(prepareSql.toString(), values);
    }

    @Override
    public int executeCount() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawCount(statement);
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
}
