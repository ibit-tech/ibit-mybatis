package tech.ibit.mybatis;

import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.SqlParams;

import java.util.List;
import java.util.Map;

/**
 * Mybatis SQL构造器
 *
 * @author IBIT TECH
 */
public class SqlBuilder {

    public static final String SQL_PARAMS = "sqlParams";
    public static final String METHOD_EXECUTE = "execute";
    public static final String RESULT_MAP = "resultMap";
    public static final String KEY = "key";
    public static final String KEY_VALUE = "key.value";

    /**
     * 执行方法
     *
     * @param paramMap 参数map
     * @return 执行的sql
     */
    public String execute(Map<String, Object> paramMap) {

        SqlParams sqlParams = (SqlParams) paramMap.get(SQL_PARAMS);
        String sql = sqlParams.getSql();

        List<KeyValuePair> params = sqlParams.getParamDetails();

        StringBuilder finalSql = new StringBuilder();
        int sqlLen = sql.length();
        int counter = 0;
        for (int i = 0; i < sqlLen; i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                // 将"?"转为mybatis支持的参数方式
                String paramKey = getParamKey(counter);
                finalSql.append("#{").append(paramKey).append("}");
                paramMap.put(paramKey, params.get(counter).getValue());
                counter++;
            } else {
                finalSql.append(c);
            }
        }
        return finalSql.toString();
    }

    /**
     * 获取参数键
     *
     * @param index 索引值
     * @return 参数键
     */
    private String getParamKey(int index) {
        return SQL_PARAMS + index;
    }
}
