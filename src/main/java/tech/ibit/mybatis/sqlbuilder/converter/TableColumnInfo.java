package tech.ibit.mybatis.sqlbuilder.converter;

import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表字段信息类
 *
 * @author iBit程序猿
 * @version 1.0
 */
public class TableColumnInfo {

    /**
     * 表
     */
    private Table table;

    /**
     * 列
     */
    private List<Column> columnInfos;

    /**
     * 无参构造函数
     */
    public TableColumnInfo() {
    }

    /**
     * 构造函数
     *
     * @param table       表
     * @param columnInfos 列信息列表
     */
    public TableColumnInfo(Table table, List<Column> columnInfos) {
        this.table = table;
        this.columnInfos = columnInfos;
    }

    /**
     * 获取非主键列
     *
     * @return 非主键列
     */
    public List<Column> getNotIdColumns() {
        if (CollectionUtils.isEmpty(columnInfos)) {
            return Collections.emptyList();
        }
        return columnInfos.stream().filter(c -> !c.isId()).collect(Collectors.toList());
    }

    /**
     * 获取主键列
     *
     * @return 主键列
     */
    public List<Column> getIds() {
        if (CollectionUtils.isEmpty(columnInfos)) {
            return Collections.emptyList();
        }
        return columnInfos.stream().filter(Column::isId)
                .collect(Collectors.toList());
    }

    /**
     * 获取列
     *
     * @return 列
     */
    public List<Column> getColumns() {
        if (CollectionUtils.isEmpty(columnInfos)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(columnInfos);
    }

    /**
     * Gets the value of table
     *
     * @return the value of table
     */
    public Table getTable() {
        return table;
    }

}
