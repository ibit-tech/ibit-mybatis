package tech.ibit.mybatis.sqlbuilder;

import org.apache.commons.lang.ArrayUtils;
import tech.ibit.mybatis.sqlbuilder.column.support.IColumnCriteriaItemSupport;
import tech.ibit.mybatis.sqlbuilder.column.support.IColumnOrderBySupport;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 定义聚合函数列
 *
 * @author iBit程序猿
 * @version 1.0
 */
public class AggregateColumn implements IColumn,
        IColumnOrderBySupport, IColumnCriteriaItemSupport {

    /**
     * 列分割符
     */
    private static final String COLUMN_SEPARATOR = ", ";

    /**
     * 全部列通配符
     */
    private static final String ALL_COLUMNS = "*";

    /**
     * 函数名称
     */
    private final String functionName;

    /**
     * 定义统计的列数组
     */
    private IColumn[] columns;

    /**
     * 列别名
     */
    private String nameAs;

    /**
     * 是否distinct
     */
    private final boolean distinct;

    /**
     * 使用空串
     */
    private boolean useEmpty;


    /**
     * 构造函数
     *
     * @param functionName 函数名称
     * @param column       列数
     */
    public AggregateColumn(String functionName, IColumn column) {
        this(functionName, new IColumn[]{column}, null, false);
        this.useEmpty = true;
    }

    /**
     * 构造函数
     *
     * @param functionName 函数名称
     * @param columns      列数组
     * @param nameAs       别名
     */
    public AggregateColumn(String functionName, IColumn[] columns, String nameAs) {
        this(functionName, columns, nameAs, false);
    }

    /**
     * 构造函数
     *
     * @param functionName 函数名称
     * @param columns      列数组
     * @param nameAs       别名
     * @param distinct     是否distinct
     */
    public AggregateColumn(String functionName, IColumn[] columns, String nameAs, boolean distinct) {
        this.functionName = functionName;
        this.columns = columns;
        this.nameAs = nameAs;
        this.distinct = distinct;
        this.useEmpty = false;
    }

    @Override
    public String getName() {
        return getColumnName(getColumnNames());
    }

    /**
     * 获取带表别名的列名称
     *
     * @return 列名称
     */
    @Override
    public String getNameWithTableAlias() {
        return getColumnName(getColumnNamesWithTableAlias());
    }

    /**
     * 获取列名称
     *
     * @param columnNames 列名称字串
     * @return 列名称
     */
    private String getColumnName(String columnNames) {
        return functionName + "(" + (distinct ? "DISTINCT " : "") + columnNames + ")";
    }

    /**
     * 获取列名
     */
    private String getColumnNames() {
        if (ArrayUtils.isEmpty(columns)) {
            return ALL_COLUMNS;
        }
        String columnNames = Arrays.stream(columns)
                .map(IColumn::getName).collect(Collectors.joining(COLUMN_SEPARATOR));
        return useEmpty ? (columnNames + COLUMN_SEPARATOR + "''") : columnNames;
    }

    /**
     * 获取带表别名的列名
     */
    private String getColumnNamesWithTableAlias() {
        if (ArrayUtils.isEmpty(columns)) {
            return ALL_COLUMNS;
        }
        String columnNames = Arrays.stream(columns)
                .map(IColumn::getNameWithTableAlias).collect(Collectors.joining(COLUMN_SEPARATOR));
        return useEmpty ? (columnNames + COLUMN_SEPARATOR + "''") : columnNames;
    }

    @Override
    public IColumn getColumn() {
        return this;
    }

    /**
     * Gets the value of functionName
     *
     * @return the value of functionName
     */
    public String getFunctionName() {
        return functionName;
    }


    /**
     * Gets the value of columns
     *
     * @return the value of columns
     */
    public IColumn[] getColumns() {
        return columns;
    }

    /**
     * Sets the columns
     * <p>You can use getColumns() to get the value of columns</p>
     *
     * @param columns columns
     */
    public void setColumns(IColumn[] columns) {
        this.columns = columns;
    }

    @Override
    public String getNameAs() {
        return nameAs;
    }

    /**
     * Sets the nameAs
     * <p>You can use getNameAs() to get the value of nameAs</p>
     *
     * @param nameAs nameAs
     */
    public void setNameAs(String nameAs) {
        this.nameAs = nameAs;
    }

    /**
     * Gets the value of distinct
     *
     * @return the value of distinct
     */
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AggregateColumn.class.getSimpleName() + "[", "]")
                .add("functionName='" + functionName + "'")
                .add("columns=" + Arrays.toString(columns))
                .add("nameAs='" + nameAs + "'")
                .add("distinct=" + distinct)
                .add("useEmpty=" + useEmpty)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregateColumn that = (AggregateColumn) o;
        return isDistinct() == that.isDistinct() &&
                useEmpty == that.useEmpty &&
                getFunctionName().equals(that.getFunctionName()) &&
                Arrays.equals(getColumns(), that.getColumns()) &&
                Objects.equals(getNameAs(), that.getNameAs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getFunctionName(), getNameAs(), isDistinct(), useEmpty);
        result = 31 * result + Arrays.hashCode(getColumns());
        return result;
    }
}
