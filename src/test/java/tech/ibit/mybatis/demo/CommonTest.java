package tech.ibit.mybatis.demo;

import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SimpleNameColumn;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 公共测试类
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
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