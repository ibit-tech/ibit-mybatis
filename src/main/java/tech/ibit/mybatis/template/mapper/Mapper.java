package tech.ibit.mybatis.template.mapper;


import org.apache.ibatis.annotations.*;
import tech.ibit.mybatis.template.provider.SqlProvider;
import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.PrepareStatement;

import java.util.List;

/**
 * 抽象Mapper接口（定义基础增、删、改、查）
 *
 * @author IBIT程序猿
 */
public interface Mapper<T> {

    /**
     * 查询列表
     *
     * @param sqlParams SQL语句-参数对象
     * @return 结果对象列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    List<T> select(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 查询单个对象
     *
     * @param sqlParams SQL语句-参数对象
     * @return 结果对象
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    T selectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 基本类型查询
     *
     * @param sqlParams SQL语句-参数对象
     * @param <P>       基本类型
     * @return 结果列表
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    <P> List<P> selectDefault(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 计数
     *
     * @param sqlParams SQL语句-参数对象
     * @return 计数结果
     */
    @SelectProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int count(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 更新
     *
     * @param sqlParams SQL语句-参数对象
     * @return 更新条数
     */
    @UpdateProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int update(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

    /**
     * 插入并生成主键
     *
     * @param sqlParams SQL语句-参数对象
     * @param key       接受主键的生成结果的key对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    @Options(useGeneratedKeys = true, keyProperty = SqlProvider.PARAM_KEY_VALUE)
    int insertWithGenerateKeys(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
                               @Param(SqlProvider.PARAM_KEY) KeyValuePair key);

    /**
     * 插入
     *
     * @param sqlParams SQL语句-参数对象
     * @return 插入条数（0或1）
     */
    @InsertProvider(type = SqlProvider.class, method = SqlProvider.METHOD_EXECUTE)
    int insert(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams);

}
