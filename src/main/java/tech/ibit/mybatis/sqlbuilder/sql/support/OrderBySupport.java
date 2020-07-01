package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.IOrderBy;

import java.util.List;

/**
 * OrderBy Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface OrderBySupport<T> {


    /**
     * `ORDER BY` 语句
     *
     * @param orderBy 相关orderBy
     * @return SQL对象
     * @see IOrderBy
     */
    T orderBy(IOrderBy orderBy);

    /**
     * `ORDER BY` 语句
     *
     * @param orderBys 相关orderBy列表
     * @return SQL对象
     * @see IOrderBy
     */
    T orderBy(List<IOrderBy> orderBys);
}
