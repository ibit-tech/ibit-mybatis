package tech.ibit.mybatis.sqlbuilder.sql.support.statement;

import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.CriteriaMaker;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.sql.support.ValuesSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultValuesSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultValuesSupport<T> extends ValuesSupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取列预查询SQL
     *
     * @return 预查询SQL
     */
    default PrepareStatement getColumnPrepareStatement() {
        List<Column> columns = getColumn().getItems();
        if (CollectionUtils.isEmpty(columns)) {
            return PrepareStatement.empty();
        }
        return getPrepareStatement("(", columns, Column::getName, null, ", ", ")");
    }

    /**
     * 获取Value预查询SQL
     *
     * @return 预查询SQL
     */
    default PrepareStatement getValuePrepareStatement() {
        List<Column> columns = getColumn().getItems();
        if (CollectionUtils.isEmpty(columns)) {
            return PrepareStatement.empty();
        }

        List<Object> values = getValue().getItems();
        if (CollectionUtils.isEmpty(values)) {
            return PrepareStatement.empty();
        }

        int columnSize = columns.size();
        int valueSize = values.size();

        List<String> valuesIns = getValueIns(columnSize, valueSize);
        String prepareSql = " VALUES" + StringUtils.join(valuesIns, ", ");

        List<ColumnValue> columnValues = new ArrayList<>();

        for (int i = 0; i < valueSize; i++) {
            columnValues.add(new ColumnValue(columns.get(i % columnSize), values.get(i)));
        }

        return new PrepareStatement(prepareSql, columnValues);
    }


    /**
     * 构造`?`参数
     *
     * @param columnSize 列数量
     * @param totalSize  总参数数量
     * @return ? 列表
     */
    default List<String> getValueIns(int columnSize, int totalSize) {
        List<String> valueIns = new ArrayList<>();
        int size = totalSize / columnSize;
        for (int i = 0; i < size; i++) {
            valueIns.add("(" + CriteriaMaker.getIn(columnSize) + ")");
        }
        return valueIns;
    }


}
