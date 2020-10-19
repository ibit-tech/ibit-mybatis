package tech.ibit.mybatis.sqlbuilder.sql;

import java.util.List;

/**
 * Page
 *
 * @author iBit程序猿
 */
public class Page<T> {

    /**
     * 开始位置
     */
    private int start;

    /**
     * 返回条数
     */
    private int limit;

    /**
     * 总条数
     */
    private int total;

    /**
     * 返回对象列表
     */
    private List<T> results;

    /**
     * 构造函数
     *
     * @param start   开始位置
     * @param limit   条数
     * @param total   总条数
     * @param results 结果
     */
    public Page(int start, int limit, int total, List<T> results) {
        this.start = start;
        this.limit = limit;
        this.total = total;
        this.results = results;
    }

    /**
     * 构造函数
     */
    public Page() {
    }

    /**
     * Gets the value of start
     *
     * @return the value of start
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start
     * <p>You can use getStart() to get the value of start</p>
     *
     * @param start start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets the value of limit
     *
     * @return the value of limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit
     * <p>You can use getLimit() to get the value of limit</p>
     *
     * @param limit limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Gets the value of total
     *
     * @return the value of total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the total
     * <p>You can use getTotal() to get the value of total</p>
     *
     * @param total total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Gets the value of results
     *
     * @return the value of results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Sets the results
     * <p>You can use getResults() to get the value of results</p>
     *
     * @param results results
     */
    public void setResults(List<T> results) {
        this.results = results;
    }
}
