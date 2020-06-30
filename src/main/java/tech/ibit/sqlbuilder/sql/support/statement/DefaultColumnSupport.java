package tech.ibit.sqlbuilder.sql.support.statement;

import tech.ibit.sqlbuilder.IColumn;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.sql.support.ColumnSupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.List;

/**
 * DefaultColumnSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultColumnSupport<T> extends ColumnSupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getColumnPrepareStatement(boolean useAlias) {
        List<IColumn> columns = getColumn().getItems();
        if (CollectionUtils.isEmpty(columns)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement("", columns
                , (IColumn column) -> column.getSelectColumnName(useAlias), null, ", ");
    }

}
