package tech.ibit.mybatis;

import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;

import java.util.List;

/**
 * 测试的mapper
 *
 * @author iBit程序猿
 * mailto: ibit_tech@aliyun.com
 */
public class TestMapper<T> implements RawMapper<T> {

    @Override
    public List<T> rawSelect(PrepareStatement sqlParams) {
        return null;
    }

    @Override
    public T rawSelectOne(PrepareStatement sqlParams) {
        return null;
    }

    @Override
    public Integer rawCount(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public Integer rawUpdate(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public Integer rawInsertWithGenerateKeys(PrepareStatement sqlParams, KeyValuePair key) {
        return 0;
    }

    @Override
    public Integer rawInsert(PrepareStatement sqlParams) {
        return 0;
    }

    @Override
    public <P> List<P> rawSelectDefault(PrepareStatement sqlParams) {
        return null;
    }

    @Override
    public <V> List<V> rawSelectWithType(PrepareStatement sqlParams, Class<V> resultType) {
        return null;
    }

    @Override
    public <V> V rawSelectOneWithType(PrepareStatement sqlParams, Class<V> resultType) {
        return null;
    }
}
