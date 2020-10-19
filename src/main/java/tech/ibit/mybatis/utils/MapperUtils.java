package tech.ibit.mybatis.utils;

import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.Mapper;
import tech.ibit.mybatis.MultipleIdMapper;
import tech.ibit.mybatis.SingleIdMapper;
import tech.ibit.mybatis.SqlProvider;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.converter.EntityConverter;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.mybatis.sqlbuilder.utils.IdSqlUtils;
import tech.ibit.mybatis.sqlbuilder.utils.UniqueKeySqlUtils;
import tech.ibit.structlog4j.Logger;
import tech.ibit.structlog4j.StructLoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper工具类
 *
 * @author iBit程序猿
 */
public class MapperUtils {

    private static final Logger logger = StructLoggerFactory.getLogger(MapperUtils.class);

    /**
     * 插入实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param <T>    实体类类型
     * @return 插入条数
     */
    public static <T> int insert(Mapper<T> mapper, T entity) {
        InsertSql sql = IdSqlUtils.insertInto(mapper, entity);
        AutoIncrementIdSetterMethod idSetterMethod =
                EntityConverter.getAutoIncrementIdSetterMethod(entity.getClass());
        if (null == idSetterMethod) {
            return sql.executeInsert();
        }
        KeyValuePair key = new KeyValuePair(SqlProvider.PARAM_KEY, null);
        //write auto increase key
        int result = sql.executeInsertWithGenerateKeys(key);
        if (result == 0) {
            return result;
        }
        try {
            updateAutoIncreaseId(entity, idSetterMethod, (Number) key.getValue());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("Error rawUpdate id", e);
            return 0;
        }
        return result;
    }

    /**
     * 通过主键删除
     *
     * @param mapper 实体对应mapper
     * @param id     主键
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 删除条数
     */
    public static <T, K> int deleteById(SingleIdMapper<T, K> mapper, K id) {
        return null == id ? 0 : IdSqlUtils.deleteById(mapper, id).executeDelete();
    }

    /**
     * 通过主键批量删除
     *
     * @param mapper 实体对应mapper
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 删除条数
     */
    public static <T, K> int deleteByIds(SingleIdMapper<T, K> mapper, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.deleteByIds(mapper, ids).executeDelete();
    }

    /**
     * 通过主键删除
     *
     * @param mapper 实体对应mapper
     * @param id     主键对象
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 删除条数
     */
    public static <T, K extends MultiId> int deleteByMultiId(MultipleIdMapper<T, K> mapper, K id) {
        return null == id ? 0 : IdSqlUtils.deleteByMultiId(mapper, id).executeDelete();
    }

    /**
     * 通过主键批量删除
     *
     * @param mapper 实体对应mapper
     * @param ids    主键对象
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 删除条数
     */
    public static <T, K extends MultiId> int deleteByMultiIds(MultipleIdMapper<T, K> mapper, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.deleteByMultiIds(mapper, ids).executeDelete();
    }

    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param <T>    实体类类型
     * @return 更新条数
     */
    public static <T> int updateById(Mapper<T> mapper, T entity) {
        return IdSqlUtils.updateById(mapper, entity).executeUpdate();
    }


    /**
     * 通过主键更新实体指定字段
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param columns 更新字段列表
     * @param <T>     实体类类型
     * @return 更新条数
     */
    public static <T> int updateById(Mapper<T> mapper, T entity, List<Column> columns) {
        return IdSqlUtils.updateById(mapper, entity, columns).executeUpdate();
    }


    /**
     * 通过主键批量更新实体
     *
     * @param mapper   实体对应mapper
     * @param entities 实体对象列表
     * @param <T>      实体类类型
     */
    public static <T> void batchUpdateById(Mapper<T> mapper, List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        if (entities.size() == 1) {
            updateById(mapper, entities.get(0));
        } else {
            List<PrepareStatement> sqlParamsList = entities.stream()
                    .map(entity -> IdSqlUtils.updateById(mapper, entity).getPrepareStatement()).collect(Collectors.toList());
            PrepareStatement sqlParams = merge(sqlParamsList);
            doLog(sqlParams);
            mapper.rawUpdate(sqlParams);
        }

    }

    /**
     * 通过主键批量更新实体
     *
     * @param mapper   实体对应mapper
     * @param entities 实体对象列表
     * @param columns  更新列
     * @param <T>      实体类类型
     */
    public static <T> void batchUpdateById(Mapper<T> mapper, List<T> entities, List<Column> columns) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        if (entities.size() == 1) {
            updateById(mapper, entities.get(0), columns);
        } else {
            List<PrepareStatement> sqlParamsList = entities.stream()
                    .map(entity -> IdSqlUtils.updateById(mapper, entity, columns).getPrepareStatement()).collect(Collectors.toList());
            PrepareStatement sqlParams = merge(sqlParamsList);
            doLog(sqlParams);
            mapper.rawUpdate(sqlParams);
        }

    }

    /**
     * 通过主键更新非忽略的列
     *
     * @param mapper        实体对应mapper
     * @param entities      实体对象列表
     * @param ignoreColumns 忽略的列
     * @param <T>           实体类类型
     */
    public static <T> void batchUpdateByIdAndIgnoreColumns(Mapper<T> mapper, List<T> entities, List<Column> ignoreColumns) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        List<Column> columns = EntityConverter.getUpdateColumns(entities.get(0).getClass(), ignoreColumns);
        batchUpdateById(mapper, entities, columns);
    }

    /**
     * 合并SqlParams
     *
     * @param sqlParamsList SqlParams列表
     * @return 合并后的SqlParams
     */
    private static PrepareStatement merge(List<PrepareStatement> sqlParamsList) {
        StringBuilder sql = new StringBuilder();
        List<ColumnValue> paramDetails = new ArrayList<>();
        sqlParamsList.forEach(sqlParams -> {
            sql.append(sqlParams.getPrepareSql()).append(";");
            paramDetails.addAll(sqlParams.getValues());
        });
        if (sql.length() > 0) {
            sql.deleteCharAt(sql.length() - 1);
        }
        return new PrepareStatement(sql.toString(), paramDetails);
    }


    /**
     * 通过主键更新非忽略的列
     *
     * @param mapper        实体对应mapper
     * @param entity        实体对象
     * @param ignoreColumns 忽略的列
     * @param <T>           实体类类型
     * @return 更新条数
     */
    public static <T> int updateByIdAndIgnoreColumns(Mapper<T> mapper, T entity, List<Column> ignoreColumns) {
        List<Column> columns = EntityConverter.getUpdateColumns(entity.getClass(), ignoreColumns);
        return updateById(mapper, entity, columns);
    }

    /**
     * 通过主键更新非忽略的列
     *
     * @param mapper        实体对应mapper
     * @param entity        实体对象
     * @param ignoreColumns 忽略的列
     * @param ids           主键列表
     * @param <T>           实体类类型
     * @param <K>           主键值类型
     * @return 更新条数
     */
    public static <T, K> int updateByIdAndIgnoreColumns(SingleIdMapper<T, K> mapper, T entity, List<Column> ignoreColumns, Collection<K> ids) {
        List<Column> columns = EntityConverter.getUpdateColumns(entity.getClass(), ignoreColumns);
        return updateByIds(mapper, entity, columns, ids);
    }

    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 更新条数
     */
    public static <T, K> int updateByIds(SingleIdMapper<T, K> mapper, T entity, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByIds(mapper, entity, ids).executeUpdate();
    }

    /**
     * 通过主键更新实体指定列
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param ids     主键列表
     * @param columns 更新列
     * @param <T>     实体类类型
     * @param <K>     主键值类型
     * @return 更新条数
     */
    public static <T, K> int updateByIds(SingleIdMapper<T, K> mapper, T entity, List<Column> columns, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByIds(mapper, entity, columns, ids).executeUpdate();
    }


    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 更新条数
     */
    public static <T, K extends MultiId> int updateByMultiIds(MultipleIdMapper<T, K> mapper, T entity, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByMultiIds(mapper, entity, ids).executeUpdate();
    }

    /**
     * 通过主键更新实体指定列
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param ids     主键列表
     * @param columns 更新列
     * @param <T>     实体类类型
     * @param <K>     主键值类型
     * @return 更新条数
     */
    public static <T, K extends MultiId> int updateByMultiIds(MultipleIdMapper<T, K> mapper, T entity, List<Column> columns, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByMultiIds(mapper, entity, columns, ids).executeUpdate();
    }

    /**
     * 通过主键获取实体
     *
     * @param mapper 实体对应mapper
     * @param id     主键
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 实体
     */
    public static <T, K> T getById(SingleIdMapper<T, K> mapper, K id) {
        return null == id ? null : IdSqlUtils.getById(mapper, id).executeQueryOne();
    }

    /**
     * 通过主键批量获取实体
     *
     * @param mapper 实体对应mapper
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 实体列表
     */
    public static <T, K> List<T> getByIds(SingleIdMapper<T, K> mapper, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : IdSqlUtils.getByIds(mapper, ids).executeQuery();
    }

    /**
     * 通过主键获取实体
     *
     * @param mapper 实体对应mapper
     * @param id     主键
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 实体
     */
    public static <T, K extends MultiId> T getByMultiId(MultipleIdMapper<T, K> mapper, K id) {
        return null == id ? null : IdSqlUtils.getByMultiId(mapper, id).executeQueryOne();
    }

    /**
     * 通过主键批量获取实体
     *
     * @param mapper 实体对应mapper
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @return 实体列表
     */
    public static <T, K extends MultiId> List<T> getByMultiIds(MultipleIdMapper<T, K> mapper, Collection<K> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : IdSqlUtils.getByMultiIds(mapper, ids).executeQuery();
    }

    /**
     * 通过主键获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param id     主键
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象
     */
    public static <T, K, P> P getPoById(SingleIdMapper<T, K> mapper, Class<P> clazz, K id) {
        return IdSqlUtils.getById(mapper, clazz, id).executeQueryOne(clazz);
    }

    /**
     * 通过主键批量获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象列表
     */
    public static <T, K, P> List<P> getPoByIds(SingleIdMapper<T, K> mapper, Class<P> clazz, Collection<K> ids) {
        return IdSqlUtils.getByIds(mapper, clazz, ids).executeQuery(clazz);
    }

    /**
     * 通过主键获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param id     主键
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象
     */
    public static <T, K extends MultiId, P> P getPoByMultiId(MultipleIdMapper<T, K> mapper, Class<P> clazz, K id) {
        return IdSqlUtils.getByMultiId(mapper, id).executeQueryOne(clazz);
    }

    /**
     * 通过主键批量获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <K>    主键值类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象列表
     */
    public static <T, K extends MultiId, P> List<P> getPoByMultiIds(MultipleIdMapper<T, K> mapper, Class<P> clazz, Collection<K> ids) {
        return IdSqlUtils.getByMultiIds(mapper, ids).executeQuery(clazz);
    }

    /**
     * 通过 unique key 获取实体
     *
     * @param mapper    实体对应mapper
     * @param uniqueKey unique key
     * @param <T>       实体类类型
     * @return 实体
     */
    public static <T> T getByUniqueKey(Mapper<T> mapper, UniqueKey uniqueKey) {
        return UniqueKeySqlUtils.getByUniqueKey(mapper, mapper.getPoClazz(), uniqueKey).executeQueryOne();
    }

    /**
     * 通过 unique key 批量获取实体
     *
     * @param mapper     实体对应mapper
     * @param uniqueKeys 主键列表
     * @param <T>        实体类类型
     * @return 实体列表
     */
    public static <T> List<T> getByUniqueKeys(Mapper<T> mapper, Collection<UniqueKey> uniqueKeys) {
        return UniqueKeySqlUtils.getByUniqueKeys(mapper, mapper.getPoClazz(), uniqueKeys).executeQuery();
    }

    /**
     * 通过 unique key 获取某个类型的持久化对象
     *
     * @param mapper    实体对应的mapper
     * @param clazz     持久化对象类
     * @param uniqueKey 主键
     * @param <T>       实体类类型
     * @param <P>       持久化对象类类型
     * @return 持久化对象
     */
    public static <T, P> P getPoByUniqueKey(Mapper<T> mapper, Class<P> clazz, UniqueKey uniqueKey) {
        return UniqueKeySqlUtils.getByUniqueKey(mapper, clazz, uniqueKey).executeQueryOne(clazz);
    }

    /**
     * 通过 unique key 批量获取某个类型的持久化对象
     *
     * @param mapper     实体对应的mapper
     * @param clazz      持久化对象类
     * @param uniqueKeys 主键列表
     * @param <T>        实体类类型
     * @param <P>        持久化对象类类型
     * @return 持久化对象列表
     */
    public static <T, P> List<P> getPoByUniqueKeys(Mapper<T> mapper, Class<P> clazz, Collection<UniqueKey> uniqueKeys) {
        return UniqueKeySqlUtils.getByUniqueKeys(mapper, clazz, uniqueKeys).executeQuery(clazz);
    }

    /**
     * 通过 unique key 更新实体
     *
     * @param mapper    实体对应mapper
     * @param entity    实体对象
     * @param uniqueKey unique key
     * @param <T>       实体类类型
     * @return 更新条数
     */
    public static <T> int updateByUniqueKey(Mapper<T> mapper, T entity, UniqueKey uniqueKey) {
        return updateByUniqueKeys(
                mapper, entity, null == uniqueKey ? null : Collections.singletonList(uniqueKey)
        );
    }

    /**
     * 通过 unique key 更新实体指定列
     *
     * @param mapper    实体对应mapper
     * @param entity    实体对象
     * @param uniqueKey unique key
     * @param columns   更新列
     * @param <T>       实体类类型
     * @return 更新条数
     */
    public static <T> int updateByUniqueKey(Mapper<T> mapper, T entity, List<Column> columns, UniqueKey uniqueKey) {
        return updateByUniqueKeys(
                mapper, entity, columns, null == uniqueKey ? null : Collections.singletonList(uniqueKey)
        );
    }

    /**
     * 通过 unique key 更新实体
     *
     * @param mapper     实体对应mapper
     * @param entity     实体对象
     * @param uniqueKeys unique key 列表
     * @param <T>        实体类类型
     * @return 更新条数
     */
    public static <T> int updateByUniqueKeys(Mapper<T> mapper, T entity, Collection<UniqueKey> uniqueKeys) {
        return UniqueKeySqlUtils.updateByUniqueKeys(mapper, entity, uniqueKeys).executeUpdate();
    }

    /**
     * 通过 unique key 更新实体指定列
     *
     * @param mapper     实体对应mapper
     * @param entity     实体对象
     * @param uniqueKeys unique key 列表
     * @param columns    更新列
     * @param <T>        实体类类型
     * @return 更新条数
     */
    public static <T> int updateByUniqueKeys(Mapper<T> mapper, T entity, List<Column> columns, Collection<UniqueKey> uniqueKeys) {
        return UniqueKeySqlUtils.updateByUniqueKeys(mapper, entity, columns, uniqueKeys).executeUpdate();
    }

    /**
     * 通过 unique key 删除
     *
     * @param mapper    实体对应mapper
     * @param uniqueKey 主键对象
     * @param <T>       实体类类型
     * @return 删除条数
     */
    public static <T> int deleteByUniqueKey(Mapper<T> mapper, UniqueKey uniqueKey) {
        return UniqueKeySqlUtils.deleteByUniqueKey(mapper, uniqueKey).executeDelete();
    }

    /**
     * 通过 unique key 批量删除
     *
     * @param mapper     实体对应mapper
     * @param uniqueKeys 主键对象
     * @param <T>        实体类类型
     * @return 删除条数
     */
    public static <T> int deleteByUniqueKeys(Mapper<T> mapper, Collection<UniqueKey> uniqueKeys) {
        return UniqueKeySqlUtils.deleteByUniqueKeys(mapper, uniqueKeys).executeDelete();
    }


    /**
     * 更新自增长id
     *
     * @param entity         实体类
     * @param idSetterMethod id设置类
     * @param key            值
     * @param <T>            实体类类型
     * @throws InvocationTargetException 调用失败
     * @throws IllegalAccessException    无权限访问方法
     */
    private static <T> void updateAutoIncreaseId(T entity, AutoIncrementIdSetterMethod idSetterMethod, Number key)
            throws InvocationTargetException, IllegalAccessException {
        Class<?> type = idSetterMethod.getType();
        if (Integer.class == type || int.class == type) {
            idSetterMethod.getMethod().invoke(entity, key.intValue());
        }
    }

    /**
     * 增加精确搜索关键字
     *
     * @param sql                Sql对象
     * @param keyword            查询关键字
     * @param searchColumns      模糊查询列
     * @param exactSearchColumns 精确查询列
     */
    public static void addExactKeywords(WhereSupport<?> sql, String keyword
            , List<Column> searchColumns, List<Column> exactSearchColumns) {

        if (StringUtils.isBlank(keyword)) {
            return;
        }

        if (CollectionUtils.isEmpty(searchColumns) && CollectionUtils.isEmpty(exactSearchColumns)) {
            return;
        }

        List<Criteria> criterion = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(searchColumns)) {
            String kw = getKeyword(keyword);
            searchColumns.forEach(column -> criterion.add(column.like(kw).or()));
        }

        if (CollectionUtils.isNotEmpty(exactSearchColumns)) {
            String kw = getExactKeyWord(keyword);
            exactSearchColumns.forEach(column -> criterion.add(column.eq(kw).or()));
        }

        sql.andWhere(criterion);
    }

    /**
     * 增加关键字查询参数
     *
     * @param sql           Sql对象
     * @param keyword       查询关键字
     * @param searchColumns 查询列
     */
    public static void addKeywords(WhereSupport<?> sql, String keyword, List<Column> searchColumns) {
        if (StringUtils.isNotBlank(keyword) && (CollectionUtils.isNotEmpty(searchColumns))) {
            keyword = getKeyword(keyword);
            List<Criteria> criterion = new ArrayList<>();
            for (Column column : searchColumns) {
                criterion.add(column.like(keyword).or());
            }
            sql.andWhere(criterion);
        }
    }

    /**
     * 获取keyword
     *
     * @param keyword 查询关键字
     * @return 结果
     */
    public static String getKeyword(String keyword) {
        keyword = StringUtils.trimToNull(keyword);
        if (null == keyword) {
            return null;
        }
        return "%" + getExactKeyWord(keyword) + "%";
    }

    /**
     * 获取精确的关键字
     *
     * @param keyword 关键字
     * @return 结果
     */
    public static String getExactKeyWord(String keyword) {
        keyword = StringUtils.trim(keyword);
        return null == keyword ? null : keyword.replaceAll("%", "\\\\%").replace("_", "\\_");

    }

    private static void doLog(PrepareStatement sqlParams) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate SQL", "sql", sqlParams.getPrepareSql(), "params", sqlParams.getParams());
        }
    }
}
