package tech.ibit.mybatis.sqlbuilder;

import java.util.Collections;

/**
 * 定义排序类
 *
 * @author IBIT程序猿
 * @version 1.0
 */
public class OrderBy implements IOrderBy {

    /**
     * 列
     */
    private IColumn column;

    /**
     * 是否倒叙
     */
    private boolean desc;

    /**
     * 构造函数
     *
     * @param column 列
     */
    public OrderBy(IColumn column) {
        this(column, false);
    }

    /**
     * 构造函数
     *
     * @param column 列
     * @param desc   是否倒序
     */
    public OrderBy(IColumn column, boolean desc) {
        this.column = column;
        this.desc = desc;
    }

    /**
     * 构造预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return BETWEEN值预查询SQL对象
     */
    @Override
    public PrepareStatement getPrepareStatement(boolean useAlias) {
        String columnName = getColumn().getCompareColumnName(useAlias);
        String prepareSql = columnName + (desc ? " DESC" : "");
        return new PrepareStatement(prepareSql, Collections.emptyList());
    }

    /**
     * Gets the value of column
     *
     * @return the value of column
     */
    public IColumn getColumn() {
        return column;
    }

    /**
     * Sets the column
     * <p>You can use getColumn() to get the value of column</p>
     *
     * @param column column
     */
    public void setColumn(IColumn column) {
        this.column = column;
    }

    /**
     * Gets the value of desc
     *
     * @return the value of desc
     */
    public boolean isDesc() {
        return desc;
    }

    /**
     * Sets the desc
     * <p>You can use getDesc() to get the value of desc</p>
     *
     * @param desc desc
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}
