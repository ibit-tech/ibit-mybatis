package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.support.UseAliasSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.impl.InsertTableSupportImpl;
import tech.ibit.mybatis.sqlbuilder.sql.support.impl.OnDuplicateKeyUpdateSupportImpl;
import tech.ibit.mybatis.sqlbuilder.sql.support.impl.PrepareStatementBuildSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.impl.ValuesSupportImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InsertSql实现
 *
 * @author iBit程序猿
 * @version 2.0
 */
public class InsertSqlImpl extends SqlLogImpl implements InsertSql,
        UseAliasSupport, PrepareStatementBuildSupport {

    /**
     * insert table 支持
     */
    private final InsertTableSupportImpl<InsertSql> insertTableSupport;

    /**
     * value 支持
     */
    private final ValuesSupportImpl<InsertSql> valuesSupport;

    /**
     * on duplicate key update 支持
     */
    private final OnDuplicateKeyUpdateSupportImpl<InsertSql> onDuplicateKeyUpdateSupport;

    /**
     * 基础mapper
     */
    private final Mapper<?> mapper;

    public InsertSqlImpl(Mapper<?> mapper) {
        this.mapper = mapper;
        this.insertTableSupport = new InsertTableSupportImpl<>(this);
        this.valuesSupport = new ValuesSupportImpl<>(this);
        this.onDuplicateKeyUpdateSupport = new OnDuplicateKeyUpdateSupportImpl<>(this);
    }

    @Override
    public InsertSql insert(Table table) {
        return insertTableSupport.insert(table);
    }

    @Override
    public InsertSql insert(List<Table> tables) {
        return insertTableSupport.insert(tables);
    }

    @Override
    public InsertSql values(List<? extends ColumnValue> columnValues) {
        return valuesSupport.values(columnValues);
    }

    @Override
    public InsertSql values(ColumnValue columnValue) {
        return valuesSupport.values(columnValue);
    }

    @Override
    public InsertSql values(Column column, Object value) {
        return valuesSupport.values(column, value);
    }

    @Override
    public InsertSql values(List<Column> columns, List<Object> values) {
        return valuesSupport.values(columns, values);
    }

    @Override
    public InsertSql onDuplicateKeyUpdate(SetItem item) {
        return onDuplicateKeyUpdateSupport.onDuplicateKeyUpdate(item);
    }

    @Override
    public InsertSql onDuplicateKeyUpdate(List<SetItem> items) {
        return onDuplicateKeyUpdateSupport.onDuplicateKeyUpdate(items);
    }

    @Override
    public boolean isUseAlias() {
        return false;
    }


    @Override
    public InsertSql insertDefault() {
        return insert(mapper.getDefaultTable());
    }

    @Override
    public PrepareStatement getPrepareStatement() {


        // 没有指定表，指定默认的
        if (CollectionUtils.isEmpty(insertTableSupport.getTable().getItems())) {
            insert(mapper.getDefaultTable());
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        append(
                Arrays.asList(
                        insertTableSupport.getInsertPrepareStatement(isUseAlias()),
                        valuesSupport.getColumnPrepareStatement(),
                        valuesSupport.getValuePrepareStatement(),
                        onDuplicateKeyUpdateSupport.getOnDuplicateKeyUpdatePrepareStatement(isUseAlias())
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

}
