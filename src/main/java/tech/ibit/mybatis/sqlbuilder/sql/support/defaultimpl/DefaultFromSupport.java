package tech.ibit.mybatis.sqlbuilder.sql.support.defaultimpl;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.Table;
import tech.ibit.mybatis.sqlbuilder.sql.field.ListField;
import tech.ibit.mybatis.sqlbuilder.sql.support.FromSupport;

import java.util.List;

/**
 * DefaultFromSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultFromSupport<T> extends DefaultSqlSupport<T>,
        FromSupport<T>, DefaultTableSupport {

    /**
     * 获取from
     *
     * @return from
     */
    ListField<Table> getFrom();


    /**
     * `FROM table1 t1` 语句, t1表示"表别名"
     *
     * @param table 表对象
     * @return SQL对象
     * @see Table
     */
    @Override
    default T from(Table table) {
        getFrom().addItem(table);
        return getSql();
    }

    /**
     * `FROM table1 t1, table2 t2...` 语句, t1, t2表示"表别名"
     *
     * @param tables 表对象列表
     * @return SQL对象
     * @see Table
     */
    @Override
    default T from(List<Table> tables) {
        getFrom().addItems(tables);
        return getSql();
    }

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getFromPrepareStatement(boolean useAlias) {
        return getTablePrepareStatement(getFrom(), " FROM ", useAlias);
    }

}
