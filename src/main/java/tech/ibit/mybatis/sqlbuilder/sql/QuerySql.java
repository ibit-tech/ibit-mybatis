package tech.ibit.mybatis.sqlbuilder.sql;

import tech.ibit.mybatis.sqlbuilder.sql.support.*;

import java.util.List;

/**
 * 定义搜索接口
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface QuerySql<T> extends ColumnSupport<QuerySql<T>>,
        DistinctSupport<QuerySql<T>>,
        FromSupport<QuerySql<T>>,
        GroupBySupport<QuerySql<T>>,
        HavingSupport<QuerySql<T>>,
        JoinOnSupport<QuerySql<T>>,
        LimitSupport<QuerySql<T>>,
        OrderBySupport<QuerySql<T>>,
        WhereSupport<QuerySql<T>>,
        PrepareStatementSupport {

    /**
     * 转换为 CountSql
     *
     * @return CountSql对象
     */
    CountSql toCountSql();

    /**
     * 查询（包含分页信息）
     *
     * @return 查询结果
     */
    Page<T> executeQueryPage();

    /**
     * 查询（包含分页信息）
     *
     * @param clazz 返回类型
     * @param <P>   返回类型模板
     * @return 查询结果
     */
    <P> Page<P> executeQueryPage(Class<P> clazz);

    /**
     * 查询
     *
     * @return 结果列表
     */
    List<T> executeQuery();

    /**
     * 查询
     *
     * @param clazz 返回类型
     * @param <P>   返回类型模板
     * @return 结果列表
     */
    <P> List<P> executeQuery(Class<P> clazz);

    /**
     * 查询单个对象
     *
     * @return 结果对象
     */
    T executeQueryOne();

    /**
     * 查询单个对象
     *
     * @param clazz 返回类型
     * @param <P>   返回类型模板
     * @return 结果对象
     */
    <P> P executeQueryOne(Class<P> clazz);

    /**
     * 查询基本类型（包含分页信息）
     *
     * @param <V> 基本类型
     * @return 结果列表
     */
    <V> Page<V> executeQueryDefaultPage();

    /**
     * 查询基本类型
     *
     * @param <V> 基本类型
     * @return 结果列表
     */
    <V> List<V> executeQueryDefault();

}
