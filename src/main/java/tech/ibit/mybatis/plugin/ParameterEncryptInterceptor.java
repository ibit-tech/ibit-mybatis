package tech.ibit.mybatis.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.SqlProvider;
import tech.ibit.mybatis.plugin.strategy.TransferStrategy;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 解密拦截器
 *
 * @author iBit程序猿
 * @since 2.10
 */
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
})
public class ParameterEncryptInterceptor implements Interceptor {

    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 需要加密的字段
     */
    private final Set<String> columnsToEncrypt;

    /**
     * 转换策略
     */
    private final TransferStrategy strategy;

    /**
     * 参数加密
     *
     * @param columnsToEncrypt 参数加密
     * @param strategy         加密策略
     */
    public ParameterEncryptInterceptor(Set<String> columnsToEncrypt, TransferStrategy strategy) {
        this.columnsToEncrypt = columnsToEncrypt;
        this.strategy = strategy;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (!(invocation.getTarget() instanceof ParameterHandler)) {
            return invocation.proceed();
        }

        if (CollectionUtils.isEmpty(columnsToEncrypt) || null == strategy) {
            return invocation.proceed();
        }

        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();

        MetaObject metaObject = MetaObject.forObject(parameterHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                DEFAULT_REFLECTOR_FACTORY
        );

        BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
        String sql = boundSql.getSql().toLowerCase(Locale.ENGLISH);
        boolean isUpdate = !sql.startsWith("select");


        // 反射获取 参数对像
        Object parameterObject = parameterHandler.getParameterObject();
        if (!(parameterObject instanceof Map)) {
            return invocation.proceed();
        }

        Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;

        // 自定义框架
        if (parameterMap.containsKey(SqlProvider.PARAM_SQL_PARAMS)) {

            PrepareStatement statement = (PrepareStatement) parameterMap.get(SqlProvider.PARAM_SQL_PARAMS);
            if (CollectionUtils.isEmpty(statement.getValues())) {
                return invocation.proceed();
            }

            List<ColumnValue> paramDetails = statement.getValues();
            int parameterSize = paramDetails.size();

            // 需要转换的参数字段和值
            Map<String, Object> sqlParamMapToTransfer = new LinkedHashMap<>();

            for (int i = 0; i < parameterSize; i++) {
                ColumnValue detail = paramDetails.get(i);
                String column = detail.getColumn().getName();
                if (null != column) {
                    column = getColumn(column);
                    if (columnsToEncrypt.contains(column)) {
                        String parameterName = SqlProvider.getParamKey(i);
                        sqlParamMapToTransfer.put(parameterName, parameterMap.get(parameterName));
                    }
                }
            }
            updateParameterMap(metaObject, boundSql, sqlParamMapToTransfer, parameterMap, isUpdate, false);
            return invocation.proceed();
        }

        // 如果自己写xml
        Map<String, Object> paramMapToTransfer = new LinkedHashMap<>();
        // 如果自己写xml
        parameterMap.forEach((parameterName, value) -> {
            String column = getColumn((String) parameterName);
            if (columnsToEncrypt.contains(column)) {
                paramMapToTransfer.put((String) parameterName, value);
            }
        });

        if (!paramMapToTransfer.isEmpty()) {
            updateParameterMap(metaObject, boundSql, paramMapToTransfer, parameterMap, isUpdate, true);
        }

        return invocation.proceed();
    }

    /**
     * 更新参数map
     *
     * @param metaObject         元对象
     * @param boundSql           sql对象
     * @param paramMapToTransfer 待转换参数map
     * @param parameterMap       参数map
     */
    private void updateParameterMap(MetaObject metaObject
            , BoundSql boundSql
            , Map<String, Object> paramMapToTransfer
            , Map<String, Object> parameterMap
            , boolean isUpdate
            , boolean isUpdateAdditional) throws NoSuchFieldException, IllegalAccessException {
        Set<String> textSet = new HashSet<>(paramMapToTransfer.size());
        paramMapToTransfer.forEach((pName, text) -> {
            if (text instanceof List) {
                ((List<?>) text).forEach(t -> textSet.add((String) t));
            } else {
                textSet.add((String) text);
            }
        });

        Map<String, String> transferTextMap = isUpdate
                ? strategy.batchEncryptAndSaveText(textSet)
                : strategy.batchEncrypt(textSet);

        paramMapToTransfer.forEach((parameterName, text) -> {
            if (text instanceof List) {
                List<String> newText = ((List<?>) text).stream()
                        .map(t -> transferTextMap.get((String) t))
                        .collect(Collectors.toList());
                parameterMap.put(parameterName, newText);
            } else {
                String transferText = transferTextMap.get((String) text);
                parameterMap.put(parameterName, transferText);
            }
        });
        if (isUpdateAdditional) {
            updateAdditionalParameters(metaObject, boundSql, parameterMap);
        }
    }

    /**
     * 更新额外桉树
     *
     * @param metaObject   元对象
     * @param boundSql     sql对象
     * @param parameterMap 参数map
     * @throws NoSuchFieldException   字段不存在异常
     * @throws IllegalAccessException 禁止访问异常
     */
    private void updateAdditionalParameters(MetaObject metaObject
            , BoundSql boundSql, Map<?, ?> parameterMap) throws NoSuchFieldException, IllegalAccessException {
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        BoundSql newBoundSql = mappedStatement.getBoundSql(parameterMap);

        Field additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
        additionalParametersField.setAccessible(true);

        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        Set<String> parameterNames = additionalParameters.keySet();
        parameterNames.forEach(parameterName -> {
            // 比较恶心的listItem
            if (newBoundSql.hasAdditionalParameter(parameterName)) {
                Object value = newBoundSql.getAdditionalParameter(parameterName);
                additionalParameters.put(parameterName, value);

                boundSql.setAdditionalParameter(parameterName, value);
            }
        });
    }

    /**
     * 获取列名（不包含别名）
     *
     * @param column 列名
     * @return 列名
     */
    private String getColumn(String column) {
        int index = column.lastIndexOf(".");
        return index >= 0 ? column.substring(index + 1) : column;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
