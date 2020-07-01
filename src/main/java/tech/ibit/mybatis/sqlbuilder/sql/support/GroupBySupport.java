package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.IColumn;

import java.util.List;

/**
 * GroupBy Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface GroupBySupport<T> {

    /**
     * `GROUP BY t1.column`语句
     *
     * @param groupBy 相关列a
     * @return SQL对象
     * @see IColumn
     */
    T groupBy(Column groupBy);

    /**
     * `GROUP BY t1.column1, t2.column2, ...`语句
     *
     * @param groupBys 相关列列表
     * @return SQL对象
     * @see IColumn
     */
    T groupBy(List<Column> groupBys);

}
