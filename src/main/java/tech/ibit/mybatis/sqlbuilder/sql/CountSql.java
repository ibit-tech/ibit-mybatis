package tech.ibit.mybatis.sqlbuilder.sql;

import tech.ibit.mybatis.sqlbuilder.sql.support.*;

/**
 * 定义计数接口
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface CountSql extends ColumnSupport<CountSql>,
        DistinctSupport<CountSql>,
        FromSupport<CountSql>,
        GroupBySupport<CountSql>,
        HavingSupport<CountSql>,
        JoinOnSupport<CountSql>,
        WhereSupport<CountSql>,
        UseAliasSupport {

    /**
     * 计数
     *
     * @return 计算结果
     */
    int executeCount();

}
