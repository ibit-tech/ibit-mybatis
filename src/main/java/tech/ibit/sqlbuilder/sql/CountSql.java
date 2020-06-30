package tech.ibit.sqlbuilder.sql;

import tech.ibit.sqlbuilder.sql.support.*;

/**
 * 定义计数接口
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface CountSql<T> extends ColumnSupport<CountSql<T>>,
        DistinctSupport<CountSql<T>>,
        FromSupport<CountSql<T>>,
        GroupBySupport<CountSql<T>>,
        HavingSupport<CountSql<T>>,
        JoinOnSupport<CountSql<T>>,
        WhereSupport<CountSql<T>>,
        UseAliasSupport {

    /**
     * 计数
     *
     * @return 计算结果
     */
    int doCount();

}
