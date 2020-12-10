package tech.ibit.mybatis.sqlbuilder;

import tech.ibit.mybatis.sqlbuilder.column.support.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 列定义
 *
 * @author iBit程序猿
 * @version 1.0
 */
public class Column implements IColumn,
        IColumnCriteriaItemSupport,
        IColumnAggregateSupport,
        IColumnFullTextSupport,
        IColumnSetItemSupport,
        IColumnOrderBySupport,
        IColumnUniqueKeySupport {

    /**
     * 表
     */
    private Table table;

    /**
     * 列名
     */
    private String name;

    /**
     * 是否为id
     */
    private boolean id;

    /**
     * 是否可以为空
     */
    private boolean nullable;


    /**
     * 是否自增长
     */
    private boolean autoIncrease;


    /**
     * 获取实例
     *
     * @param table 表名
     * @param name  列名
     * @return 列
     */
    public static Column getInstance(Table table, String name) {
        return new Column(table, name, false, false, false);
    }

    /**
     * 获取实例
     *
     * @param table    表名
     * @param name     列名
     * @param nullable 是否可为空
     * @return 列
     */
    public static Column getInstance(Table table, String name, boolean nullable) {
        return new Column(table, name, false, nullable, false);
    }

    /**
     * 获取id列
     *
     * @param table 表
     * @param name  列名
     * @return id列
     */
    public static Column getIdInstance(Table table, String name) {
        return new Column(table, name, true, false, false);
    }

    /**
     * 获取id列
     *
     * @param table        表
     * @param name         列名
     * @param autoIncrease 是否自增
     * @return id列
     */
    public static Column getIdInstance(Table table, String name, boolean autoIncrease) {
        return new Column(table, name, true, false, autoIncrease);
    }

    /**
     * 构造函数
     *
     * @param table 表
     * @param name  列名
     */
    @Deprecated
    public Column(Table table, String name) {
        this(table, name, false, false, false);
    }

    /**
     * 构造函数
     *
     * @param column 列
     */
    protected Column(Column column) {
        this(column.getTable(), column.getName(), column.isId(), column.isNullable(), column.isAutoIncrease());
    }

    /**
     * 构造函数
     *
     * @param table        表
     * @param name         列名
     * @param id           是否为id
     * @param nullable     是否为空
     * @param autoIncrease 是否自增
     */
    protected Column(Table table, String name, boolean id
            , boolean nullable, boolean autoIncrease) {
        this.table = table;
        this.name = name;
        this.id = id;
        this.nullable = nullable;
        this.autoIncrease = autoIncrease;
    }

    @Override
    public String getNameWithTableAlias() {
        return table.getAlias() + "." + name;
    }

    @Override
    public String getNameAs() {
        // 这里不定义别名
        return null;
    }

    @Override
    public IColumn getColumn() {
        return this;
    }


    /**
     * Gets the value of table
     *
     * @return the value of table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets the table
     * <p>You can use getTable() to get the value of table</p>
     *
     * @param table table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of id
     *
     * @return the value of id
     */
    public boolean isId() {
        return id;
    }

    /**
     * Sets the id
     * <p>You can use getId() to get the value of id</p>
     *
     * @param id id
     */
    public void setId(boolean id) {
        this.id = id;
    }

    /**
     * Gets the value of nullable
     *
     * @return the value of nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Sets the nullable
     * <p>You can use getNullable() to get the value of nullable</p>
     *
     * @param nullable nullable
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Gets the value of autoIncrease
     *
     * @return the value of autoIncrease
     */
    public boolean isAutoIncrease() {
        return autoIncrease;
    }

    /**
     * Sets the autoIncrease
     * <p>You can use getAutoIncrease() to get the value of autoIncrease</p>
     *
     * @param autoIncrease autoIncrease
     */
    public void setAutoIncrease(boolean autoIncrease) {
        this.autoIncrease = autoIncrease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Column column = (Column) o;
        return isId() == column.isId() &&
                isNullable() == column.isNullable() &&
                isAutoIncrease() == column.isAutoIncrease() &&
                getTable().equals(column.getTable()) &&
                getName().equals(column.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTable(), getName(), isId(), isNullable(), isAutoIncrease());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Column.class.getSimpleName() + "[", "]")
                .add("table=" + table)
                .add("name='" + name + "'")
                .add("id=" + id)
                .add("nullable=" + nullable)
                .add("autoIncrease=" + autoIncrease)
                .toString();
    }
}
