package tech.ibit.mybatis.sqlbuilder.sql;

import tech.ibit.mybatis.sqlbuilder.sql.support.*;

/**
 * UpdateSql
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface UpdateSql extends UpdateTableSupport<UpdateSql>,
        JoinOnSupport<UpdateSql>,
        SetSupport<UpdateSql>,
        WhereSupport<UpdateSql>,
        PrepareStatementSupport {

    /**
     * `UPDATE table1 t1` 语句, t1表示"表别名"
     *
     * @return SQL对象
     */
    @Deprecated
    UpdateSql updateDefault();

    /**
     * 执行更新
     *
     * @return 更新条数
     */
    int executeUpdate();
}
