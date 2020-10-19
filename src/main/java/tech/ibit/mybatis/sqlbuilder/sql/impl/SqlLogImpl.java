package tech.ibit.mybatis.sqlbuilder.sql.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.structlog4j.Logger;
import tech.ibit.structlog4j.StructLoggerFactory;

/**
 * 日志相关实现
 *
 * @author iBit程序猿
 */
public class SqlLogImpl {

    /**
     * 日志对象
     */
    private final Logger logger = StructLoggerFactory.getLogger(SqlLogImpl.class);


    /**
     * 打印日志
     *
     * @param statement 预查询对象
     */
    protected void doLog(PrepareStatement statement) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate SQL", "sql", statement.getPrepareSql(), "params", statement.getParams());
        }
    }

}
