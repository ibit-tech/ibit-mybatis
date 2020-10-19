package tech.ibit.mybatis.sqlbuilder.sql;

import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.support.*;

import java.util.List;

/**
 * DeleteSql
 *
 * @author iBit程序猿
 * @version 2.0
 */
public interface DeleteSql extends DeleteSupport<DeleteSql>,
        FromSupport<DeleteSql>,
        JoinOnSupport<DeleteSql>,
        WhereSupport<DeleteSql>,
        PrepareStatementSupport {

    /**
     * 删除表，从mapper获取
     *
     * @return SQL对象
     */
    @Deprecated
    DeleteSql deleteFromDefault();

    /**
     * 删除表，item和from同时设置
     *
     * @param table 表
     * @return SQL对象
     */
    DeleteSql deleteFrom(Table table);


    /**
     * 删除表，item和from同时设置
     *
     * @param tables 表列表
     * @return SQL对象
     */
    DeleteSql deleteFrom(List<Table> tables);

    /**
     * 执行删除
     *
     * @return 删除条数
     */
    int executeDelete();
}
