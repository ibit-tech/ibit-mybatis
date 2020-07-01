package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.IColumn;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.GroupBySupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.List;

/**
 * DefaultGroupBySupport
 *
 * @author IBIT程序猿
 */
public interface DefaultGroupBySupport<T> extends DefaultSqlSupport<T>,
        GroupBySupport<T>, DefaultPrepareStatementSupport {


    /**
     * Group by
     *
     * @return group by
     */
    ListField<Column> getGroupBy();


    /**
     * `GROUP BY t1.column`语句
     *
     * @param groupBy 相关列a
     * @return SQL对象
     * @see IColumn
     */
    @Override
    default T groupBy(Column groupBy) {
        getGroupBy().addItem(groupBy);
        return getSql();
    }

    /**
     * `GROUP BY t1.column1, t2.column2, ...`语句
     *
     * @param groupBys 相关列列表
     * @return SQL对象
     * @see IColumn
     */
    @Override
    default T groupBy(List<Column> groupBys) {
        getGroupBy().addItems(groupBys);
        return getSql();
    }

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
