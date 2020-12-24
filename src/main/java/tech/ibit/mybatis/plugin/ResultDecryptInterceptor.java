package tech.ibit.mybatis.plugin;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import tech.ibit.mybatis.plugin.strategy.TransferStrategy;
import tech.ibit.mybatis.plugin.utils.EntityTransferUtils;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * 解密拦截器
 *
 * @author iBit程序猿
 * @since 2.10
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class ResultDecryptInterceptor implements Interceptor {

    /**
     * 转化策略
     */
    private final TransferStrategy strategy;

    /**
     * 构造函数
     *
     * @param strategy 转换策略
     */
    public ResultDecryptInterceptor(TransferStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof ResultSetHandler)) {
            return invocation.proceed();
        }

        if (null == strategy) {
            return invocation.proceed();
        }

        Object result = invocation.proceed();
        if (null == result) {
            return null;
        }

        if (result instanceof List) {
            EntityTransferUtils.decrypt((List<?>) result, strategy);
        } else {
            EntityTransferUtils.decrypt(result, strategy);
        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
