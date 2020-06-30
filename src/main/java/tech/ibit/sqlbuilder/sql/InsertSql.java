package tech.ibit.sqlbuilder.sql;

import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.sql.support.InsertTableSupport;
import tech.ibit.sqlbuilder.sql.support.UseAliasSupport;
import tech.ibit.sqlbuilder.sql.support.ValuesSupport;

/**
 * InsertSql
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface InsertSql extends InsertTableSupport<InsertSql>,
        ValuesSupport<InsertSql>,
        UseAliasSupport {

    /**
     * 插入
     *
     * @return 插入条数
     */
    int doInsert();


    /**
     * 插入
     *
     * @param key 生成主键接收对象
     * @return 插入条数
     */
    int doInsertWithGenerateKeys(KeyValuePair key);

}
