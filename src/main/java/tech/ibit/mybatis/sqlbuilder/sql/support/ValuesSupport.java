package tech.ibit.mybatis.sqlbuilder.sql.support;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;

import java.util.List;

/**
 * Value Support
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public interface ValuesSupport<T> extends SqlSupport<T> {

    /**
     * 获取列
     *
     * @return 列
     */
    ListField<Column> getColumn();

    /**
     * 获取值
     *
     * @return 值
     */
    ListField<Object> getValue();

    /**
     * `(column1, column2, ...) VALUES(?, ?, ...)`语句
     *
     * @param columnValues 列和值列表
     * @return SQL对象
     * @see ColumnValue
     */
    default T values(List<? extends ColumnValue> columnValues) {
        columnValues.forEach(this::values);
        return getSql();
    }

    /**
     * `(column1) VALUES(?)`语句
     *
     * @param columnValue 列和值
     * @return SQL对象
     * @see ColumnValue
     */
    default T values(ColumnValue columnValue) {
        getColumn().addItem((Column) columnValue.getColumn());
        getValue().addItem(columnValue.getValue());
        return getSql();
    }

    /**
     * `(column1, column2, ...) VALUES(?, ?, ...)`语句
     *
     * @param columns 列列表
     * @param values  值列表
     * @return SQL对象
     * @see ColumnValue
     */
    default T values(List<Column> columns, List<Object> values) {
        getColumn().addItems(columns);
        getValue().addItems(values);
        return getSql();
    }

}
