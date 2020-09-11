package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.UpdateTableSupport;

import java.util.List;

/**
 * DefaultUpdateTableSupport
 *
 * @author IBIT程序猿
 */
public class UpdateTableSupportImpl<T> extends TableSupportImpl implements SqlSupport<T>,
        UpdateTableSupport<T> {

    /**
     * sql 对象
     */
    private final T sql;

    /**
     * fromDefault
     */
    private final ListField<Table> updateTable;

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public UpdateTableSupportImpl(T sql) {
        this.sql = sql;
        this.updateTable = new ListField<>();
    }

    /**
     * 获取更新表
     *
     * @return 插入表
     */
    private ListField<Table> getUpdateTable() {
        return updateTable;
    }

    @Override
    public T getSql() {
        return sql;
    }

    /**
     * `UPDATE table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    @Override
    public T update(Table table) {
        getUpdateTable().addItem(table);
        return getSql();
    }

    /**
     * `UPDATE table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    @Override
    public T update(List<Table> tables) {
        getUpdateTable().addItems(tables);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getUpdatePrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getUpdateTable(), "UPDATE ", useAlias);
    }

}
