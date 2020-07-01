package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Table;

import java.util.List;

/**
 * 更新表Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface UpdateTableSupport<T> extends SqlSupport<T> {

    /**
     * `UPDATE table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    T update(Table table);

    /**
     * `UPDATE table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    T update(List<Table> tables);

}
