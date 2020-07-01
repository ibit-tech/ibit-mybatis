package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.InsertTableSupport;

import java.util.List;

/**
 * DefaultInsertTableSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultInsertTableSupport<T> extends DefaultSqlSupport<T>,
        InsertTableSupport<T>, DefaultTableSupport {


    /**
     * 获取插入表
     *
     * @return 插入表
     */
    ListField<Table> getInsertTable();


    /**
     * `INSERT INTO table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    @Override
    default T insert(Table table) {
        getInsertTable().addItem(table);
        return getSql();
    }

    /**
     * `INSERT INTO table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    @Override
    default T insert(List<Table> tables) {
        getInsertTable().addItems(tables);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getInsertPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getInsertTable(), "INSERT INTO ", useAlias);
    }
}
