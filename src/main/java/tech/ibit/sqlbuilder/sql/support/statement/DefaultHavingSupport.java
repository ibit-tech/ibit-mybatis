package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.ColumnValue;
import tech.ibit.sqlbuilder.Criteria;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.HavingSupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultHavingSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultHavingSupport<T> extends HavingSupport<T>, DefaultCriteriaSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getHavingPrepareStatement(boolean useAlias) {
        List<Criteria> criterion = getHaving().getItems();
        if (CollectionUtils.isEmpty(criterion)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();
        prepareSql.append(" HAVING ");

        append(criterion, useAlias, prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }
}
