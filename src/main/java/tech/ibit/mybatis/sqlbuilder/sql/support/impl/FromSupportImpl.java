package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.FromSupport;

import java.util.List;

/**
 * FromSupport实现
 *
 * @param <T> 对象模板类型
 * @author IBIT程序猿
 */
public class FromSupportImpl<T> extends BaseTableSupportImpl<T> implements FromSupport<T> {

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public FromSupportImpl(T sql) {
        this(sql, new ListField<>());
    }

    /**
     * 构造函数
     *
     * @param sql  sql对象
     * @param from from对象
     */
    private FromSupportImpl(T sql, ListField<Table> from) {
        super(sql, from);
    }

    /**
     * 对象复制（浅复制）
     *
     * @param sql sql对象
     * @param <K> sql对象模板
     * @return 复制后的对象
     */
    public <K> FromSupportImpl<K> copy(K sql) {
        return new FromSupportImpl<>(sql, getTable());
    }

    /**
     * `FROM table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    @Override
    public T from(Table table) {
        getTable().addItem(table);
        return getSql();
    }

    /**
     * `FROM table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    @Override
    public T from(List<Table> tables) {
        getTable().addItems(tables);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getFromPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(" FROM ", useAlias);
    }

}
