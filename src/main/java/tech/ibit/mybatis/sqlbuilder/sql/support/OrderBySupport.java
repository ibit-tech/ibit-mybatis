package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.IOrderBy;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * OrderBy Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface OrderBySupport<T> extends SqlSupport<T> {

    /**
     * Order by
     *
     * @return Order by
     */
    ListField<IOrderBy> getOrderBy();


    /**
     * `ORDER BY` 语句
     *
     * @param orderBy 相关orderBy
     * @return SQL对象
     * @see IOrderBy
     */
    default T orderBy(IOrderBy orderBy) {
        getOrderBy().addItem(orderBy);
        return getSql();
    }

    /**
     * `ORDER BY` 语句
     *
     * @param orderBys 相关orderBy列表
     * @return SQL对象
     * @see IOrderBy
     */
    default T orderBy(List<IOrderBy> orderBys) {
        getOrderBy().addItems(orderBys);
        return getSql();
    }
}
