package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.ColumnValue;
import tech.ibit.sqlbuilder.Criteria;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultWhereSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultWhereSupport<T> extends WhereSupport<T>, DefaultCriteriaSupport {
    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getWherePrepareStatement(boolean useAlias) {
        List<Criteria> criterion = getWhere().getItems();
        if (CollectionUtils.isEmpty(criterion)) {
            return PrepareStatement.empty();
        }

        StringBuilder prepareSql = new StringBuilder();
        List<ColumnValue> values = new ArrayList<>();

        prepareSql.append(" WHERE ");

        append(criterion, useAlias, prepareSql, values);

        return new PrepareStatement(prepareSql.toString(), values);
    }

}
