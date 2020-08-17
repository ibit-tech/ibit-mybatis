package tech.ibit.mybatis;

import com.alibaba.fastjson.JSON;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SimpleNameColumn;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 公共测试类
 *
 * @author IBIT程序猿
 */
public class CommonTest {

    /**
     * 断言参数相等
     *
     * @param sql       sql
     * @param values    值
     * @param sqlParams sql参数对象
     */
    public void assertPrepareStatementEquals(String sql, List<ColumnValue> values, PrepareStatement sqlParams) {
        assertEquals(new PrepareStatement(sql, values).toString(), sqlParams.toString());
    }


    /**
     * 断言参数相等
     *
     * @param sql       sql
     * @param sqlParams sql参数对象
     */
    public void assertPrepareStatementEquals(String sql, PrepareStatement sqlParams) {
        assertEquals(new PrepareStatement(sql, Collections.emptyList()).toString(), sqlParams.toString());
    }

    /**
     * 判读对象是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     */
    public void assetObjectEquals(Object o1, Object o2) {
        assertEquals(JSON.toJSONString(o1), JSON.toJSONString(o2));
    }

    /**
     * 获取limit列
     *
     * @return limit列
     */
    public SimpleNameColumn getLimitColumn() {
        return new SimpleNameColumn("$limit");
    }

    /**
     * 获取start列
     *
     * @return start列
     */
    public SimpleNameColumn getStartColumn() {
        return new SimpleNameColumn("$start");
    }

}
