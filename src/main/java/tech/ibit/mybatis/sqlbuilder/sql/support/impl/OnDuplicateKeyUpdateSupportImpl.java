package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SetItem;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.OnDuplicateKeyUpdateSupport;

import java.util.List;

/**
 * SetSupport实现
 *
 * @author iBit程序猿
 */
public class OnDuplicateKeyUpdateSupportImpl<T>
        extends BaseSetSupportImpl<T> implements OnDuplicateKeyUpdateSupport<T> {

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public OnDuplicateKeyUpdateSupportImpl(T sql) {
        super(sql, new ListField<>());
    }

    @Override
    public T onDuplicateKeyUpdate(SetItem item) {
        getSet().addItem(item);
        return getSql();
    }

    @Override
    public T onDuplicateKeyUpdate(List<SetItem> items) {
        getSet().addItems(items);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getOnDuplicateKeyUpdatePrepareStatement(boolean useAlias) {
        return getSetItemPrepareStatement(" ON DUPLICATE KEY UPDATE ", useAlias);
    }
}
