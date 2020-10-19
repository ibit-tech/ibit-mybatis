package tech.ibit.mybatis.sqlbuilder.sql;

import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.sql.support.InsertTableSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.OnDuplicateKeyUpdateSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.PrepareStatementSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.ValuesSupport;

/**
 * InsertSql
 *
 * @author iBit程序猿
 * @version 2.0
 */
public interface InsertSql extends InsertTableSupport<InsertSql>,
        ValuesSupport<InsertSql>,
        OnDuplicateKeyUpdateSupport<InsertSql>,
        PrepareStatementSupport {

    /**
     * 插入表，从mapper获取
     *
     * @return SQL对象
     */
    @Deprecated
    InsertSql insertDefault();

    /**
     * 插入
     *
     * @return 插入条数
     */
    int executeInsert();


    /**
     * 插入
     *
     * @param key 生成主键接收对象
     * @return 插入条数
     */
    int executeInsertWithGenerateKeys(KeyValuePair key);

}
