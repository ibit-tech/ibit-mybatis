package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.JoinOnSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.List;

/**
 * JoinOnSupport实现
 *
 * @author IBIT程序猿
 */
public class JoinOnSupportImpl<T> implements SqlSupport<T>,
        JoinOnSupport<T>, PrepareStatementBuildSupport {

    /**
     * sql 对象
     */
    private final T sql;


    /**
     * Join on
     */
    private final ListField<JoinOn> joinOn;

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public JoinOnSupportImpl(T sql) {
        this(sql, new ListField<>());
    }

    /**
     * 构造函数
     *
     * @param sql    sql对象
     * @param joinOn joinOn对象
     */
    private JoinOnSupportImpl(T sql, ListField<JoinOn> joinOn) {
        this.sql = sql;
        this.joinOn = joinOn;
    }

    /**
     * 对象复制（浅复制）
     *
     * @param sql sql对象
     * @param <K> sql对象模板
     * @return 复制后的对象
     */
    public <K> JoinOnSupportImpl<K> copy(K sql) {
        return new JoinOnSupportImpl<>(sql, joinOn);
    }


    /**
     * Join on
     *
     * @return JoinOn
     */
    public ListField<JoinOn> getJoinOn() {
        return joinOn;
    }

    @Override
    public T getSql() {
        return sql;
    }

    /**
     * JoinOn操作
     *
     * @param joinOn JoinOn操作
     * @return SQL对象
     */
    @Override
    public T joinOn(JoinOn joinOn) {
        getJoinOn().addItem(joinOn);
        return getSql();
    }


    /**
     * JoinOn操作
     *
     * @param joinOns JoinOn操作列表
     * @return SQL对象
     */
    @Override
    public T joinOn(List<JoinOn> joinOns) {
        getJoinOn().addItems(joinOns);
        return getSql();
    }

    /**
     * `JOIN table t1 ON t1.column1=t0.column2, t1.column3=t0.column4`语句
     *
     * @param table       需要join的表对象
     * @param columnPairs on相关的"列-对"
     * @return SQL对象
     * @see Column
     */
    @Override
    public T joinOn(Table table, List<Column> columnPairs) {
        joinOn(JoinOn.none(table, columnPairs));
        return getSql();
    }

    /**
     * `LEFT JOIN table t1 ON t1.column1=t0.column2, t1.column3=t0.column4`语句
     *
     * @param table       需要join的表对象
     * @param columnPairs DbColumn pairs
     * @return SQL对象
     * @see Column
     */
    @Override
    public T leftJoinOn(Table table, List<Column> columnPairs) {
        joinOn(JoinOn.left(table, columnPairs));
        return getSql();
    }

    /**
     * `RIGHT JOIN table t1 ON t1.column1=t0.column2, t1.column3=t0.column4`语句
     *
     * @param table       需要join的表对象
     * @param columnPairs DbColumn pairs
     * @return SQL对象
     * @see Column
     */
    @Override
    public T rightJoinOn(Table table, List<Column> columnPairs) {
        joinOn(JoinOn.right(table, columnPairs));
        return getSql();
    }

    /**
     * `FULL JOIN table t1 ON t1.column1=t0.column2, t1.column3=t0.column4`语句
     *
     * @param table       需要join的表对象
     * @param columnPairs DbColumn pairs
     * @return SQL对象
     * @see Column
     */
    @Override
    public T fullJoinOn(Table table, List<Column> columnPairs) {
        joinOn(JoinOn.full(table, columnPairs));
        return getSql();
    }

    /**
     * `INNER JOIN table t1 ON t1.column1=t0.column2, t1.column3=t0.column4`语句
     *
     * @param table       需要join的表对象
     * @param columnPairs DbColumn pairs
     * @return SQL对象
     * @see Column
     */
    @Override
    public T innerJoinOn(Table table, List<Column> columnPairs) {
        joinOn(JoinOn.inner(table, columnPairs));
        return getSql();
    }


    /**
     * `LEFT JOIN table t1 on t1.column1=t0.column2, t1.column3=t0.column4 AND t1.column5=?`语句
     *
     * @param table         需要join的表对象
     * @param criteriaItems 条件
     * @return SQL对象
     */
    @Override
    public T complexLeftJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        joinOn(JoinOn.left(table, null, criteriaItems));
        return getSql();
    }

    /**
     * `RIGHT JOIN table t1 on t1.column1=t0.column2, t1.column3=t0.column4 AND t1.column5=?`语句
     *
     * @param table         需要join的表对象
     * @param criteriaItems 条件
     * @return SQL对象
     */
    @Override
    public T complexRightJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        joinOn(JoinOn.right(table, null, criteriaItems));
        return getSql();
    }

    /**
     * `FULL JOIN table t1 on t1.column1=t0.column2, t1.column3=t0.column4 AND t1.column5=?`语句
     *
     * @param table         需要join的表对象
     * @param criteriaItems 条件
     * @return SQL对象
     */
    @Override
    public T complexFullJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        joinOn(JoinOn.full(table, null, criteriaItems));
        return getSql();
    }

    /**
     * `INNER JOIN table t1 on t1.column1=t0.column2, t1.column3=t0.column4 AND t1.column5=?`语句
     *
     * @param table         需要join的表对象
     * @param criteriaItems 条件
     * @return SQL对象
     */
    @Override
    public T complexInnerJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        joinOn(JoinOn.inner(table, null, criteriaItems));
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getJoinOnPrepareStatement(boolean useAlias) {
        List<JoinOn> joinOns = getJoinOn().getItems();
        if (CollectionUtils.isEmpty(joinOns)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement(" ", joinOns, " ", useAlias);
    }
}
