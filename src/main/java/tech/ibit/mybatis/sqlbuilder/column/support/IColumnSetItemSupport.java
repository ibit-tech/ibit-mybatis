package tech.ibit.mybatis.sqlbuilder.column.support;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.SetItem;

/**
 * 列构造setItem
 *
 * @author iBit程序猿
 * @version 2.0
 */
public interface IColumnSetItemSupport extends IColumnSupport {

    /**
     * 设置具体值
     *
     * @param value 值
     * @return 设置item
     */
    default SetItem set(Object value) {
        return SetItem.set((Column) getColumn(), value);
    }

    /**
     * 设置自增长
     *
     * @param value 值
     * @return 自增长item
     */
    default SetItem increaseSet(Number value) {
        return SetItem.increaseSet((Column) getColumn(), value);
    }

    /**
     * 设置自减
     *
     * @param value 值OL
     * @return 自减item
     */
    default SetItem decreaseSet(Number value) {
        return SetItem.decreaseSet((Column) getColumn(), value);
    }
}
