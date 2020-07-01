package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.JoinOnSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.List;

/**
 * DefaultJoinOnSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultJoinOnSupport<T> extends DefaultSqlSupport<T>,
        JoinOnSupport<T>, DefaultPrepareStatementSupport {


    /**
     * Join on
     *
     * @return JoinOn
     */
    ListField<JoinOn> getJoinOn();

    /**
     * JoinOn操作
     *
     * @param joinOn JoinOn操作
     * @return SQL对象
     */
    @Override
    default T joinOn(JoinOn joinOn) {
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
    default T joinOn(List<JoinOn> joinOns) {
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
    default T joinOn(Table table, List<Column> columnPairs) {
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
    default T leftJoinOn(Table table, List<Column> columnPairs) {
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
    default T rightJoinOn(Table table, List<Column> columnPairs) {
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
    default T fullJoinOn(Table table, List<Column> columnPairs) {
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
    default T innerJoinOn(Table table, List<Column> columnPairs) {
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
    default T complexLeftJoinOn(Table table, List<CriteriaItem> criteriaItems) {
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
    default T complexRightJoinOn(Table table, List<CriteriaItem> criteriaItems) {
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
    default T complexFullJoinOn(Table table, List<CriteriaItem> criteriaItems) {
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
    default T complexInnerJoinOn(Table table, List<CriteriaItem> criteriaItems) {
        joinOn(JoinOn.inner(table, null, criteriaItems));
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getJoinOnPrepareStatement(boolean useAlias) {
        List<JoinOn> joinOns = getJoinOn().getItems();
        if (CollectionUtils.isEmpty(joinOns)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement(" ", joinOns, " ", useAlias);
    }
}
