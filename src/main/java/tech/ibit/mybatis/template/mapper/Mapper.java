package tech.ibit.mybatis.template.mapper;


import org.apache.ibatis.annotations.*;
import tech.ibit.mybatis.SqlBuilder;
import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.SqlParams;

import java.util.List;

/**
 * 抽象Mapper接口（定义基础增、删、改、查）
 *
 * @author IBIT TECH
 */
public interface Mapper<T> {


    /**
     * 指定ResultMap查询
     *
     * @param sqlParams SQL语句-参数对象
     * @param resultMap ResultMap名称
     * @param <P>       实体类类型
     * @return 结果对象列表
     */
    @SelectProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    <P> List<P> selectWithResultMap(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams,
                                    @Param(SqlBuilder.RESULT_MAP) String resultMap);

    /**
     * 使用XML中默认的ResultMap查询（返回系统生成的实体类）
     *
     * @param sqlParams SQL语句-参数对象
     * @return 结果对象列表
     */
    @ResultMap(SqlBuilder.RESULT_MAP)
    @SelectProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    List<T> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams);

    /**
     * 基本类型查询
     *
     * @param sqlParams SQL语句-参数对象
     * @param <P>       基本类型
     * @return 结果列表
     */
    @SelectProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    <P> List<P> selectDefault(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams);

    /**
     * 计数
     *
     * @param sqlParams SQL语句-参数对象
     * @return 计数结果
     */
    @ResultType(value = int.class)
    @SelectProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    int count(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams);

    /**
     * 更新
     *
     * @param sqlParams SQL语句-参数对象
     * @return 更新条数
     */
    @ResultType(int.class)
    @UpdateProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams);

    /**
     * 插入并生成主键
     *
     * @param sqlParams SQL语句-参数对象
     * @param key       接受主键的生成结果的key对象
     * @return 插入条数（0或1）
     */
    @ResultType(int.class)
    @InsertProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    @Options(useGeneratedKeys = true, keyProperty = SqlBuilder.KEY_VALUE)
    int insertWithGenerateKeys(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams,
                               @Param(SqlBuilder.KEY) KeyValuePair key);

    /**
     * 插入
     *
     * @param sqlParams SQL语句-参数对象
     * @return 插入条数（0或1）
     */
    @ResultType(int.class)
    @InsertProvider(type = SqlBuilder.class, method = SqlBuilder.METHOD_EXECUTE)
    int insert(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams);

}
