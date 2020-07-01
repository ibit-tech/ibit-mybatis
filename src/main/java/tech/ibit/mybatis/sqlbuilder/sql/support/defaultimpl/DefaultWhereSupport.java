package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.Criteria;
import tech.ibit.mybatis.sqlbuilder.CriteriaItem;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultWhereSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultWhereSupport<T> extends DefaultSqlSupport<T>,
        WhereSupport<T>, DefaultCriteriaSupport {


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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    default T orWhere(List<Criteria> criterion) {
        return where(Criteria.or(criterion));
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getWherePrepareStatement(boolean useAlias) {
        List<Criteria> criterion = getWhere().getItems();
        if (CollectionUtils.isEmpty(criterion)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        prepareSql.append(" WHERE ");

        append(criterion, useAlias, prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }

}
