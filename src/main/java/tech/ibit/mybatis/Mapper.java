package tech.ibit.mybatis;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.UniqueKey;
import tech.ibit.mybatis.sqlbuilder.exception.OperationNotSupportedException;
import tech.ibit.mybatis.sqlbuilder.sql.*;
import tech.ibit.mybatis.sqlbuilder.sql.impl.*;
import tech.ibit.mybatis.utils.MapperUtils;

import java.util.Collection;
import java.util.List;

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

    /**
     * 创建搜索
     *
     * @return 搜索sql
     */
    default QuerySql<T> createQuery() {
        return new QuerySqlImpl<>(this);
    }

    /**
     * 创建计数
     *
     * @return 计数sql
     */
    default CountSql createCount() {
        return new CountSqlImpl(this);
    }

    /**
     * 创建删除
     *
     * @return 删除sql
     */
    default DeleteSql createDelete() {
        return new DeleteSqlImpl(this);
    }

    /**
     * 创建插入
     *
     * @return 插入sql
     */
    default InsertSql createInsert() {
        return new InsertSqlImpl(this);
    }

    /**
     * 创建更新
     *
     * @return 更新sql
     */
    default UpdateSql createUpdate() {
        return new UpdateSqlImpl(this);
    }

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    default Class<T> getPoClazz() {
        throw new OperationNotSupportedException("Method getPoClazz needs to override!");
    }

    /**
     * 获取默认的表对象
     *
     * @return 表对象
     */
    default Table getDefaultTable() {
        throw new OperationNotSupportedException("Method getDefaultTable needs to override!");
    }

    /**
     * 通过 unique key 删除记录
     *
     * @param uniqueKey unique key
     * @return 删除条数
     */
    default int deleteByUniqueKey(UniqueKey uniqueKey) {
        return MapperUtils.deleteByUniqueKey(this, uniqueKey);
    }

    /**
     * 通过 unique keys 批量删除记录
     *
     * @param uniqueKeys unique key 列表
     * @return 删除条数
     */
    default int deleteByUniqueKeys(Collection<UniqueKey> uniqueKeys) {
        return MapperUtils.deleteByUniqueKeys(this, uniqueKeys);
    }

    /**
     * 通过 unique key 更新
     *
     * @param po        更新对象
     * @param uniqueKey unique key
     * @return 更新条数
     */
    default int updateByUniqueKey(T po, UniqueKey uniqueKey) {
        return MapperUtils.updateByUniqueKey(this, po, uniqueKey);
    }

    /**
     * 通过主键更新指定的列
     *
     * @param po        更新对象
     * @param columns   更新列
     * @param uniqueKey unique key
     * @return 更新条数
     */
    default int updateByUniqueKeyWithColumns(T po, List<Column> columns, UniqueKey uniqueKey) {
        return MapperUtils.updateByUniqueKey(this, po, columns, uniqueKey);
    }

    /**
     * 通过主键批量更新
     *
     * @param po         更新对象
     * @param uniqueKeys unique key 列表
     * @return 更新条数
     */
    default int updateByUniqueKeys(T po, Collection<UniqueKey> uniqueKeys) {
        return MapperUtils.updateByUniqueKeys(this, po, uniqueKeys);
    }

    /**
     * 通过主键批量更新指定列
     *
     * @param po         更新对象
     * @param columns    更新列
     * @param uniqueKeys 主键列表
     * @return 更新条数
     */
    default int updateByUniqueKeysWithColumns(T po, List<Column> columns, Collection<UniqueKey> uniqueKeys) {
        return MapperUtils.updateByUniqueKeys(this, po, columns, uniqueKeys);
    }

    /**
     * 通过 unique key 获取
     *
     * @param uniqueKey unique key
     * @return 相应的对象
     */
    default T getByUniqueKey(UniqueKey uniqueKey) {
        return MapperUtils.getByUniqueKey(this, uniqueKey);
    }

    /**
     * 通过 unique key 批量获取
     *
     * @param uniqueKeys unique key 列表
     * @return 相应的对象列表
     */
    default List<T> getByUniqueKeys(Collection<UniqueKey> uniqueKeys) {
        return MapperUtils.getByUniqueKeys(this, uniqueKeys);
    }

    /**
     * 通过 unique key 获取
     *
     * @param uniqueKey unique key
     * @param poClazz   返回类
     * @param <P>       返回类型
     * @return 相应的对象
     */
    default <P> P getPoByUniqueKey(Class<P> poClazz, UniqueKey uniqueKey) {
        return MapperUtils.getPoByUniqueKey(this, poClazz, uniqueKey);
    }

    /**
     * 通过 unique key 批量获取
     *
     * @param uniqueKeys unique key 列表
     * @param poClazz    返回类
     * @param <P>        返回类型
     * @return 相应的对象列表
     */
    default <P> List<P> getPoByUniqueKeys(Class<P> poClazz, Collection<UniqueKey> uniqueKeys) {
        return MapperUtils.getPoByUniqueKeys(this, poClazz, uniqueKeys);
    }

}
