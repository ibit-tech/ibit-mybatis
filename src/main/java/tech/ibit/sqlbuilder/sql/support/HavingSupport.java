package tech.ibit.sqlbuilder.sql.support;

import tech.ibit.sqlbuilder.Criteria;
import tech.ibit.sqlbuilder.CriteriaItem;
import tech.ibit.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * Having Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface HavingSupport<T> extends SqlSupport<T> {

    /**
     * Having
     *
     * @return Having
     */
    ListField<Criteria> getHaving();


    /**
     * `HAVING`语句
     *
     * @param having having语句对象
     * @return SQL对象
     */
    default T having(Criteria having) {
        getHaving().addItem(having);
        return getSql();
    }

    /**
     * `HAVING`语句
     *
     * @param havings having语句对象列表
     * @return SQL对象
     */
    default T having(List<Criteria> havings) {
        getHaving().addItems(havings);
        return getSql();
    }

    /**
     * `HAVING AND item`语句
     *
     * @param havingItem having语句对象
     * @return SQL对象
     */
    default T andHaving(CriteriaItem havingItem) {
        having(havingItem.and());
        return getSql();
    }

    /**
     * `HAVING AND (havings)`语句
     *
     * @param havings having语句对象列表
     * @return SQL对象
     */
    default T andHaving(List<Criteria> havings) {
        having(Criteria.and(havings));
        return getSql();
    }


    /**
     * `HAVING OR item`语句
     *
     * @param havingItem having语句对象
     * @return SQL对象
     */
    default T orHaving(CriteriaItem havingItem) {
        having(havingItem.or());
        return getSql();
    }

    /**
     * `HAVING OR (havings)`语句（多个OR关系）
     *
     * @param havings having语句对象列表
     * @return SQL对象
     */
    default T orHaving(List<Criteria> havings) {
        having(Criteria.or(havings));
        return getSql();
    }

}
