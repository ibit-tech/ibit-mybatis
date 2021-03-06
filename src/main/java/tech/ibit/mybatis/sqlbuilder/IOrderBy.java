package tech.ibit.mybatis.sqlbuilder;

/**
 * 定义OrderBy接口
 *
 * @author iBit程序猿
 * @version 1.0
 */
public interface IOrderBy extends PrepareStatementSupplier {

    /**
     * 获取列
     *
     * @return 列
     */
    IColumn getColumn();

    /**
     * 定义是否倒序
     *
     * @return 是否倒序
     */
    boolean isDesc();
}
