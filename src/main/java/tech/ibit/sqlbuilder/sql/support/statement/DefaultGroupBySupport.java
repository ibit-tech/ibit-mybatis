package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.GroupBySupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.List;

/**
 * DefaultGroupBySupport
 *
 * @author IBIT程序猿
 */
public interface DefaultGroupBySupport<T> extends GroupBySupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getGroupByPrepareStatement(boolean useAlias) {
        List<Column> groupBys = getGroupBy().getItems();
        if (CollectionUtils.isEmpty(groupBys)) {
            return PrepareStatement.empty();
        }
        return getPrepareStatement(" GROUP BY ", groupBys
                , (Column groupBy) -> groupBy.getCompareColumnName(useAlias), null, ", ");
    }
}
