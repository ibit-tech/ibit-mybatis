package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.IOrderBy;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.OrderBySupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * DefaultOrderBySupport
 *
 * @author IBIT程序猿
 */
public interface DefaultOrderBySupport<T> extends OrderBySupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getOrderByPrepareStatement(boolean useAlias) {
        List<IOrderBy> orderBys = getOrderBy().getItems();
        if (CollectionUtils.isEmpty(orderBys)) {
            return new PrepareStatement("", Collections.emptyList());
        }
        return getPrepareStatement(" ORDER BY ", orderBys, ", ", useAlias);
    }

}
