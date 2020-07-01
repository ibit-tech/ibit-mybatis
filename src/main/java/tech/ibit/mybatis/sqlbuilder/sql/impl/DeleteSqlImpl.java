package tech.ibit.mybatis.sqlbuilder.sql.impl;

import lombok.Getter;
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
@Getter
public class DeleteSqlImpl extends SqlLogImpl implements DeleteSql,
        DefaultDeleteSupport<DeleteSql>,
        DefaultFromSupport<DeleteSql>,
        DefaultJoinOnSupport<DeleteSql>,
        DefaultWhereSupport<DeleteSql>,
        DefaultUseAliasSupport {

    /**
     * 删除项
     */
    private ListField<Table> deleteItem = new ListField<>();

    /**
     * 表
     */
    private ListField<Table> from = new ListField<>();

    /**
     * Join on
     */
    private ListField<JoinOn> joinOn = new ListField<>();

    /**
     * where语句
     */
    private ListField<Criteria> where = new ListField<>();


    /**
     * 基础mapper
     */
    private RawMapper mapper;

    public DeleteSqlImpl(RawMapper mapper) {
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

    /**
     * 删除表，item和from同时设置
     *
     * @param table 表
     * @return SQL对象
     */
    @Override
    public DeleteSql deleteFrom(Table table) {
        delete(table);
        from(table);
        return getSql();
    }


    /**
     * 删除表，item和from同时设置
     *
     * @param tables 表列表
     * @return SQL对象
     */
    @Override
    public DeleteSql deleteFrom(List<Table> tables) {
        delete(tables);
        from(tables);
        return getSql();
    }
}
