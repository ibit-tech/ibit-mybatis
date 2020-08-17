package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.DeleteSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class DeleteSqlImpl extends SqlLogImpl implements DeleteSql,
        DefaultDeleteSupport<DeleteSql>,
        DefaultFromSupport<DeleteSql>,
        DefaultJoinOnSupport<DeleteSql>,
        DefaultWhereSupport<DeleteSql>,
        DefaultUseAliasSupport {

    /**
     * 删除项
     */
    private final ListField<Table> deleteItem = new ListField<>();

    /**
     * 表
     */
    private final ListField<Table> from = new ListField<>();

    /**
     * Join on
     */
    private final ListField<JoinOn> joinOn = new ListField<>();

    /**
     * where语句
     */
    private final ListField<Criteria> where = new ListField<>();


    /**
     * 基础mapper
     */
    private final RawMapper<?> mapper;

    public DeleteSqlImpl(RawMapper<?> mapper) {
        this.mapper = mapper;
    }

    @Override
    public DeleteSql getSql() {
        return this;
    }

    @Override
    public boolean isUseAlias() {
        return isMultiTables();
    }

    /**
     * 是否关联多张表
     *
     * @return 关联多张表
     */
    private boolean isMultiTables() {
        return from.getItems().size() > 1 || joinOn.getItems().size() > 0;
    }

    @Override
    public PrepareStatement getPrepareStatement() {
        if (getWhere().getItems().isEmpty()) {
            throw new SqlException("Where cannot be empty when do deleting!");
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        boolean useAlias = isUseAlias();
        boolean multiTables = isMultiTables();

        prepareSql.append("DELETE");

        append(
                Arrays.asList(
                        getDeleteItemPrepareStatement(multiTables),
                        getFromPrepareStatement(useAlias),
                        getJoinOnPrepareStatement(useAlias),
                        getWherePrepareStatement(useAlias)
                ), prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }


    @Override
    public int executeDelete() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawUpdate(statement);
    }

    @Override
    public DeleteSql deleteFromDefault() {
        return deleteFrom(mapper.getDefaultTable());
    }

    @Override
    public DeleteSql deleteFrom(Table table) {
        delete(table);
        from(table);
        return getSql();
    }


    @Override
    public DeleteSql deleteFrom(List<Table> tables) {
        delete(tables);
        from(tables);
        return getSql();
    }

    @Override
    public ListField<Table> getDeleteItem() {
        return deleteItem;
    }

    @Override
    public ListField<Table> getFrom() {
        return from;
    }

    @Override
    public ListField<JoinOn> getJoinOn() {
        return joinOn;
    }

    @Override
    public ListField<Criteria> getWhere() {
        return where;
    }
}
