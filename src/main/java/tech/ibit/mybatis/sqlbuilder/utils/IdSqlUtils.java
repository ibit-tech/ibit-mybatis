package tech.ibit.mybatis.sqlbuilder.utils;

import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.MultipleIdMapper;
import tech.ibit.mybatis.SingleIdMapper;
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
 * @author iBit程序猿
 * @version 1.0
 */
public class IdSqlUtils {

    private IdSqlUtils() {
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值集合
     * @param <K>      主键值类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public static <T, K> QuerySql<T> getByIds(SingleIdMapper<T, K> mapper, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return getByIds(mapper, mapper.getPoClazz(), mapper.getId(), idValues);
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param <T>     模板类型
     * @param <K>     主键值类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public static <T, K> QuerySql<T> getById(SingleIdMapper<T, K> mapper, K idValue) {
        return getByIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值集合
     * @param poClazz  查询类
     * @param <K>      主键值类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public static <T, K> QuerySql<T> getByIds(SingleIdMapper<T, K> mapper, Class<?> poClazz, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return getByIds(mapper, poClazz, mapper.getId(), idValues);
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（单列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param poClazz 查询类
     * @param <T>     模板类型
     * @param <K>     主键值类型
     * @return SQL参数对象
     * @see QuerySql
     */
    public static <T, K> QuerySql<T> getById(SingleIdMapper<T, K> mapper, Class<?> poClazz, K idValue) {
        return getByIds(mapper, poClazz, null == idValue ? null : Collections.singletonList(idValue));
    }

    /**
     * 构造通过单个主键构造函数
     *
     * @param mapper   mapper对象
     * @param poClazz  查询类
     * @param id       主键列
     * @param idValues 主键值列表
     * @return 预查询sql
     */
    private static <T> QuerySql<T> getByIds(Mapper<T> mapper, Class<?> poClazz, Column id, Collection<?> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return mapper
                .createQuery()
                .columnPo(poClazz)
                .andWhere(id.in(idValues))
                .limit(idValues.size());
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值列表
     * @param <K>      主键值类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public static <T, K extends MultiId> QuerySql<T> getByMultiIds(MultipleIdMapper<T, K> mapper, Collection<K> idValues) {
        return getByMultiIds(mapper, mapper.getPoClazz(), idValues);
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键值列表
     * @param poClazz  查询实体类
     * @param <K>      主键值类型
     * @param <T>      模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public static <T, K extends MultiId> QuerySql<T> getByMultiIds(MultipleIdMapper<T, K> mapper, Class<?> poClazz, Collection<K> idValues) {
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
            return getByIds(mapper, poClazz, id, getIdValues(id, idValueList));
        }

        QuerySql<T> sql = mapper.createQuery()
                .columnPo(poClazz)
                .limit(idValues.size());

        appendWhereSql(idValueList, sql);

        return sql;
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键值
     * @param <K>     主键值类型
     * @param <T>     模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public static <T, K extends MultiId> QuerySql<T> getByMultiId(MultipleIdMapper<T, K> mapper, K idValue) {
        return getByMultiId(mapper, mapper.getPoClazz(), idValue);
    }

    /**
     * 构造通过主键查询对象的SQL参数对象（多列作为主键）
     *
     * @param mapper  mapper对象
     * @param poClazz 查询类
     * @param idValue 主键值
     * @param <K>     主键值类型
     * @param <T>     模板类型
     * @return SQL参数对象
     * @see QuerySql
     * @see MultiId
     */
    public static <T, K extends MultiId> QuerySql<T> getByMultiId(MultipleIdMapper<T, K> mapper, Class<?> poClazz, K idValue) {
        return getByMultiIds(mapper, poClazz, null == idValue ? null : Collections.singletonList(idValue));
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
    public static <T, K> DeleteSql deleteByIds(SingleIdMapper<T, K> mapper, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        return mapper.createDelete()
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
    public static <T, K> DeleteSql deleteById(SingleIdMapper<T, K> mapper, K idValue) {
        return deleteByIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }

    /**
     * 构造通过主键删除对象的SQL对象参数（多列作为主键）
     *
     * @param mapper   mapper对象
     * @param idValues 主键对象列表
     * @param <T>      返回实体类类型
     * @param <K>      主键值类型
     * @return SQL参数对象
     * @see DeleteSql
     * @see MultiId
     */
    public static <T, K extends MultiId> DeleteSql deleteByMultiIds(MultipleIdMapper<T, K> mapper, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }
        List<TableColumnSetValues> idValueList =
                EntityConverter.getTableColumnValuesList(idValues, true);
        TableColumnSetValues firstIdValues = idValueList.get(0);

        //没有主键
        if (firstIdValues.getColumnValues().isEmpty()) {
            throw SqlException.idNotFound(firstIdValues.getTable().getName());
        }

        DeleteSql sql = mapper.createDelete();
        appendWhereSql(idValueList, sql);
        return sql;
    }


    /**
     * 构造通过主键删除对象的SQL对象参数（多列作为主键）
     *
     * @param mapper  mapper对象
     * @param idValue 主键对象
     * @param <T>     返回实体类类型
     * @param <K>     主键值类型
     * @return SQL参数对象
     * @see DeleteSql
     * @see MultiId
     */
    public static <T, K extends MultiId> DeleteSql deleteByMultiId(MultipleIdMapper<T, K> mapper, K idValue) {
        return deleteByMultiIds(mapper, null == idValue ? null : Collections.singletonList(idValue));
    }


    /**
     * 构造插入对象的SQL对象参数
     *
     * @param mapper mapper对象
     * @param po     插入对象
     * @param <T>    返回实体类类型
     * @return SQL参数对象
     * @see InsertSql
     */
    public static <T> InsertSql insertInto(Mapper<T> mapper, T po) {

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
    private static boolean isColumnNotNullable(ColumnSetValue columnSetValue) {
        return !columnSetValue.isAutoIncrease() && !columnSetValue.isNullable();
    }


    /**
     * 构造批量插入对象的SQL对象参数
     * SQL语法 : `INSERT INTO table(column1, column2, ...) values(?, ?, ...), (?, ?, ...)`
     *
     * @param mapper  mapper对象
     * @param pos     返回实体类列表
     * @param columns 需要插入列
     * @param <T>     返回实体类类型
     * @return SQL参数对象
     * @see InsertSql
     */
    public static <T> InsertSql batchInsertInto(Mapper<T> mapper, List<T> pos, List<Column> columns) {
        BatchInsertItems batchInsertItems = getBatchInsertItems(pos, columns);
        return mapper
                .createInsert()
                .values(batchInsertItems.getColumns(), batchInsertItems.getValues());
    }

    /**
     * 构造通过主键更新对象的SQL参数对象（支持单列或多列主键）
     *
     * @param mapper       mapper对象
     * @param updateObject 更新对象
     * @param <T>          返回实体类类型
     * @return SQL参数对象
     * @see UpdateSql
     */
    public static <T> UpdateSql updateById(Mapper<T> mapper, T updateObject) {
        return updateById(mapper, updateObject, null);
    }

    /**
     * 构造通过主键更新对象指定列的SQL参数对象（支持单列或多列主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param updateColumns 指定更新字段
     * @param <T>           返回实体类类型
     * @return SQL参数对象
     * @see UpdateSql
     */
    public static <T> UpdateSql updateById(Mapper<T> mapper, T updateObject, List<Column> updateColumns) {
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

        UpdateSql sql = mapper.createUpdate();

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
     * @param <T>          返回实体类类型
     * @param <K>          主键值类型
     * @return SQL参数对象
     * @see UpdateSql
     */
    public static <T, K> UpdateSql updateByIds(SingleIdMapper<T, K> mapper, T updateObject, Collection<K> idValues) {
        return updateByIds(mapper, updateObject, null, idValues);
    }

    /**
     * 构造通过主键批量更新对象指定列的SQL参数对象（单列作为主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param idValues      主键值列表
     * @param updateColumns 指定更新列
     * @param <T>           返回实体类类型
     * @param <K>           主键值类型
     * @return SQL参数对象
     * @see UpdateSql
     */
    public static <T, K> UpdateSql updateByIds(SingleIdMapper<T, K> mapper, T updateObject, List<Column> updateColumns, Collection<K> idValues) {
        if (CollectionUtils.isEmpty(idValues)) {
            throw SqlException.idValueNotFound();
        }

        TableColumnSetValues tableColumnValues = null == updateColumns
                ? EntityConverter.getTableColumnValues(updateObject, false)
                : EntityConverter.getTableColumnValues(updateObject, updateColumns);

        UpdateSql sql = mapper.createUpdate();
        addSetsSql(tableColumnValues, sql);
        sql.andWhere(mapper.getId().in(idValues));
        return sql;
    }

    /**
     * 构造通过主键批量更新对象的SQL参数对象（多列作为主键）
     *
     * @param mapper       mapper对象
     * @param updateObject 更新对象
     * @param idValues     主键对象列表
     * @param <T>          返回实体类类型
     * @param <K>          主键值类型
     * @return SQL参数对象
     * @see UpdateSql
     * @see MultiId
     */
    public static <T, K extends MultiId> UpdateSql updateByMultiIds(MultipleIdMapper<T, K> mapper, T updateObject, Collection<K> idValues) {
        return updateByMultiIds(mapper, updateObject, null, idValues);
    }


    /**
     * 构造通过主键批量更新对象指定列的SQL参数对象（多列作为主键）
     *
     * @param mapper        mapper对象
     * @param updateObject  更新对象
     * @param idValues      主键值列表
     * @param updateColumns 指定更新列
     * @param <T>           返回实体类类型
     * @param <K>           主键值类型
     * @return Update相关SQLParams
     * @see UpdateSql
     * @see MultiId
     */
    public static <T, K extends MultiId> UpdateSql updateByMultiIds(MultipleIdMapper<T, K> mapper, T updateObject
            , List<Column> updateColumns, Collection<K> idValues) {
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

        UpdateSql sql = mapper
                .createUpdate()
                .update(table);
        addSetsSql(tableColumnValues, sql);
        appendWhereSql(idValueList, sql);
        return sql;
    }

    /**
     * 检查主键是否为null
     *
     * @param ids             主键离列表
     * @param columnSetValues 列-值对
     */
    private static void checkIdNotNull(List<Column> ids, List<ColumnSetValue> columnSetValues) {
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
     * @param tableColumnValues 表列-值信息
     * @param sql               SQL对象
     */
    public static void addSetsSql(TableColumnSetValues tableColumnValues, SetSupport<?> sql) {
        for (ColumnSetValue cv : tableColumnValues.getColumnValues()) {
            Column column = (Column) cv.getColumn();
            Object value = cv.getValue();
            //id不能更新
            if (cv.isId()) {
                throw SqlException.idInvalidUpdate(((Column) cv.getColumn()).getTable().getName(), column.getName());
            } else {
                if (!cv.isNullable() && null == value) {
                    throw SqlException.columnNullPointer(((Column) cv.getColumn()).getTable().getName(), column.getName());
                }
                sql.set(column.set(value));
            }
        }
    }

    /**
     * 获取单个主建值列表
     *
     * @param id               主键
     * @param columnValuesList 列-值列表
     * @return 主键值列表
     */
    private static List<Object> getIdValues(Column id, List<TableColumnSetValues> columnValuesList) {
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
     * 扩展`where`语句
     *
     * @param columnValuesList 列-值列表
     * @param sql              Sql对象
     */
    private static void appendWhereSql(List<TableColumnSetValues> columnValuesList, WhereSupport<?> sql) {

        // 优化只有一个值
        if (columnValuesList.size() == 1) {
            List<ColumnSetValue> cvs = columnValuesList.get(0).getColumnValues();
            if (CollectionUtils.isNotEmpty(cvs)) {
                cvs.stream().filter(Objects::nonNull).forEach(
                        cv ->
                                sql.andWhere(
                                        null == cv.getValue()
                                                ? ((Column) cv.getColumn()).isNull()
                                                : ((Column) cv.getColumn()).eq(cv.getValue()))
                );
            }
            return;
        }

        TableColumnSetValues firstIdValues = columnValuesList.get(0);

        // 优化只有一个column
        if (firstIdValues.getColumnValues().size() == 1) {
            Column id = (Column) firstIdValues.getColumnValues().get(0).getColumn();
            List<Object> values = getIdValues(id, columnValuesList);
            sql.andWhere(values.size() == 1
                    ? (null == values.get(0) ? id.isNull() : id.eq(values.get(0)))
                    : id.in(values));
            return;
        }

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
     * @param poClazz 查询类
     * @param <T>     返回实体类类型
     * @return 列信息
     */
    private static <T> TableColumnInfo getAndCheckTableIdInfo(Class<T> poClazz) {
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
    private static BatchInsertItems getBatchInsertItems(List<?> objs, List<Column> columns) {
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
    private static List<ColumnSetValue> getFilterColumnSetValues(List<ColumnSetValue> columnSetValues
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
    private static class BatchInsertItems {
        /**
         * 表
         */
        private final Table table;

        /**
         * 插入列
         */
        private final List<Column> columns;

        /**
         * 插入值列表
         */
        private final List<Object> values;

        /**
         * 构造函数
         *
         * @param table   表
         * @param columns 插入列列表
         * @param values  值列表
         */
        public BatchInsertItems(Table table, List<Column> columns, List<Object> values) {
            this.table = table;
            this.columns = columns;
            this.values = values;
        }

        /**
         * Gets the value of table
         *
         * @return the value of table
         */
        public Table getTable() {
            return table;
        }

        /**
         * Gets the value of columns
         *
         * @return the value of columns
         */
        public List<Column> getColumns() {
            return columns;
        }

        /**
         * Gets the value of values
         *
         * @return the value of values
         */
        public List<Object> getValues() {
            return values;
        }
    }

}
