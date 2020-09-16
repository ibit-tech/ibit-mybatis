package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SetItem;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.SetSupport;

import java.util.List;

/**
 * SetSupport实现
 *
 * @author IBIT程序猿
 */
public class SetSupportImpl<T> extends BaseSetSupportImpl<T> implements SetSupport<T> {

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public SetSupportImpl(T sql) {
        super(sql, new ListField<>());
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
        return getSetItemPrepareStatement(" SET ", useAlias);
    }
}
