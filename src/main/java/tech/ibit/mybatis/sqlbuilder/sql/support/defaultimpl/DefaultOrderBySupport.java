package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.IOrderBy;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.OrderBySupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * DefaultOrderBySupport
 *
 * @author IBIT程序猿
 */
public interface DefaultOrderBySupport<T> extends OrderBySupport<T>, DefaultPrepareStatementSupport {

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
    @Override
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
    @Override
    default T orderBy(List<IOrderBy> orderBys) {
        getOrderBy().addItems(orderBys);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getOrderByPrepareStatement(boolean useAlias) {
        List<IOrderBy> orderBys = getOrderBy().getItems();
        if (CollectionUtils.isEmpty(orderBys)) {
            return new PrepareStatement("", Collections.emptyList());
        }
        return getPrepareStatement(" ORDER BY ", orderBys, ", ", useAlias);
    }

}
