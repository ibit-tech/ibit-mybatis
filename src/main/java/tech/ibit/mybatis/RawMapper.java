package tech.ibit.mybatis;

import org.apache.ibatis.annotations.*;
import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.exception.OperationNotSupportedException;
import tech.ibit.mybatis.sqlbuilder.sql.*;
import tech.ibit.mybatis.sqlbuilder.sql.impl.*;

import java.util.List;

/**
 * 抽象Mapper接口（定义基础增、删、改、查）
 *
 * @param <T> 实体类型
 * @author IBIT程序猿
 */
public interface RawMapper<T> {

    /**
     * 查询列表
     *
     * @param sqlParams SQL语句-参数对象
     * @return 结果对象列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    List<T> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 查询单个对象
     *
     * @param sqlParams SQL语句-参数对象
     * @return 结果对象
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    T rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 基本类型查询
     *
     * @param sqlParams SQL语句-参数对象
     * @param <V>       基本类型
     * @return 结果列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    <V> List<V> rawSelectDefault(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 计数
     *
     * @param sqlParams SQL语句-参数对象
     * @return 计数结果
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int rawCount(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 更新
     *
     * @param sqlParams SQL语句-参数对象
     * @return 更新条数
     */
    @UpdateProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 插入并生成主键
     *
     * @param sqlParams SQL语句-参数对象
     * @param key       接受主键的生成结果的key对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    @Options(useGeneratedKeys = true, keyProperty = SqlProvider.PARAM_KEY_VALUE)
    int rawInsertWithGenerateKeys(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
                                  @Param(SqlProvider.PARAM_KEY) KeyValuePair key);

    /**
     * 插入
     *
     * @param sqlParams SQL语句-参数对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int rawInsert(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

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

}
