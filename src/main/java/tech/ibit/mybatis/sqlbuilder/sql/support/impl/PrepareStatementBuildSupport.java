package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.PrepareStatementSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 条件支持
 *
 * @author iBit程序猿
 * @version 2.0
 */
public interface PrepareStatementBuildSupport {


    /**
     * 获取 PrepareStatement
     *
     * @param <T>              处理类型
     * @param hook             开始位置
     * @param clauses          语句列表
     * @param prepareSqlGetter prepareSql获取
     * @param valuesGetter     values获取
     * @param separator        分割符
     * @param close            结束字符
     * @return 新的PrepareStatement
     */
    default <T> PrepareStatement getPrepareStatement(String hook
            , List<T> clauses, Function<T, String> prepareSqlGetter, Function<T, List<ColumnValue>> valuesGetter
            , String separator, String close) {
        if (CollectionUtils.isEmpty(clauses)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        prepareSql.append(hook);

        for (int i = 0; i < clauses.size(); i++) {
            T clause = clauses.get(i);
            if (i != 0) {
                prepareSql.append(separator);
            }

            prepareSql.append(prepareSqlGetter.apply(clause));
            if (null != valuesGetter) {
                List<ColumnValue> cvs = valuesGetter.apply(clause);
                if (CollectionUtils.isNotEmpty(cvs)) {
                    values.addAll(cvs);
                }
            }
        }

        prepareSql.append(close);

        return new PrepareStatement(prepareSql.toString(), values);
    }


    /**
     * 获取 PrepareStatement
     *
     * @param <T>              处理类型
     * @param hook             开始位置
     * @param clauses          语句列表
     * @param prepareSqlGetter prepareSql获取
     * @param valuesGetter     values获取
     * @param separator        分割符
     * @return 新的PrepareStatement
     */
    default <T> PrepareStatement getPrepareStatement(String hook
            , List<T> clauses, Function<T, String> prepareSqlGetter, Function<T, List<ColumnValue>> valuesGetter
            , String separator) {
        return getPrepareStatement(hook, clauses, prepareSqlGetter, valuesGetter, separator, "");
    }


    /**
     * 获取 PrepareStatement
     *
     * @param hook      开始位置
     * @param clauses   语句列表
     * @param separator 分割符
     * @param close     结束字符
     * @param useAlias  是否使用别名
     * @return 新的PrepareStatement
     */
    default PrepareStatement getPrepareStatement(String hook
            , List<? extends PrepareStatementSupplier> clauses, String separator, String close, boolean useAlias) {
        if (CollectionUtils.isEmpty(clauses)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        prepareSql.append(hook);

        for (int i = 0; i < clauses.size(); i++) {
            PrepareStatementSupplier clause = clauses.get(i);
            if (i != 0) {
                prepareSql.append(separator);
            }

            PrepareStatement clausePrepareStatement = clause.getPrepareStatement(useAlias);
            prepareSql.append(clausePrepareStatement.getPrepareSql());
            values.addAll(clausePrepareStatement.getValues());
        }

        prepareSql.append(close);

        return new PrepareStatement(prepareSql.toString(), values);
    }


    /**
     * 获取 PrepareStatement
     *
     * @param hook      开始位置
     * @param clauses   语句列表
     * @param separator 分割符
     * @param useAlias  是否使用别名
     * @return 新的PrepareStatement
     */
    default PrepareStatement getPrepareStatement(String hook
            , List<? extends PrepareStatementSupplier> clauses, String separator, boolean useAlias) {
        return getPrepareStatement(hook, clauses, separator, "", useAlias);
    }

    /**
     * 扩展目标sql和values
     *
     * @param prepareStatement 预查询sql对象
     * @param targetPrepareSql 目标sql
     * @param targetValues     目标值
     */
    default void append(PrepareStatement prepareStatement, StringBuilder targetPrepareSql, List<ColumnValue> targetValues) {
        targetPrepareSql.append(prepareStatement.getPrepareSql());
        targetValues.addAll(prepareStatement.getValues());
    }


    /**
     * 扩展目标sql和values
     *
     * @param prepareStatements 预查询sql对象列表
     * @param targetPrepareSql  目标sql
     * @param targetValues      目标值
     */
    default void append(List<PrepareStatement> prepareStatements, StringBuilder targetPrepareSql, List<ColumnValue> targetValues) {
        prepareStatements.forEach(prepareStatement -> append(prepareStatement, targetPrepareSql, targetValues));
    }
}
