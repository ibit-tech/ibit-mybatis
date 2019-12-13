package tech.ibit.mybatis;

import org.junit.Test;
import tech.ibit.mybatis.test.CommonTest;
import tech.ibit.sqlbuilder.SqlParams;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * SQLBuilder测试用例
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
public class SqlBuilderTest extends CommonTest {

    private SqlBuilder sqlBuilder = new SqlBuilder();

    @Test
    public void execute() {

        SqlParams sqlParams = new SqlParams(
                "SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(?, ?, ?)",
                getKeyValuePairs("u.user_id", 1, "u.user_id", 2, "u.user_id", 3)
        );
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlBuilder.SQL_PARAMS, sqlParams);
        String sql = sqlBuilder.execute(paramMap);
        assertEquals("SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(#{sqlParams0}, #{sqlParams1}, #{sqlParams2})", sql);
        assertEquals(1, paramMap.get("sqlParams0"));
        assertEquals(2, paramMap.get("sqlParams1"));
        assertEquals(3, paramMap.get("sqlParams2"));


        paramMap.clear();
        sqlParams = new SqlParams(
                "SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(?, ?, ?)",
                getKeyValuePairs(null, 1, null, 2, null, 3)
        );
        paramMap = new HashMap<>();
        paramMap.put(SqlBuilder.SQL_PARAMS, sqlParams);
        sql = sqlBuilder.execute(paramMap);
        assertEquals("SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(#{sqlParams0}, #{sqlParams1}, #{sqlParams2})", sql);
        assertEquals(1, paramMap.get("sqlParams0"));
        assertEquals(2, paramMap.get("sqlParams1"));
        assertEquals(3, paramMap.get("sqlParams2"));

    }
}