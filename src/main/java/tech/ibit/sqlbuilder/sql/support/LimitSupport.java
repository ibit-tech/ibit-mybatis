package tech.ibit.sqlbuilder.sql.support;

import tech.ibit.sqlbuilder.sql.field.LimitField;

/**
 * Limit Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface LimitSupport<T> extends SqlSupport<T> {

    /**
     * 获取limit相关参数
     *
     * @return limit相关参数
     */
    LimitField getLimit();

    /**
     * `LIMIT #{start}, #{limit}` 语句
     *
     * @param start 开始位置
     * @param limit 限制条数
     * @return SQL对象
     */
    default T limit(int start, int limit) {
        getLimit().limit(start, limit);
        return getSql();
    }

    /**
     * `LIMIT 0, #{limit}` 语句
     *
     * @param limit 限制条数
     * @return SQL对象
     */
    default T limit(int limit) {
        getLimit().limit(limit);
        return getSql();
    }

}
