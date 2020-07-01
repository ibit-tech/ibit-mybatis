package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;

/**
 * SqlSupport
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface SqlSupport<T> {

    /**
     * 返回sql
     *
     * @return sql
     */
    T getSql();

    /**
     * 获取预查询SQL
     *
     * @return 预查询SQL
     */
    PrepareStatement getPrepareStatement();
}
