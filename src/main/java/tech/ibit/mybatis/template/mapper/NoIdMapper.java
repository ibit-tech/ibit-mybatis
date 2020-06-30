package tech.ibit.mybatis.template.mapper;

import tech.ibit.mybatis.MapperDaoUtils;

/**
 * Insert Mapper
 *
 * @author IBIT程序猿
 */
public interface NoIdMapper<P> extends RawMapper<P> {

    /**
     * 插入对象
     *
     * @param po 插入对象
     * @return 插入条数
     */
    default int insert(P po) {
        return MapperDaoUtils.insert(this, po);
    }

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    default Class<P> getPoClazz() {
        return null;
    }

}
