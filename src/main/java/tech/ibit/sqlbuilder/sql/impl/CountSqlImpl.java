package tech.ibit.sqlbuilder.sql.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.template.mapper.RawMapper;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.exception.SqlException;
import tech.ibit.sqlbuilder.sql.CountSql;
import tech.ibit.sqlbuilder.sql.field.BooleanField;
import tech.ibit.sqlbuilder.sql.field.ListField;
import tech.ibit.sqlbuilder.sql.support.statement.*;

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
public class CountSqlImpl<T> extends SqlLogImpl
        implements CountSql<T>,
        DefaultColumnSupport<CountSql<T>>,
        DefaultFromSupport<CountSql<T>>,
        DefaultJoinOnSupport<CountSql<T>>,
        DefaultWhereSupport<CountSql<T>>,
        DefaultGroupBySupport<CountSql<T>>,
        DefaultHavingSupport<CountSql<T>> {


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
    private RawMapper<T> mapper;

    public CountSqlImpl(RawMapper<T> mapper) {
        this.mapper = mapper;
    }


    @Override
    public CountSql<T> getSql() {
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
            throw new SqlException("Columns cannot be empty while at distinct statement!");
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
    public int doCount() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawCount(statement);
    }
}
