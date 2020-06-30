package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.FromSupport;

/**
 * DefaultFromSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultFromSupport<T> extends FromSupport<T>, DefaultTableSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getFromPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getFrom(), " FROM ", useAlias);
    }

}
