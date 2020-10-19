package tech.ibit.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import tech.ibit.mybatis.SqlProvider;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * ResultMap拦截器（支持参数中指定ResultMap)
 *
 * @author IBIT TECH
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class ResultMapInterceptor implements Interceptor {

    private static final String XML_SUFFIX = ".xml";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof Executor)) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

        // xml sql 不做处理
        if (ms.getResource().contains(XML_SUFFIX)) {
            return invocation.proceed();
        }

        // 指定resultMap
        ResultMap resultMap = ms.getResultMaps().iterator().next();
        if (CollectionUtils.isNotEmpty(resultMap.getResultMappings())) {
            return invocation.proceed();
        }

        // 动态改变resultType
        if (null != invocation.getArgs()
                && invocation.getArgs().length > 2
                && invocation.getArgs()[1] instanceof Map
                && ((Map<?, ?>) invocation.getArgs()[1]).containsKey(SqlProvider.PARAM_KEY_RESULT_TYPE)) {
            Class<?> resultType = (Class<?>) ((Map<?, ?>) invocation.getArgs()[1]).get(SqlProvider.PARAM_KEY_RESULT_TYPE);
            String newMsId = ms.getId() + resultType.getName().replace(".", "-") + "-class";

            try {
                MappedStatement newMs = ms.getConfiguration().getMappedStatement(newMsId, false);
                invocation.getArgs()[0] = newMs;
            } catch (IllegalArgumentException iae) {
                ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), newMsId, resultType
                        , resultMap.getResultMappings(), resultMap.getAutoMapping()).build();

                MappedStatement newMs = new MappedStatement
                        .Builder(ms.getConfiguration(), newMsId, ms.getSqlSource(), ms.getSqlCommandType())
                        .resultMaps(Collections.singletonList(newRm))
                        .build();
                try {
                    newMs.getConfiguration().addMappedStatement(newMs);
                    invocation.getArgs()[0] = newMs;
                } catch (IllegalArgumentException addException) {
                    // 并发通过异常处理
                    newMs = ms.getConfiguration().getMappedStatement(newMsId, false);
                    invocation.getArgs()[0] = newMs;
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        // 没有指定resultMap，默认处理
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
