package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SetItem;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.SetSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * SetSupport实现
 *
 * @author IBIT程序猿
 */
public class SetSupportImpl<T> implements SqlSupport<T>,
        SetSupport<T>, PrepareStatementBuildSupport {

    /**
     * sql 对象
     */
    private final T sql;

    /**
     * set
     */
    private final ListField<SetItem> set;


    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public SetSupportImpl(T sql) {
        this.sql = sql;
        this.set = new ListField<>();
    }

    @Override
    public T getSql() {
        return sql;
    }

    /**
     * 获取设置内容
     *
     * @return 设置内容
     */
    public ListField<SetItem> getSet() {
        return set;
    }


    /**
     * 增加设置内容
     *
     * @param item 设置项
     * @return SQL对象
     */
    @Override
    public T set(SetItem item) {
        getSet().addItem(item);
        return getSql();
    }

    /**
     * 批量增加设置内容
     *
     * @param items 设置项
     * @return SQL对象
     */
    @Override
    public T set(List<SetItem> items) {
        getSet().addItems(items);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getSetItemPrepareStatement(boolean useAlias) {
        List<SetItem> setItems = getSet().getItems();
        if (CollectionUtils.isEmpty(setItems)) {
            return new PrepareStatement("", Collections.emptyList());
        }

        return getPrepareStatement(" SET ", setItems, ", ", useAlias);
    }
}
