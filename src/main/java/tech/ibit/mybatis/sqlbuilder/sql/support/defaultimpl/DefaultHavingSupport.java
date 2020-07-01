package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.Criteria;
import tech.ibit.mybatis.sqlbuilder.CriteriaItem;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.HavingSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultHavingSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultHavingSupport<T> extends DefaultSqlSupport<T>,
        HavingSupport<T>, DefaultCriteriaSupport {


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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    default T orHaving(List<Criteria> havings) {
        having(Criteria.or(havings));
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getHavingPrepareStatement(boolean useAlias) {
        List<Criteria> criterion = getHaving().getItems();
        if (CollectionUtils.isEmpty(criterion)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();
        prepareSql.append(" HAVING ");

        append(criterion, useAlias, prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }
}
