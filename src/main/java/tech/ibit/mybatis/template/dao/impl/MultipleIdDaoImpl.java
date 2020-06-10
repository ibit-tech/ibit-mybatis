package tech.ibit.mybatis.template.dao.impl;


import tech.ibit.mybatis.MapperDaoUtils;
import tech.ibit.mybatis.template.dao.MultipleIdDao;
import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.MultiId;
import tech.ibit.sqlbuilder.exception.ColumnValueNotFoundException;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 多个主键的表的DAO实现
 *
 * @param <P> 实体类型
 * @param <K> 主键类型
 * @author IBIT程序猿
 */
public abstract class MultipleIdDaoImpl<P, K extends MultiId> extends AbstractDaoImpl<P> implements MultipleIdDao<P, K> {


    /**
     * 通过主键删除记录
     *
     * @param id 主键
     * @return 删除条数
     */
    @Override
    public int deleteById(K id) {
        return null == id ? 0 : MapperDaoUtils.deleteByMultiId(getMapper(), id);
    }

    /**
     * 通过主键批量删除记录
     *
     * @param ids 主键列表
     * @return 删除条数
     */
    @Override
    public int deleteByIds(List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.deleteByMultiIds(getMapper(), ids);
    }

    /**
     * 通过主键更新
     *
     * @param po 更新对象
     * @return 更新条数
     */
    @Override
    public int updateById(P po) {
        try {
            return MapperDaoUtils.updateById(getMapper(), po);
        } catch (ColumnValueNotFoundException e) {
            return 0;
        }
    }

    /**
     * 通过主键更新指定的列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @return 更新条数
     */
    @Override
    public int updateById(P po, List<Column> columns) {
        return MapperDaoUtils.updateById(getMapper(), po, columns);
    }

    /**
     * 通过主键批量更新
     *
     * @param po  更新对象
     * @param ids 主键列表
     * @return 更新条数
     */
    @Override
    public int updateByIds(P po, List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByMultiIds(getMapper(), po, ids);
    }

    /**
     * 通过主键批量更新指定列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @param ids     主键列表
     * @return 更新条数
     */
    @Override
    public int updateByIds(P po, List<Column> columns, List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByMultiIds(getMapper(), po, columns, ids);
    }

    /**
     * 通过主键获取
     *
     * @param id 主键
     * @return 相应的对象
     */
    @Override
    public P getById(K id) {
        return null == id ? null : MapperDaoUtils.getByMultiId(getMapper(), getPoClazz(), id);
    }

    /**
     * 通过主键批量获取
     *
     * @param ids 主键列表
     * @return 相应的对象列表
     */
    @Override
    public List<P> getByIds(List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : MapperDaoUtils.getByMultiIds(getMapper(), getPoClazz(), ids);
    }
}
