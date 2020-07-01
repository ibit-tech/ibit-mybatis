package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * 更新表Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface UpdateTableSupport<T> extends SqlSupport<T> {

    /**
     * 获取更新表
     *
     * @return 插入表
     */
    ListField<Table> getUpdateTable();


    /**
     * `UPDATE table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    default T update(Table table) {
        getUpdateTable().addItem(table);
        return getSql();
    }

    /**
     * `UPDATE table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    default T update(List<Table> tables) {
        getUpdateTable().addItems(tables);
        return getSql();
    }

}