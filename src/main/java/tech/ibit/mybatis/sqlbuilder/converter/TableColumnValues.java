package tech.ibit.mybatis.sqlbuilder.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.Table;

import java.util.List;

/**
 * 列-值信息
 *
 * @author IBIT程序猿
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class TableColumnValues {

    /**
     * 表
     */
    private Table table;

    /**
     * 列-值对列表
     */
    private List<ColumnValue> columnValues;
}
