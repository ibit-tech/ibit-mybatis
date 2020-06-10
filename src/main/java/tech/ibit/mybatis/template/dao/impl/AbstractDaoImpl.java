package tech.ibit.mybatis.template.dao.impl;


import tech.ibit.mybatis.MapperDaoUtils;
import tech.ibit.mybatis.template.mapper.Mapper;

/**
 * 抽象DAO实现类
 *
 * @author IBIT程序猿
 */
abstract class AbstractDaoImpl<P> {

    /**
     * 获取Mapper
     *
     * @return Mapper对象
     */
    public abstract Mapper<P> getMapper();

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    public abstract Class<P> getPoClazz();

    /**
     * 插入实体
     *
     * @param po 实体
     * @return 插入条数（0：失败，1：成功）
     */
    public int insert(P po) {
        return MapperDaoUtils.insert(getMapper(), po);
    }
}
