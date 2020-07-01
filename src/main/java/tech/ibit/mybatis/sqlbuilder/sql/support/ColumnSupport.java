package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.IColumn;

import java.util.List;

/**
 * Column支持
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface ColumnSupport<T> extends SqlSupport<T> {

    /**
     * `t.column1, t.column2, ...`语句, "t": 为表的别名
     *
     * @param columns 查询字段对象
     * @return SQL对象
     * @see IColumn
     */
    T column(List<? extends IColumn> columns);

    /**
     * `t.column`语句, "t": 为表的别名
     *
     * @param column 查询字段对象
     * @return SQL对象
     * @see IColumn
     */
    T column(IColumn column);

    /**
     * 传入实体类
     *
     * @param poClass 实体类
     * @return SQL对象
     */
    T columnPo(Class poClass);

}
