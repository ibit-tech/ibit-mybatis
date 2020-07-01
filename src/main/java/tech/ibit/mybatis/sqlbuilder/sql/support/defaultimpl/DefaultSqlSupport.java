package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

/**
 * DefaultSqlSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultSqlSupport<T> {

    /**
     * 返回sql
     *
     * @return sql
     */
    T getSql();

}
