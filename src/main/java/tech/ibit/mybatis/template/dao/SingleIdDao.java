package tech.ibit.mybatis.template.dao;

import tech.ibit.sqlbuilder.Column;

import java.util.Collection;
import java.util.List;

/**
 * 单一主键的表的DAO
 *
 * @param <P> 实体类型
 * @param <K> 主键类型
 * @author IBIT程序猿
 */
public interface SingleIdDao<P, K> {

    /**
     * 插入记录
     *
     * @param po 插入对象
     * @return 插入条数
     */
    int insert(P po);

    /**
     * 通过主键删除记录
     *
     * @param id 主键
     * @return 删除条数
     */
    int deleteById(K id);

    /**
     * 通过主键批量删除记录
     *
     * @param ids 主键列表
     * @return 删除条数
     */
    int deleteByIds(Collection<K> ids);

    /**
     * 通过主键更新
     *
     * @param po 更新对象
     * @return 更新条数
     */
    int updateById(P po);

    /**
     * 通过主键更新指定的列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @return 更新条数
     */
    int updateById(P po, List<Column> columns);

    /**
     * 通过主键批量更新
     *
     * @param po  更新对象
     * @param ids 主键列表
     * @return 更新条数
     */
    int updateByIds(P po, Collection<K> ids);

    /**
     * 通过主键批量更新指定列
     *
     * @param po      更新对象
     * @param columns 更新列
     * @param ids     主键列表
     * @return 更新条数
     */
    int updateByIds(P po, List<Column> columns, Collection<K> ids);

    /**
     * 通过主键获取
     *
     * @param id 主键
     * @return 相应的对象
     */
    P getById(K id);

    /**
     * 通过主键批量获取
     *
     * @param ids 主键列表
     * @return 相应的对象列表
     */
    List<P> getByIds(Collection<K> ids);
}
