package tech.ibit.mybatis.sqlbuilder.sql.support;

/**
 * DefaultSqlSupport
 *
 * @author IBIT程序猿
 */
public interface SqlSupport<T> {

    /**
     * 返回sql
     *
     * @return sql
     */
    T getSql();

}
