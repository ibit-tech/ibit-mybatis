package tech.ibit.mybatis.sqlbuilder.sql.support.statement;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.support.UpdateTableSupport;

/**
 * DefaultUpdateTableSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultUpdateTableSupport<T> extends UpdateTableSupport<T>, DefaultTableSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getUpdatePrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getUpdateTable(), "UPDATE ", useAlias);
    }

}
