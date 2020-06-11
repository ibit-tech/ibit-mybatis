package tech.ibit.mybatis;

import tech.ibit.mybatis.template.mapper.Mapper;
import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.PrepareStatement;

import java.util.List;

/**
 * 测试的mapper
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
public class TestMapper<T> implements Mapper<T> {
    @Override
    public <P> List<P> selectWithResultMap(PrepareStatement sqlParams, String resultMap) {
        return null;
    }

    @Override
    public List<T> select(PrepareStatement sqlParams) {
        return null;
    }

    @Override
    public int count(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public int update(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public int insertWithGenerateKeys(PrepareStatement sqlParams, KeyValuePair key) {
        return 0;
    }

    @Override
    public int insert(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public <P> List<P> selectDefault(PrepareStatement sqlParams) {
        return null;
    }
}
