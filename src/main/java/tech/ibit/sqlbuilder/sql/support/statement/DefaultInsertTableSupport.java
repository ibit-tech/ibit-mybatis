package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.InsertTableSupport;

/**
 * DefaultInsertTableSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultInsertTableSupport<T> extends InsertTableSupport<T>, DefaultTableSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getInsertPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getInsertTable(), "INSERT INTO ", useAlias);
    }
}
