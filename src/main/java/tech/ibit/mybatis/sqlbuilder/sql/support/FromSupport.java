package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Table;

import java.util.List;

/**
 * From Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface FromSupport<T> {

    /**
     * `FROM table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    T from(Table table);

    /**
     * `FROM table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    T from(List<Table> tables);

}
