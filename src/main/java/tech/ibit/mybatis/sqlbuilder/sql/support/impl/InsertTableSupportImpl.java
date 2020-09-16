package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.InsertTableSupport;

import java.util.List;

/**
 * InsertTableSupport实现
 *
 * @param <T> 对象模板类型
 * @author IBIT程序猿
 */
public class InsertTableSupportImpl<T> extends BaseTableSupportImpl<T> implements InsertTableSupport<T> {

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public InsertTableSupportImpl(T sql) {
        super(sql, new ListField<>());
    }


    /**
     * `INSERT INTO table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    @Override
    public T insert(Table table) {
        getTable().addItem(table);
        return getSql();
    }

    /**
     * `INSERT INTO table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    @Override
    public T insert(List<Table> tables) {
        getTable().addItems(tables);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getInsertPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement("INSERT INTO ", useAlias);
    }
}
