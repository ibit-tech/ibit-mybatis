package tech.ibit.mybatis.template.mapper;


import tech.ibit.mybatis.MapperDaoUtils;
import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.MultiId;
import tech.ibit.sqlbuilder.exception.SqlException;
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
public interface MultipleIdMapper<T, K extends MultiId> extends NoIdMapper<T> {


    /**
     * 通过主键删除记录
     *
     * @param id 主键
     * @return 删除条数
     */
    default int deleteById(K id) {
        return null == id ? 0 : MapperDaoUtils.deleteByMultiId(this, id);
    }

    /**
     * 通过主键批量删除记录
     *
     * @param ids 主键列表
     * @return 删除条数
     */
    default int deleteByIds(List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.deleteByMultiIds(this, ids);
    }

    /**
     * 通过主键更新
     *
     * @param po 更新对象
     * @return 更新条数
     */
    default int updateById(T po) {
        try {
            return MapperDaoUtils.updateById(this, po);
        } catch (SqlException e) {
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
    default int updateByIdWithColumns(T po, List<Column> columns) {
        return MapperDaoUtils.updateById(this, po, columns);
    }

    /**
     * 通过主键批量更新
     *
     * @param po  更新对象
     * @param ids 主键列表
     * @return 更新条数
     */
    default int updateByIds(T po, List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByMultiIds(this, po, ids);
    }

    /**
     * 通过主键批量更新指定列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @param ids     主键列表
     * @return 更新条数
     */
    default int updateByIdsWithColumns(T po, List<Column> columns, List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByMultiIds(this, po, columns, ids);
    }

    /**
     * 通过主键获取
     *
     * @param id 主键
     * @return 相应的对象
     */
    default T getById(K id) {
        return null == id ? null : MapperDaoUtils.getByMultiId(this, getPoClazz(), id);
    }

    /**
     * 通过主键批量获取
     *
     * @param ids 主键列表
     * @return 相应的对象列表
     */
    default List<T> getByIds(List<K> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : MapperDaoUtils.getByMultiIds(this, getPoClazz(), ids);
    }
}
