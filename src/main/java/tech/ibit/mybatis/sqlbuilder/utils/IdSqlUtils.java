package tech.ibit.mybatis.sqlbuilder.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import tech.ibit.mybatis.*;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.converter.ColumnSetValue;
import tech.ibit.mybatis.sqlbuilder.converter.EntityConverter;
import tech.ibit.mybatis.sqlbuilder.converter.TableColumnInfo;
import tech.ibit.mybatis.sqlbuilder.converter.TableColumnSetValues;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.DeleteSql;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.sqlbuilder.sql.UpdateSql;
import tech.ibit.mybatis.sqlbuilder.sql.support.SetSupport;
import tech.ibit.mybatis.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Id 相关的 sql 工具类
 *
 * @author IBIT程序猿
 * @version 1.0
 */
@UtilityClass
public class IdSqlUtils {

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值集合
     * @param <K>      主键类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public <T, K> QuerySql<T> getByIds(SingleIdMapper<T, K> mapper, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return getById(mapper, mapper.getId(), idValues);
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param <T>     模板类型
     * @param <K>     主键类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public <T, K> QuerySql<T> getById(SingleIdMapper<T, K> mapper, K idValue) {
        return getByIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值列表
     * @param <K>      返回实体类类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public <T, K extends MultiId> QuerySql<T> getByMultiIds(MultipleIdMapper<T, K> mapper, List<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        List<TableColumnSetValues> idValueList
                = EntityConverter.getTableColumnValuesList(new ArrayList<>(idValues), true);

        TableColumnSetValues firstIdValues = idValueList.get(0);

        //没有主键
        if (firstIdValues.getColumnValues().isEmpty()) {
            throw SqlException.idNotFound(firstIdValues.getTable().getName());
        }

        if (1 == firstIdValues.getColumnValues().size()) {
            Column id = (Column) firstIdValues.getColumnValues().get(0).getColumn();
            return getById(mapper, id, getIdValues(id, idValueList));
        }

        QuerySql<T> sql = mapper.createQuery()
                .columnDefaultPo()
                .fromDefault()
                .limit(idValues.size());

        appendWhereSql(idValueList, sql);

        return sql;
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param <K>     主键类型
     * @param <T>     模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public <T, K extends MultiId> QuerySql<T> getByMultiId(MultipleIdMapper<T, K> mapper, K idValue) {
        return getByMultiIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }


    /**
     * 构造通过主键删除对象的SQL对象参数（单列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值列表
     * @param <T>      返回实体类类型
     * @param <K>      主键值类型
     * @return SQL参数对象
     * @see DeleteSql
     */
    public <T, K> DeleteSql deleteByIds(SingleIdMapper<T, K> mapper, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return mapper.createDelete()
                .deleteFromDefault()
                .andWhere(mapper.getId().in(idValues));
    }

    /**
     * 构造通过主键删除对象的SQL对象参数（单列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param <T>     返回实体类类型
     * @param <K>     主键值类型
     * @return SQL参数对象
     * @see DeleteSql
     */
    public <T, K> DeleteSql deleteById(SingleIdMapper<T, K> mapper, K idValue) {
        return deleteByIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }

    /**
     * 构造通过主键删除对象的SQL对象参数（多列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键对象列表
     * @return SQL参数对象
     * @see DeleteSql
     * @see MultiId
     */
    public <T, K extends MultiId> DeleteSql deleteByMultiIds(MultipleIdMapper<T, K> mapper, List<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        List<TableColumnSetValues> idValueList =
                EntityConverter.getTableColumnValuesList(idValues, true);
        TableColumnSetValues firstIdValues = idValueList.get(0);

        //没有主键
        if (firstIdValues.getColumnValues().isEmpty()) {

            throw SqlException.idNotFound(firstIdValues.getTable().getName());

        } else if (1 == firstIdValues.getColumnValues().size()) {
            //single id
            Column id = (Column) firstIdValues.getColumnValues().get(0).getColumn();
            List<Object> actualIdValues = getIdValues(idValueList);
            return mapper.createDelete()
                    .deleteFromDefault()
                    .andWhere(id.in(actualIdValues));
        }

        DeleteSql sql = mapper.createDelete().deleteFromDefault();
        appendWhereSql(idValueList, sql);
        return sql;
    }


    /**
     * 构造通过主键删除对象的SQL对象参数（多列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键对象
     * @return SQL参数对象
     * @see DeleteSql
     * @see MultiId
     */
    public <T, K extends MultiId> DeleteSql deleteByMultiId(MultipleIdMapper<T, K> mapper, K idValue) {
        return deleteByMultiIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }


    /**
     * 构造插入对象的SQL对象参数
     *
     * @param mapper mapper对象
     * @param po     插入对象
     * @return SQL参数对象
     * @see InsertSql
     */
    public <T> InsertSql insertInto(Mapper<T> mapper, T po) {

        // 逻辑，1）只插入非null字段，
        // 2）如果字段不允许为null，值为null报错，
        // 3）id不允许为null
        TableColumnSetValues entity = EntityConverter.getTableColumnValues(po, true);
        if (entity.getColumnValues().isEmpty()) {
            throw SqlException.columnValueNotFound();
        }

        List<ColumnSetValue> columnValues2Insert = getFilterColumnSetValues(entity.getColumnValues());
        if (columnValues2Insert.isEmpty()) {
            throw SqlException.columnValueNotFound();
        }

        return mapper
                .createInsert()
                .insertDefault()
                .values(columnValues2Insert);
    }


    /**
     * 获取过滤的是指值
     *
     * @param columnSetValues 待设置值
     * @return 过滤后可插入的值
     */
    private static List<ColumnSetValue> getFilterColumnSetValues(List<ColumnSetValue> columnSetValues) {

        List<ColumnSetValue> columnValues2Insert = new ArrayList<>();
        columnSetValues.forEach(columnSetValue -> {
            Column column = (Column) columnSetValue.getColumn();
            Object value = columnSetValue.getValue();
            if (null != value) {
                // 子增长的id不能设置
                if (columnSetValue.isAutoIncrease()) {
                    throw SqlException.idAutoIncrease(column.getTable().getName(), column.getName());
                }
                columnValues2Insert.add(columnSetValue);
            } else {
                // 需要判断值是否可以为null
                if (isColumnNotNullable(columnSetValue)) {
                    throw SqlException.columnNullPointer(column.getTable().getName(), column.getName());
                }
            }
        });
        return columnValues2Insert;
    }

    /**
     * 判断字段是否不能为null
     *
     * @param columnSetValue 设置列-值
     * @return 判断结果
     */
    private boolean isColumnNotNullable(ColumnSetValue columnSetValue) {
        return !columnSetValue.isAutoIncrease() && !columnSetValue.isNullable();
    }


    /**
     * 构造批量插入对象的SQL对象参数
     * SQL语法 : `INSERT INTO table(column1, column2, ...) values(?, ?, ...), (?, ?, ...)`
     *
     * @param mapper  mapper对象
     * @param pos     返回实体类列表
     * @param columns 需要插入列
     * @return SQL参数对象
     * @see InsertSql
     */
    public <T> InsertSql batchInsertInto(Mapper<T> mapper, List<T> pos, List<Column> columns) {
        BatchInsertItems batchInsertItems = getBatchInsertItems(pos, columns);
        return mapper
                .createInsert()
                .insertDefault()
                .values(batchInsertItems.getColumns(), batchInsertItems.getValues());
    }

    /**
     * 构造通过主键更新对象的SQL参数对象（支持单列或多列主键）
     *
     * @param mapper       mapper对象
     * @param updateObject 更新对象
     * @return SQL参数对象
     * @see UpdateSql
     */
    public <T> UpdateSql updateById(Mapper<T> mapper, T updateObject) {
        return updateById(mapper, updateObject, null);
    }

    /**
     * 构造通过主键更新对象指定列的SQL参数对象（支持单列或多列主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param updateColumns 指定更新字段
     * @return SQL参数对象
     * @see UpdateSql
     */
    public <T> UpdateSql updateById(RawMapper<T> mapper, T updateObject, List<Column> updateColumns) {
        TableColumnInfo idEntity = getAndCheckTableIdInfo(updateObject.getClass());
        if (null != updateColumns) {
            if (updateColumns.isEmpty()) {
                throw SqlException.columnValueNotFound();
            }
            Set<Column> updateColumnSet = new LinkedHashSet<>(updateColumns);
            updateColumnSet.addAll(idEntity.getIds());
            updateColumns = new ArrayList<>(updateColumnSet);
        }
        TableColumnSetValues tableColumnValues = null == updateColumns
                ? EntityConverter.getTableColumnValues(updateObject, false)
                : EntityConverter.getTableColumnValues(updateObject, updateColumns);

        //检查主键是否为空
        checkIdNotNull(idEntity.getIds(), tableColumnValues.getColumnValues());

        UpdateSql sql = mapper
                .createUpdate()
                .updateDefault();

        for (ColumnSetValue cv : tableColumnValues.getColumnValues()) {
            Column column = (Column) cv.getColumn();
            Object value = cv.getValue();
            if (cv.isId()) {
                if (null == value) {
                    throw SqlException.idNullPointer(idEntity.getTable().getName(), column.getName());
                }
                sql.andWhere(column.eq(value));
            } else {
                if (!cv.isNullable() && null == value) {
                    throw SqlException.columnNullPointer(idEntity.getTable().getName(), column.getName());
                }
                sql.set(column.set(value));
            }
        }
        return sql;
    }


    /**
     * 构造通过主键批量更新对象的SQL参数对象（单列作为主键）
     *
     * @param mapper       mapper对象
     * @param updateObject 更新对象
     * @param idValues     主键值列表
     * @return SQL参数对象
     * @see UpdateSql
     */
    public <T, K> UpdateSql updateByIds(SingleIdMapper<T, K> mapper, T updateObject, Collection<K> idValues) {
        return updateByIds(mapper, updateObject, null, idValues);
    }

    /**
     * 构造通过主键批量更新对象指定列的SQL参数对象（单列作为主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param idValues      主键值列表
     * @param updateColumns 指定更新列
     * @return SQL参数对象
     * @see UpdateSql
     */
    public <T, K> UpdateSql updateByIds(SingleIdMapper<T, K> mapper, T updateObject, List<Column> updateColumns, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }

        TableColumnSetValues tableColumnValues = null == updateColumns
                ? EntityConverter.getTableColumnValues(updateObject, false)
                : EntityConverter.getTableColumnValues(updateObject, updateColumns);

        UpdateSql sql = mapper
                .createUpdate()
                .updateDefault();
        addSetsSql(mapper.getDefaultTable(), tableColumnValues, sql);
        sql.andWhere(mapper.getId().in(idValues));
        return sql;
    }

    /**
     * 构造通过主键批量更新对象的SQL参数对象（多列作为主键）
     *
     * @param mapper       mapper对象
     * @param updateObject 更新对象
     * @param idValues     主键对象列表
     * @return SQL参数对象
     * @see UpdateSql
     * @see MultiId
     */
    public <T, K extends MultiId> UpdateSql updateByMultiIds(MultipleIdMapper<T, K> mapper, T updateObject, List<K> idValues) {
        return updateByMultiIds(mapper, updateObject, null, idValues);
    }


    /**
     * 构造通过主键批量更新对象指定列的SQL参数对象（多列作为主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param idValues      主键值列表
     * @param updateColumns 指定更新列
     * @return Update相关SQLParams
     * @see UpdateSql
     * @see MultiId
     */
    public <T, K extends MultiId> UpdateSql updateByMultiIds(MultipleIdMapper<T, K> mapper, T updateObject
            , List<Column> updateColumns, List<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        List<TableColumnSetValues> idValueList =
                EntityConverter.getTableColumnValuesList(
                        new ArrayList<>(idValues), true);
        TableColumnSetValues firstIdValues = idValueList.get(0);

        Table table = firstIdValues.getTable();
        if (firstIdValues.getColumnValues().isEmpty()) {
            throw SqlException.idNotFound(table.getName());
        }

        TableColumnSetValues tableColumnValues = null == updateColumns
                ? EntityConverter.getTableColumnValues(updateObject, false)
                : EntityConverter.getTableColumnValues(updateObject, updateColumns);

        UpdateSql sql = SqlFactory
                .createUpdate(mapper)
                .update(table);
        addSetsSql(table, tableColumnValues, sql);

        //只有一个id
        if (1 == firstIdValues.getColumnValues().size()) {
            Column id = (Column) firstIdValues.getColumnValues().get(0).getColumn();
            sql.andWhere(id.in(getIdValues(idValueList)));
        } else {
            appendWhereSql(idValueList, sql);
        }
        return sql;
    }

    /**
     * 检查主键是否为null
     *
     * @param ids             主键离列表
     * @param columnSetValues 列-值对
     */
    private void checkIdNotNull(List<Column> ids, List<ColumnSetValue> columnSetValues) {
        Map<String, Object> idValueMap = new LinkedHashMap<>();
        ids.forEach(id -> idValueMap.put(id.getNameWithTableAlias(), null));
        columnSetValues.forEach(columnSetValue -> {
            String columnAlias = columnSetValue.getColumn().getNameWithTableAlias();
            if (idValueMap.containsKey(columnAlias)) {
                idValueMap.put(columnAlias, columnSetValue.getValue());
            }
        });
        for (Column id : ids) {
            if (null == idValueMap.get(id.getNameWithTableAlias())) {
                throw SqlException.idNullPointer(id.getTable().getName(), id.getName());
            }
        }
    }

    /**
     * 扩展`set`语句
     *
     * @param table             表
     * @param tableColumnValues 表列-值信息
     * @param sql               SQL对象
     */
    private void addSetsSql(Table table, TableColumnSetValues tableColumnValues, SetSupport sql) {
        for (ColumnSetValue cv : tableColumnValues.getColumnValues()) {
            Column column = (Column) cv.getColumn();
            Object value = cv.getValue();
            //id不能更新
            if (cv.isId()) {
                throw SqlException.idInvalidUpdate(table.getName(), column.getName());
            } else {
                if (!cv.isNullable() && null == value) {
                    throw SqlException.columnNullPointer(table.getName(), column.getName());
                }
                sql.set(column.set(value));
            }
        }
    }


    /**
     * 构造通过单个主键构造函数
     *
     * @param mapper   mapper对象
     * @param id       主键列
     * @param idValues 主键值列表
     * @return 预查询sql
     */
    private <T> QuerySql<T> getById(Mapper<T> mapper, Column id, Collection<?> idValues) {
        return mapper
                .createQuery()
                .columnDefaultPo()
                .fromDefault()
                .andWhere(id.in(idValues))
                .limit(idValues.size());
    }

    /**
     * 获取单个主建值列表
     *
     * @param id               主键
     * @param columnValuesList 列-值列表
     * @return 主键值列表
     */
    private List<Object> getIdValues(Column id, List<TableColumnSetValues> columnValuesList) {
        List<Object> idValues = new ArrayList<>(columnValuesList.size());
        for (TableColumnSetValues columnValues : columnValuesList) {
            for (ColumnValue kvPair : columnValues.getColumnValues()) {
                if (kvPair.getColumn().equals(id)) {
                    idValues.add(kvPair.getValue());
                }
            }
        }
        return idValues;
    }

    /**
     * 获取单个主键值列表
     *
     * @param columnValuesList 列-值列表
     * @return 主键值列表
     */
    private List<Object> getIdValues(List<TableColumnSetValues> columnValuesList) {
        List<Object> idValues = new ArrayList<>(columnValuesList.size());
        for (TableColumnSetValues aColumnValuesList : columnValuesList) {
            idValues.add(aColumnValuesList.getColumnValues().get(0).getValue());

        }
        return idValues;
    }

    /**
     * 扩展`where`语句
     *
     * @param columnValuesList 列-值列表
     * @param sql              Sql对象
     */
    private void appendWhereSql(List<TableColumnSetValues> columnValuesList, WhereSupport sql) {
        for (TableColumnSetValues columnValues : columnValuesList) {
            List<ColumnSetValue> cvs = columnValues.getColumnValues();
            if (CollectionUtils.isNotEmpty(cvs)) {
                List<CriteriaItem> items = cvs.stream()
                        .filter(Objects::nonNull)
                        .map(cv -> null == cv.getValue()
                                ? ((Column) cv.getColumn()).isNull()
                                : ((Column) cv.getColumn()).eq(cv.getValue()))
                        .collect(Collectors.toList());
                sql.orWhere(Criteria.ands(items));
            }
        }
    }

    /**
     * 获取列信息并检查主键是否为空
     *
     * @param poClazz 实体类
     * @param <T>     返回实体类类型
     * @return 列信息
     */
    private <T> TableColumnInfo getAndCheckTableIdInfo(Class<T> poClazz) {
        TableColumnInfo table = EntityConverter.getTableColumns(poClazz);
        if (CollectionUtils.isEmpty(table.getIds())) {
            throw SqlException.idNotFound(table.getTable().getName());
        }
        return table;
    }

    /**
     * 构造批量插入对象
     *
     * @param objs    待插入对象列表
     * @param columns 插入列
     * @return 批量插入对象
     */
    private BatchInsertItems getBatchInsertItems(List<?> objs, List<Column> columns) {
        if (CollectionUtils.isEmpty(columns)) {
            throw SqlException.columnValueNotFound();
        }

        Set<String> filterColumnSet = columns.stream()
                .map(Column::getNameWithTableAlias).collect(Collectors.toCollection(LinkedHashSet::new));

        List<Object> values = new ArrayList<>(objs.size() * columns.size());
        Table table = null;
        for (Object obj : objs) {
            TableColumnSetValues entity = EntityConverter.getTableColumnValues(obj, true);
            if (null == table) {
                table = entity.getTable();
            } else {
                if (!table.equals(entity.getTable())) {
                    throw SqlException.tableNotMatched(table.getName(), entity.getTable().getName());
                }
            }
            List<ColumnSetValue> columnValues2Insert = getFilterColumnSetValues(entity.getColumnValues(), filterColumnSet);
            columnValues2Insert.forEach(columnValue -> values.add(columnValue.getValue()));
        }
        return new BatchInsertItems(table, columns, values);
    }

    /**
     * 获取过滤的是指值
     *
     * @param columnSetValues 待设置值
     * @return 过滤后可插入的值
     */
    private List<ColumnSetValue> getFilterColumnSetValues(List<ColumnSetValue> columnSetValues
            , Set<String> filterColumnSet) {

        Map<String, ColumnSetValue> columnSetValueMap = new HashMap<>();

        columnSetValues.forEach(columnSetValue -> {

            Column column = (Column) columnSetValue.getColumn();
            String columnNameWithTableAlias = column.getNameWithTableAlias();
            Object value = columnSetValue.getValue();

            // 包含该字段
            if (filterColumnSet.contains(columnNameWithTableAlias)) {
                if (null != value) {
                    if (columnSetValue.isAutoIncrease()) {
                        throw SqlException.idAutoIncrease(column.getTable().getName(), column.getName());
                    }
                } else {
                    if (isColumnNotNullable(columnSetValue)) {
                        throw SqlException.columnNullPointer(column.getTable().getName(), column.getName());
                    }
                }
                columnSetValueMap.put(columnNameWithTableAlias, columnSetValue);
            } else {
                // 字段不存在
                if (isColumnNotNullable(columnSetValue)) {
                    throw SqlException.columnNullPointer(column.getTable().getName(), column.getName());
                }
            }
        });

        List<ColumnSetValue> columnValues2Insert = new ArrayList<>();
        filterColumnSet.forEach(column -> columnValues2Insert.add(columnSetValueMap.get(column)));
        return columnValues2Insert;
    }


    /**
     * 批量插入对象
     */
    @AllArgsConstructor
    @Getter
    private class BatchInsertItems {
        /**
         * 表
         */
        private Table table;

        /**
         * 插入列
         */
        private List<Column> columns;

        /**
         * 插入值列表
         */
        private List<Object> values;
    }

}
