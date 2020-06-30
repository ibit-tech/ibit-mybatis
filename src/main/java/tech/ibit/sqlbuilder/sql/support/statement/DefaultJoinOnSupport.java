package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.JoinOn;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.JoinOnSupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.List;

/**
 * DefaultJoinOnSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultJoinOnSupport<T> extends JoinOnSupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getJoinOnPrepareStatement(boolean useAlias) {
        List<JoinOn> joinOns = getJoinOn().getItems();
        if (CollectionUtils.isEmpty(joinOns)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement(" ", joinOns, " ", useAlias);
    }
}
