package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SetItem;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.SetSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * DefaultSetSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultSetSupport<T> extends DefaultSqlSupport<T>,
        SetSupport<T>, DefaultPrepareStatementSupport {


    /**
     * 获取设置内容
     *
     * @return 设置内容
     */
    ListField<SetItem> getSet();

    /**
     * 增加设置内容
     *
     * @param item 设置项
     * @return SQL对象
     */
    @Override
    default T set(SetItem item) {
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
    default T set(List<SetItem> items) {
        getSet().addItems(items);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getSetItemPrepareStatement(boolean useAlias) {
        List<SetItem> setItems = getSet().getItems();
        if (CollectionUtils.isEmpty(setItems)) {
            return new PrepareStatement("", Collections.emptyList());
        }

        return getPrepareStatement(" SET ", setItems, ", ", useAlias);
    }
}
