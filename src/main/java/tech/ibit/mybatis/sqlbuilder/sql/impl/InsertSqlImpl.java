package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.DefaultInsertTableSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.DefaultUseAliasSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.DefaultValuesSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InsertSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class InsertSqlImpl extends SqlLogImpl implements InsertSql,
        DefaultInsertTableSupport<InsertSql>,
        DefaultValuesSupport<InsertSql>,
        DefaultUseAliasSupport {

    /**
     * fromDefault
     */
    private final ListField<Table> insertTable = new ListField<>();

    /**
     * column
     */
    private final ListField<Column> column = new ListField<>();

    /**
     * value
     */
    private final ListField<Object> value = new ListField<>();

    /**
     * 基础mapper
     */
    private final RawMapper<?> mapper;

    public InsertSqlImpl(RawMapper<?> mapper) {
        this.mapper = mapper;
    }


    @Override
    public boolean isUseAlias() {
        return false;
    }

    @Override
    public InsertSql getSql() {
        return this;
    }

    @Override
    public InsertSql insertDefault() {
        return insert(mapper.getDefaultTable());
    }

    @Override
    public PrepareStatement getPrepareStatement() {

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        append(
                Arrays.asList(
                        getInsertPrepareStatement(isUseAlias()),
                        getColumnPrepareStatement(),
                        getValuePrepareStatement()
                ), prepareSql, values);


        return new PrepareStatement(prepareSql.toString(), values);
    }


    @Override
    public int executeInsert() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawInsert(statement);
    }

    @Override
    public int executeInsertWithGenerateKeys(KeyValuePair key) {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawInsertWithGenerateKeys(statement, key);
    }

    @Override
    public ListField<Table> getInsertTable() {
        return insertTable;
    }

    @Override
    public ListField<Column> getColumn() {
        return column;
    }

    @Override
    public ListField<Object> getValue() {
        return value;
    }
}
