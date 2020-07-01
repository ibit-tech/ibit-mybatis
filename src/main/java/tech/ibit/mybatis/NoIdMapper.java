package tech.ibit.mybatis;

import tech.ibit.mybatis.utils.MapperUtils;

/**
 * 无主键的表的 Mapper
 *
 * @author IBIT程序猿
 */
public interface NoIdMapper<T> extends RawMapper<T> {

    /**
     * 插入对象
     *
     * @param po 插入对象
     * @return 插入条数
     */
    default int insert(T po) {
        return MapperUtils.insert(this, po);
    }

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    default Class<T> getPoClazz() {
        return null;
    }

}
