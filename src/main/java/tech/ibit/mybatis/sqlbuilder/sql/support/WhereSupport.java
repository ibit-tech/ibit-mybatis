package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Criteria;
import tech.ibit.mybatis.sqlbuilder.CriteriaItem;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * Where Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface WhereSupport<T> extends SqlSupport<T> {

    /**
     * 返回where条件
     *
     * @return where条件
     */
    ListField<Criteria> getWhere();

    /**
     * `WHERE` 语句
     *
     * @param criteria WHERE相关条件
     * @return SQL对象
     * @see Criteria
     */
    default T where(Criteria criteria) {
        getWhere().addItem(criteria);
        return getSql();
    }

    /**
     * `WHERE` 语句
     *
     * @param criterion WHERE相关条件列表
     * @return SQL对象
     * @see Criteria
     */
    default T where(List<Criteria> criterion) {
        getWhere().addItems(criterion);
        return getSql();
    }

    /**
     * `WHERE AND` 语句
     *
     * @param item WHERE相关条件
     * @return SQL对象
     * @see Criteria
     */
    default T andWhere(CriteriaItem item) {
        return where(item.and());
    }

    /**
     * `WHERE AND` 语句
     *
     * @param criterion WHERE相关条件列表
     * @return SQL对象
     * @see Criteria
     */
    default T andWhere(List<Criteria> criterion) {
        return where(Criteria.and(criterion));
    }

    /**
     * `WHERE OR`语句
     *
     * @param item WHERE相关条件
     * @return SQL对象
     * @see Criteria
     */
    default T orWhere(CriteriaItem item) {
        return where(item.or());
    }

    /**
     * `WHERE OR`语句
     *
     * @param criterion WHERE相关条件列表
     * @return SQL对象
     * @see Criteria
     */
    default T orWhere(List<Criteria> criterion) {
        return where(Criteria.or(criterion));
    }
}