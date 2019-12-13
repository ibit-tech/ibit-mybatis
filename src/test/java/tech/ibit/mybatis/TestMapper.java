package tech.ibit.mybatis;

import tech.ibit.mybatis.template.mapper.Mapper;
import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.SqlParams;

import java.util.List;

/**
 * 测试的mapper
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
public class TestMapper<T> implements Mapper<T> {
    @Override
    public <P> List<P> selectWithResultMap(SqlParams sqlParams, String resultMap) {
        return null;
    }

    @Override
    public List<T> select(SqlParams sqlParams) {
        return null;
    }

    @Override
    public int count(SqlParams sqlParams) {
        return 0;
    }

    @Override
    public int update(SqlParams sqlParams) {
        return 0;
    }

    @Override
    public int insertWithGenerateKeys(SqlParams sqlParams, KeyValuePair key) {
        return 0;
    }

    @Override
    public int insert(SqlParams sqlParams) {
        return 0;
    }

    @Override
    public <P> List<P> selectDefault(SqlParams sqlParams) {
        return null;
    }
}
