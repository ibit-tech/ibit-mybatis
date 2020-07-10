package tech.ibit.mybatis;

import tech.ibit.mybatis.utils.MapperUtils;

/**
 * 基础 Mapper
 *
 * @param <T> 实体类型
 * @author IBIT程序猿
 */
public interface Mapper<T> extends RawMapper<T> {

    /**
     * 插入对象
     *
     * @param po 插入对象
     * @return 插入条数
     */
    default int insert(T po) {
        return MapperUtils.insert(this, po);
    }

}
