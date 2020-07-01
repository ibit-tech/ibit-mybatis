package tech.ibit.mybatis.sqlbuilder.sql.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.RawMapper;
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
@Getter
@Setter
public class CountSqlImpl extends SqlLogImpl
        implements CountSql,
        DefaultDistinctSupport<CountSql>,
        DefaultColumnSupport<CountSql>,
        DefaultFromSupport<CountSql>,
        DefaultJoinOnSupport<CountSql>,
        DefaultWhereSupport<CountSql>,
        DefaultGroupBySupport<CountSql>,
        DefaultHavingSupport<CountSql> {


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
    private RawMapper mapper;

    public CountSqlImpl(RawMapper mapper) {
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
}
