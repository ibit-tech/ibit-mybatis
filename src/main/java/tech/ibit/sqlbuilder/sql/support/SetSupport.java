package tech.ibit.sqlbuilder.sql.support;

import tech.ibit.sqlbuilder.SetItem;
import tech.ibit.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * SetSupport
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface SetSupport<T> extends SqlSupport<T> {

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
    default T set(List<SetItem> items) {
        getSet().addItems(items);
        return getSql();
    }

}
