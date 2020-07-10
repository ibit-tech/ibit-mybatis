package tech.ibit.mybatis;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.exception.OperationNotSupportedException;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.utils.MapperUtils;

import java.util.Collection;
import java.util.List;

/**
 * 单一主键的表的 Mapper
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author IBIT程序猿
 */
public interface SingleIdMapper<T, K> extends Mapper<T> {

    /**
     * 获取主键列
     *
     * @return 主键列
     */
    default Column getId() {
        throw new OperationNotSupportedException("Method getId needs to override!");
    }

    /**
     * 通过主键删除记录
     *
     * @param id 主键
     * @return 删除条数
     */
    default int deleteById(K id) {
        return MapperUtils.deleteById(this, id);
    }

    /**
     * 通过主键批量删除记录
     *
     * @param ids 主键列表
     * @return 删除条数
     */
    default int deleteByIds(Collection<K> ids) {
        return MapperUtils.deleteByIds(this, ids);
    }

    /**
     * 通过主键更新
     *
     * @param po 更新对象
     * @return 更新条数
     */
    default int updateById(T po) {
        try {
            return MapperUtils.updateById(this, po);
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
        return MapperUtils.updateById(this, po, columns);
    }

    /**
     * 通过主键批量更新
     *
     * @param po  更新对象
     * @param ids 主键列表
     * @return 更新条数
     */
    default int updateByIds(T po, Collection<K> ids) {
        return MapperUtils.updateByIds(this, po, ids);
    }

    /**
     * 通过主键批量更新指定列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @param ids     主键列表
     * @return 更新条数
     */
    default int updateByIdsWithColumns(T po, List<Column> columns, Collection<K> ids) {
        return MapperUtils.updateByIds(this, po, columns, ids);
    }

    /**
     * 通过主键获取
     *
     * @param id 主键
     * @return 相应的对象
     */
    default T getById(K id) {
        return MapperUtils.getById(this, id);
    }

    /**
     * 通过主键批量获取
     *
     * @param ids 主键列表
     * @return 相应的对象列表
     */
    default List<T> getByIds(Collection<K> ids) {
        return MapperUtils.getByIds(this, ids);
    }
}
