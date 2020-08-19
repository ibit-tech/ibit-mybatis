package tech.ibit.mybatis.sqlbuilder;

import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.sqlbuilder.sql.*;
import tech.ibit.mybatis.sqlbuilder.sql.impl.*;

/**
 * SqlFactory
 *
 * @author IBIT程序猿
 * @version 2.0
 */
public class SqlFactory {

    /**
     * 构造函数
     */
    private SqlFactory() {
    }

    /**
     * 创建搜索
     *
     * @param <T>    模板类型
     * @param mapper mapper对象
     * @return 搜索sql
     */
    public static <T> QuerySql<T> createQuery(Mapper<T> mapper) {
        return new QuerySqlImpl<>(mapper);
    }

    /**
     * 创建计数
     *
     * @param mapper mapper对象
     * @return 计数sql
     */
    public static CountSql createCount(Mapper<?> mapper) {
        return new CountSqlImpl(mapper);
    }

    /**
     * 创建删除
     *
     * @param mapper mapper对象
     * @return 删除sql
     */
    public static DeleteSql createDelete(Mapper<?> mapper) {
        return new DeleteSqlImpl(mapper);
    }

    /**
     * 创建插入
     *
     * @param mapper mapper对象
     * @return 插入sql
     */
    public static InsertSql createInsert(Mapper<?> mapper) {
        return new InsertSqlImpl(mapper);
    }

    /**
     * 创建更新
     *
     * @param mapper mapper对象
     * @return 更新sql
     */
    public static UpdateSql createUpdate(Mapper<?> mapper) {
        return new UpdateSqlImpl(mapper);
    }

}
