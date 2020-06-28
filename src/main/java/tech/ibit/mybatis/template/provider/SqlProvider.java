package tech.ibit.mybatis.template.provider;

import lombok.Getter;
import lombok.Setter;
import tech.ibit.sqlbuilder.ColumnValue;
import tech.ibit.sqlbuilder.PrepareStatement;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Mybatis SQL构造器
 *
 * @author IBIT程序猿
 */
public class SqlProvider {

    /**
     * 参数sqlParams
     */
    public static final String PARAM_SQL_PARAMS = "sqlParams";

    /**
     * 参数key
     */
    public static final String PARAM_KEY = "key";

    /**
     * 参数key.value
     */
    public static final String PARAM_KEY_VALUE = "key.value";

    /**
     * 参数execute
     */
    public static final String METHOD_EXECUTE = "execute";


    @Setter
    @Getter
    private static Map<Class, Function<Object, Object>> valueFormatter;


    /**
     * 执行方法
     *
     * @param paramMap 参数map
     * @return 执行的sql
     */
    public String execute(Map<String, Object> paramMap) {
        PrepareStatement sqlParams = (PrepareStatement) paramMap.get(PARAM_SQL_PARAMS);
        String sql = sqlParams.getPrepareSql();

        List<ColumnValue> params = sqlParams.getValues();

        StringBuilder finalSql = new StringBuilder();
        int sqlLen = sql.length();
        int counter = 0;
        for (int i = 0; i < sqlLen; i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                // 将"?"转为mybatis支持的参数方式
                String paramKey = getParamKey(counter);
                finalSql.append("#{").append(paramKey).append("}");
                paramMap.put(paramKey, getValue(params.get(counter).getValue()));
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
        return PARAM_SQL_PARAMS + index;
    }

    /**
     * 获取值
     *
     * @param value 对象
     * @return 转换后的值
     */
    private Object getValue(Object value) {
        if (null == value) {
            return null;
        }
        if (null != valueFormatter && !valueFormatter.isEmpty()) {
            for (Map.Entry<Class, Function<Object, Object>> entry : valueFormatter.entrySet()) {
                if (entry.getKey().isAssignableFrom(value.getClass())) {
                    return entry.getValue().apply(value);
                }
            }
        }
        return value;
    }
}
