package tech.ibit.mybatis.sqlbuilder.sql.impl;

import lombok.Getter;
import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.UpdateSql;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultJoinOnSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultSetSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultUpdateTableSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.statement.DefaultWhereSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UpdateSqlImpl
 *
 * @author IBIT程序猿
 * @version 2.0
 */
@Getter
public class UpdateSqlImpl extends SqlLogImpl implements UpdateSql,
        DefaultUpdateTableSupport<UpdateSql>,
        DefaultJoinOnSupport<UpdateSql>,
        DefaultSetSupport<UpdateSql>,
        DefaultWhereSupport<UpdateSql> {

    /**
     * from
     */
    private ListField<Table> updateTable = new ListField<>();

    /**
     * join on
     */
    private ListField<JoinOn> joinOn = new ListField<>();

    /**
     * set
     */
    private ListField<SetItem> set = new ListField<>();

    /**
     * where
     */
    private ListField<Criteria> where = new ListField<>();

    /**
     * 基础mapper
     */
    private RawMapper mapper;

    public UpdateSqlImpl(RawMapper mapper) {
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
}
