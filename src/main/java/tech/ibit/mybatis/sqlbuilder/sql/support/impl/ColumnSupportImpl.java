package tech.ibit.mybatis.sqlbuilder.sql.support.impl;

import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.sqlbuilder.FullTextColumn;
import tech.ibit.mybatis.sqlbuilder.IColumn;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.converter.EntityConverter;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.ColumnSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.SqlSupport;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ColumnSupport实现
 *
 * @author iBit程序猿
 */
public class ColumnSupportImpl<T>
        implements SqlSupport<T>, ColumnSupport<T>, PrepareStatementBuildSupport {

    /**
     * sql 对象
     */
    private final T sql;

    /**
     * 列
     */
    private final ListField<IColumn> column;

    /**
     * 构造函数
     *
     * @param sql sql对象
     */
    public ColumnSupportImpl(T sql) {
        this(sql, new ListField<>());
    }

    /**
     * 构造函数
     *
     * @param sql    sql对象
     * @param column 列
     */
    private ColumnSupportImpl(T sql, ListField<IColumn> column) {
        this.sql = sql;
        this.column = column;
    }

    /**
     * 对象复制（浅复制）
     *
     * @param sql sql对象
     * @param <K> sql对象模板
     * @return 复制后的对象
     */
    public <K> ColumnSupportImpl<K> copy(K sql) {
        return new ColumnSupportImpl<>(sql, column.copy());
    }

    /**
     * 获取列
     *
     * @return 列
     */
    public ListField<IColumn> getColumn() {
        return column;
    }

    @Override
    public T getSql() {
        return sql;
    }

    @Override
    public T column(List<? extends IColumn> columns) {
        getColumn().addItems(columns);
        return getSql();
    }

    @Override
    public T column(IColumn column) {
        getColumn().addItem(column);
        return getSql();
    }

    @Override
    public T columnPo(Class<?> poClass) {
        getColumn().addItems(EntityConverter.getColumns(poClass));
        return getSql();
    }


    /**
     * 重置列
     *
     * @param columns 列
     * @return SQL对象
     */
    public T resetColumn(List<? extends IColumn> columns) {
        column.setItems(columns.stream().map(c -> (IColumn) c).collect(Collectors.toList()));
        return getSql();
    }


    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    public PrepareStatement getColumnPrepareStatement(boolean useAlias) {
        List<IColumn> columns = getColumn().getItems();
        if (CollectionUtils.isEmpty(columns)) {
            return PrepareStatement.empty();
        }

        return getPrepareStatement("", columns
                , (IColumn column) -> column.getSelectColumnName(useAlias), (IColumn column) -> {
                    // 全文索引有点特殊
                    if (column instanceof FullTextColumn) {
                        return Collections.singletonList(column.value(((FullTextColumn) column).getValue()));
                    }
                    return null;
                }, ", ");
    }

}
