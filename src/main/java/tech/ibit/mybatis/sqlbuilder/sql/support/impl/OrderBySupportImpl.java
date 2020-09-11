package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.IOrderBy;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.OrderBySupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * OrderBySupport实现
 *
 * @author IBIT程序猿
 */
public class OrderBySupportImpl<T> implements SqlSupport<T>,
        OrderBySupport<T>, PrepareStatementBuildSupport {

    private final T sql;

    /**
     * Order by
     */
    private final ListField<IOrderBy> orderBy;

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public OrderBySupportImpl(T sql) {
        this.sql = sql;
        this.orderBy = new ListField<>();
    }

    /**
     * Order by
     *
     * @return Order by
     */
    private ListField<IOrderBy> getOrderBy() {
        return orderBy;
    }

    @Override
    public T getSql() {
        return sql;
    }

    /**
     * `ORDER BY` 语句
     *
     * @param orderBy 相关orderBy
     * @return SQL对象
     * @see IOrderBy
     */
    @Override
    public T orderBy(IOrderBy orderBy) {
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
    @Override
    public T orderBy(List<IOrderBy> orderBys) {
        getOrderBy().addItems(orderBys);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getOrderByPrepareStatement(boolean useAlias) {
        List<IOrderBy> orderBys = getOrderBy().getItems();
        if (CollectionUtils.isEmpty(orderBys)) {
            return new PrepareStatement("", Collections.emptyList());
        }
        return getPrepareStatement(" ORDER BY ", orderBys, ", ", useAlias);
    }

}
