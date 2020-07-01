package tech.ibit.mybatis.sqlbuilder.sql.impl;

import lombok.Getter;
import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultInsertTableSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultValuesSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InsertSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
@Getter
public class InsertSqlImpl extends SqlLogImpl implements InsertSql,
        DefaultInsertTableSupport<InsertSql>,
        DefaultValuesSupport<InsertSql> {

    /**
     * from
     */
    private ListField<Table> insertTable = new ListField<>();

    /**
     * column
     */
    private ListField<Column> column = new ListField<>();

    /**
     * value
     */
    private ListField<Object> value = new ListField<>();

    /**
     * 基础mapper
     */
    private RawMapper mapper;

    public InsertSqlImpl(RawMapper mapper) {
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
        return mapper.rawInsert(getPrepareStatement());
    }

    @Override
    public int executeInsertWithGenerateKeys(KeyValuePair key) {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawInsertWithGenerateKeys(statement, key);
    }
}
