package tech.ibit.mybatis.sqlbuilder.sql.field;

import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义列表字段
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class ListField<T> {

    /**
     * 列表值
     */
    private List<T> items = new ArrayList<>();


    /**
     * 增加对象
     *
     * @param item 对象
     */
    public void addItem(T item) {
        items.add(item);
    }

    /**
     * 批量增加对象
     *
     * @param items 对象集合
     */
    public void addItems(List<? extends T> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            this.items.addAll(items);
        }
    }


    /**
     * Gets the value of items
     *
     * @return the value of items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Sets the items
     * <p>You can use getItems() to get the value of items</p>
     *
     * @param items items
     */
    public void setItems(List<T> items) {
        this.items = items;
    }
}
