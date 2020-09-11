package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.List;

/**
 * TableSupport实现
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class TableSupportImpl implements PrepareStatementBuildSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param table    表
     * @param useAlias 是否使用别名
     * @param hook     前缀
     * @return 预查询SQL对象
     */
    public PrepareStatement getTablePrepareStatement(ListField<Table> table, String hook, boolean useAlias) {

        List<Table> froms = table.getItems();
        if (CollectionUtils.isEmpty(froms)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement(hook
                , froms, (Table from) -> from.getTableName(useAlias), null, ", ");
    }


}
