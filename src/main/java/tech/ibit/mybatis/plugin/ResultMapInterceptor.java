package tech.ibit.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import tech.ibit.mybatis.SqlBuilder;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * ResultMap拦截器（支持参数中指定ResultMap)
 *
 * @author IBIT程序猿
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class ResultMapInterceptor implements Interceptor {

    private static final String XML_SUFFIX = ".xml";
    private static final String RESULT_MAPS = "resultMaps";

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

        // 在参数中指定resultMap
        if (null != invocation.getArgs()
                && invocation.getArgs().length > 2
                && invocation.getArgs()[1] instanceof Map
                && ((Map) invocation.getArgs()[1]).containsKey(SqlBuilder.RESULT_MAP)) {
            String resultMapName = ((Map) invocation.getArgs()[1]).get(SqlBuilder.RESULT_MAP).toString();
            int lastIndex = ms.getId().lastIndexOf(".");
            String mapperClassId = lastIndex < 0 ? "" : ms.getId().substring(0, lastIndex);
            String resultMapId = mapperClassId + "." + resultMapName;

            ResultMap newRm = ms.getConfiguration().getResultMap(resultMapId);
            Field field = MappedStatement.class.getDeclaredField(RESULT_MAPS);
            field.setAccessible(true);
            field.set(ms, Collections.singletonList(newRm));
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
