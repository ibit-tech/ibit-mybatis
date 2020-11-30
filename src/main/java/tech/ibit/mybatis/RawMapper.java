package tech.ibit.mybatis;

import org.apache.ibatis.annotations.*;
import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;

import java.util.List;

/**
 * 抽象Mapper接口（定义基础增、删、改、查）
 *
 * @param <T> 实体类型
 * @author iBit程序猿
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
     * 通过类型查询列表
     *
     * @param sqlParams  SQL语句-参数对象
     * @param resultType 返回类型类
     * @param <V>        基本类型
     * @return 结果列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    <V> List<V> rawSelectWithType(
            @Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
            @Param(SqlProvider.PARAM_KEY_RESULT_TYPE) Class<V> resultType
    );

    /**
     * 通过类型查询单个对象
     *
     * @param sqlParams  SQL语句-参数对象
     * @param resultType 返回类型类
     * @param <V>        基本类型
     * @return 结果列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    <V> V rawSelectOneWithType(
            @Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
            @Param(SqlProvider.PARAM_KEY_RESULT_TYPE) Class<V> resultType
    );

    /**
     * 计数
     *
     * @param sqlParams SQL语句-参数对象
     * @return 计数结果
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    Integer rawCount(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 更新
     *
     * @param sqlParams SQL语句-参数对象
     * @return 更新条数
     */
    @UpdateProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    Integer rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 插入并生成主键
     *
     * @param sqlParams SQL语句-参数对象
     * @param key       接受主键的生成结果的key对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    @Options(useGeneratedKeys = true, keyProperty = SqlProvider.PARAM_KEY_VALUE)
    Integer rawInsertWithGenerateKeys(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
                                      @Param(SqlProvider.PARAM_KEY) KeyValuePair key);

    /**
     * 插入
     *
     * @param sqlParams SQL语句-参数对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    Integer rawInsert(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

}
