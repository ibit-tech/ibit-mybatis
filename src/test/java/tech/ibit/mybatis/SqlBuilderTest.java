package tech.ibit.mybatis;

import org.junit.Test;
import tech.ibit.mybatis.demo.CommonTest;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.mybatis.template.provider.SqlBuilder;
import tech.ibit.sqlbuilder.PrepareStatement;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * SQLBuilder测试用例
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
public class SqlBuilderTest extends CommonTest {

    private SqlBuilder sqlBuilder = new SqlBuilder();

    @Test
    public void execute() {

        PrepareStatement sqlParams = new PrepareStatement(
                "SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(?, ?, ?)",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                )
        );
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlBuilder.PARAM_SQL_PARAMS, sqlParams);
        String sql = sqlBuilder.execute(paramMap);
        assertEquals("SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.user_id in(#{sqlParams0}, #{sqlParams1}, #{sqlParams2})", sql);
        assertEquals(1, paramMap.get("sqlParams0"));
        assertEquals(2, paramMap.get("sqlParams1"));
        assertEquals(3, paramMap.get("sqlParams2"));

    }

    @Test
    public void execute2() {
        // 测试设置枚举类型
        SqlBuilder.setValueFormatter(new LinkedHashMap<Class, Function<Object, Object>>() {{
            put(tech.ibit.mybatis.type.CommonEnum.class, o -> ((tech.ibit.mybatis.type.CommonEnum) o).getValue());
        }});

        PrepareStatement sqlParams = new PrepareStatement(
                "SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.type = ?",
                Collections.singletonList(
                        UserProperties.type.value(null)
                )
        );
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlBuilder.PARAM_SQL_PARAMS, sqlParams);
        String sql = sqlBuilder.execute(paramMap);
        assertEquals("SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.type = #{sqlParams0}", sql);
        assertNull(paramMap.get("sqlParams0"));


        paramMap.clear();
        sqlParams = new PrepareStatement(
                "SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.type = ?",
                Collections.singletonList(
                        UserProperties.type.value(UserType.u1)
                )
        );
        paramMap = new HashMap<>();
        paramMap.put(SqlBuilder.PARAM_SQL_PARAMS, sqlParams);
        sql = sqlBuilder.execute(paramMap);
        assertEquals("SELECT u.user_id, u.name, u.email, u.type FROM user u WHERE u.type = #{sqlParams0}", sql);
        assertEquals(1, paramMap.get("sqlParams0"));
    }
}