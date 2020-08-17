package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.UpdateSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UpdateSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class UpdateSqlImpl extends SqlLogImpl implements UpdateSql,
        DefaultUpdateTableSupport<UpdateSql>,
        DefaultJoinOnSupport<UpdateSql>,
        DefaultSetSupport<UpdateSql>,
        DefaultWhereSupport<UpdateSql>,
        DefaultUseAliasSupport {

    /**
     * fromDefault
     */
    private final ListField<Table> updateTable = new ListField<>();

    /**
     * join on
     */
    private final ListField<JoinOn> joinOn = new ListField<>();

    /**
     * set
     */
    private final ListField<SetItem> set = new ListField<>();

    /**
     * where
     */
    private final ListField<Criteria> where = new ListField<>();

    /**
     * 基础mapper
     */
    private final RawMapper<?> mapper;

    public UpdateSqlImpl(RawMapper<?> mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean isUseAlias() {
        return true;
    }

    @Override
    public UpdateSql getSql() {
        return this;
    }


    @Override
    public UpdateSql updateDefault() {
        return update(mapper.getDefaultTable());
    }

    @Override
    public PrepareStatement getPrepareStatement() {
        if (getWhere().getItems().isEmpty()) {
            throw new SqlException("Where cannot be empty when do updating!");
        }

        if (getSet().getItems().isEmpty()) {
            throw new SqlException("Set cannot be empty when do updating!");
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();
        boolean useAlias = isUseAlias();

        append(
                Arrays.asList(
                        getUpdatePrepareStatement(useAlias),
                        getJoinOnPrepareStatement(useAlias),
                        getSetItemPrepareStatement(useAlias),
                        getWherePrepareStatement(useAlias)
                ), prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }

    @Override
    public int executeUpdate() {
        PrepareStatement statement = getPrepareStatement();
        doLog(statement);
        return mapper.rawUpdate(statement);
    }

    /**
     * Gets the value of updateTable
     *
     * @return the value of updateTable
     */
    public ListField<Table> getUpdateTable() {
        return updateTable;
    }

    /**
     * Gets the value of joinOn
     *
     * @return the value of joinOn
     */
    public ListField<JoinOn> getJoinOn() {
        return joinOn;
    }

    /**
     * Gets the value of set
     *
     * @return the value of set
     */
    public ListField<SetItem> getSet() {
        return set;
    }

    /**
     * Gets the value of where
     *
     * @return the value of where
     */
    public ListField<Criteria> getWhere() {
        return where;
    }

    /**
     * Gets the value of mapper
     *
     * @return the value of mapper
     */
    public RawMapper<?> getMapper() {
        return mapper;
    }
}
