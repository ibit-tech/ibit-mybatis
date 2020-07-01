package tech.ibit.mybatis.sqlbuilder;

import lombok.experimental.UtilityClass;
import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.sqlbuilder.sql.*;
import tech.ibit.mybatis.sqlbuilder.sql.impl.*;

/**
 * SqlFactory
 *
 * @author IBIT程序猿
 * @version 2.0
 */
@UtilityClass
public class SqlFactory {

    /**
     * 创建搜索
     *
     * @param <T>    模板类型
     * @param mapper mapper对象
     * @return 搜索sql
     */
    public <T> QuerySql<T> createQuery(RawMapper<T> mapper) {
        return new QuerySqlImpl<>(mapper);
    }


    /**
     * 创建计数
     *
     * @param mapper mapper对象
     * @return 计数sql
     */
    public  CountSql createCount(RawMapper mapper) {
        return new CountSqlImpl(mapper);
    }

    /**
     * 创建删除
     *
     * @param mapper mapper对象
     * @return 删除sql
     */
    public DeleteSql createDelete(RawMapper mapper) {
        return new DeleteSqlImpl(mapper);
    }

    /**
     * 创建插入
     *
     * @param mapper mapper对象
     * @return 插入sql
     */
    public InsertSql createInsert(RawMapper mapper) {
        return new InsertSqlImpl(mapper);
    }

    /**
     * 创建更新
     *
     * @param mapper mapper对象
     * @return 更新sql
     */
    public UpdateSql createUpdate(RawMapper mapper) {
        return new UpdateSqlImpl(mapper);
    }

}
