package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.SetItem;

import java.util.List;

/**
 * SetSupport
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface SetSupport<T> extends SqlSupport<T> {

    /**
     * 增加设置内容
     *
     * @param item 设置项
     * @return SQL对象
     */
    T set(SetItem item);

    /**
     * 批量增加设置内容
     *
     * @param items 设置项
     * @return SQL对象
     */
    T set(List<SetItem> items);

}
