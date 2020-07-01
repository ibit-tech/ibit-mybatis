package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * DeleteItem Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface DeleteSupport<T> extends SqlSupport<T> {

    /**
     * Delete Item
     *
     * @return 删除项
     */
    ListField<Table> getDeleteItem();


    /**
     * `DELETE t1.*` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    default T delete(Table table) {
        getDeleteItem().addItem(table);
        return getSql();
    }

    /**
     * `DELETE t1.*, t2.* ...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    default T delete(List<Table> tables) {
        getDeleteItem().addItems(tables);
        return getSql();
    }

}
