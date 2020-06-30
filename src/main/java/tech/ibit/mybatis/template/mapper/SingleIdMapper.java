package tech.ibit.mybatis.template.mapper;

import tech.ibit.mybatis.MapperDaoUtils;
import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.exception.SqlException;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 单一主键的表的DAO
 *
 * @param <P> 实体类型
 * @param <K> 主键类型
 * @author IBIT程序猿
 */
public interface SingleIdMapper<P, K> extends NoIdMapper<P> {

    /**
     * 通过主键删除记录
     *
     * @param id 主键
     * @return 删除条数
     */
    default int deleteById(K id) {
        return null == id ? 0 : MapperDaoUtils.deleteById(this, getPoClazz(), id);
    }

    /**
     * 通过主键批量删除记录
     *
     * @param ids 主键列表
     * @return 删除条数
     */
    default int deleteByIds(Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.deleteByIds(this, getPoClazz(), ids);
    }

    /**
     * 通过主键更新
     *
     * @param po 更新对象
     * @return 更新条数
     */
    default int updateById(P po) {
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
    default int updateByIdWithColumns(P po, List<Column> columns) {
        return MapperDaoUtils.updateById(this, po, columns);
    }

    /**
     * 通过主键批量更新
     *
     * @param po  更新对象
     * @param ids 主键列表
     * @return 更新条数
     */
    default int updateByIds(P po, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByIds(this, po, ids);
    }

    /**
     * 通过主键批量更新指定列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @param ids     主键列表
     * @return 更新条数
     */
    default int updateByIdsWithColumns(P po, List<Column> columns, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : MapperDaoUtils.updateByIds(this, po, columns, ids);
    }

    /**
     * 通过主键获取
     *
     * @param id 主键
     * @return 相应的对象
     */
    default P getById(K id) {
        return null == id ? null : MapperDaoUtils.getById(this, getPoClazz(), id);
    }

    /**
     * 通过主键批量获取
     *
     * @param ids 主键列表
     * @return 相应的对象列表
     */
    default List<P> getByIds(Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : MapperDaoUtils.getByIds(this, getPoClazz(), ids);
    }
}
