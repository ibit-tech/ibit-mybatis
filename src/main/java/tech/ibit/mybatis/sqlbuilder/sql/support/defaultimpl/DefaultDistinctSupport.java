package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.sql.field.BooleanField;
import tech.ibit.mybatis.sqlbuilder.sql.support.DistinctSupport;

/**
 * DefaultDistinctSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultDistinctSupport<T> extends DefaultSqlSupport<T>,
        DistinctSupport<T> {

    /**
     * 获取distinct
     *
     * @return distinct
     */
    BooleanField getDistinct();

    /**
     * distinct 操作
     *
     * @return SQL对象
     */
    @Override
    default T distinct() {
        return distinct(true);
    }


    /**
     * distinct 操作
     *
     * @param distinct 是否distinct
     * @return SQL对象
     */
    @Override
    default T distinct(boolean distinct) {
        getDistinct().setValue(distinct);
        return getSql();
    }
}
