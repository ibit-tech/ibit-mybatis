package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.Criteria;
import tech.ibit.mybatis.sqlbuilder.CriteriaItem;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * WhereSupport实现
 *
 * @author IBIT程序猿
 */
public class WhereSupportImpl<T> extends CriteriaSupportImpl
        implements SqlSupport<T>, WhereSupport<T> {

    /**
     * sql 对象
     */
    private final T sql;

    /**
     * where
     */
    private final ListField<Criteria> where;

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public WhereSupportImpl(T sql) {
        this(sql, new ListField<>());
    }

    /**
     * 构造函数
     *
     * @param sql   sql对象
     * @param where where对象
     */
    private WhereSupportImpl(T sql, ListField<Criteria> where) {
        this.sql = sql;
        this.where = where;
    }

    /**
     * 对象复制（浅复制）
     *
     * @param sql sql对象
     * @param <K> sql对象模板
     * @return 复制后的对象
     */
    public <K> WhereSupportImpl<K> copy(K sql) {
        return new WhereSupportImpl<>(sql, where);
    }

    /**
     * 返回where条件
     *
     * @return where条件
     */
    public ListField<Criteria> getWhere() {
        return where;
    }

    @Override
    public T getSql() {
        return sql;
    }

    /**
     * `WHERE` 语句
     *
     * @param criteria WHERE相关条件
     * @return SQL对象
     * @see Criteria
     */
    @Override
    public T where(Criteria criteria) {
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
    public T where(List<Criteria> criterion) {
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
    public T andWhere(CriteriaItem item) {
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
    public T andWhere(List<Criteria> criterion) {
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
    public T orWhere(CriteriaItem item) {
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
    public T orWhere(List<Criteria> criterion) {
        return where(Criteria.or(criterion));
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getWherePrepareStatement(boolean useAlias) {
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