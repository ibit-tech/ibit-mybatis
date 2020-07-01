package tech.ibit.mybatis.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.RawMapper;
import tech.ibit.mybatis.SqlProvider;
import tech.ibit.mybatis.sqlbuilder.*;
import tech.ibit.mybatis.sqlbuilder.converter.EntityConverter;
import tech.ibit.mybatis.sqlbuilder.sql.InsertSql;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.mybatis.sqlbuilder.utils.IdSqlUtils;
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
 * @author IBIT程序猿
 */
@UtilityClass
public class MapperUtils {

    private Logger logger = StructLoggerFactory.getLogger(MapperUtils.class);

    /**
     * 插入实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param <T>    实体类类型
     * @return 插入条数
     */
    public <T> int insert(RawMapper mapper, T entity) {
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
     * @param clazz  实体类
     * @param id     主键
     * @param <T>    主题类类型
     * @return 删除条数
     */
    public <T> int deleteById(RawMapper mapper, Class<T> clazz, Object id) {
        return null == id ? 0 : IdSqlUtils.deleteById(mapper, clazz, id).executeDelete();
    }

    /**
     * 通过主键批量删除
     *
     * @param mapper 实体对应mapper
     * @param clazz  实体类
     * @param ids    主键列表
     * @param <T>    主题类类型
     * @return 删除条数
     */
    public <T> int deleteByIds(RawMapper mapper, Class<T> clazz, Collection<?> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.deleteByIds(mapper, clazz, ids).executeDelete();
    }

    /**
     * 通过主键删除
     *
     * @param mapper 实体对应mapper
     * @param id     主键对象
     * @return 删除条数
     */
    public int deleteByMultiId(RawMapper mapper, MultiId id) {
        return null == id ? 0 : IdSqlUtils.deleteByMultiId(mapper, id).executeDelete();
    }

    /**
     * 通过主键批量删除
     *
     * @param mapper 实体对应mapper
     * @param ids    主键对象
     * @return 删除条数
     */
    public int deleteByMultiIds(RawMapper mapper, List<? extends MultiId> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.deleteByMultiIds(mapper, ids).executeDelete();
    }

    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @return 更新条数
     */
    public int updateById(RawMapper mapper, Object entity) {
        return IdSqlUtils.updateById(mapper, entity).executeUpdate();
    }


    /**
     * 通过主键更新实体指定字段
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param columns 更新字段列表
     * @return 更新条数
     */
    public int updateById(RawMapper mapper, Object entity, List<Column> columns) {
        return IdSqlUtils.updateById(mapper, entity, columns).executeUpdate();
    }


    /**
     * 通过主键批量更新实体
     *
     * @param mapper   实体对应mapper
     * @param entities 实体对象列表
     */
    public void batchUpdateById(RawMapper mapper, List<?> entities) {
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
     */
    public void batchUpdateById(RawMapper mapper, List<?> entities, List<Column> columns) {
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
     */
    public void batchUpdateByIdAndIgnoreColumns(RawMapper mapper, List<?> entities, List<Column> ignoreColumns) {
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
    private PrepareStatement merge(List<PrepareStatement> sqlParamsList) {
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
     * @return 更新条数
     */
    public int updateByIdAndIgnoreColumns(RawMapper mapper, Object entity, List<Column> ignoreColumns) {
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
     * @return 更新条数
     */
    public int updateByIdAndIgnoreColumns(RawMapper mapper, Object entity, List<Column> ignoreColumns, Collection<?> ids) {
        List<Column> columns = EntityConverter.getUpdateColumns(entity.getClass(), ignoreColumns);
        return updateByIds(mapper, entity, columns, ids);
    }

    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param ids    主键列表
     * @return 更新条数
     */
    public int updateByIds(RawMapper mapper, Object entity, Collection<?> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByIds(mapper, entity, ids).executeUpdate();
    }

    /**
     * 通过主键更新实体指定列
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param ids     主键列表
     * @param columns 更新列
     * @return 更新条数
     */
    public int updateByIds(RawMapper mapper, Object entity, List<Column> columns, Collection<?> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByIds(mapper, entity, columns, ids).executeUpdate();
    }


    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param ids    主键列表
     * @return 更新条数
     */
    public int updateByMultiIds(RawMapper mapper, Object entity, List<? extends MultiId> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByMultiIds(mapper, entity, ids).executeUpdate();
    }

    /**
     * 通过主键更新实体指定列
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param ids     主键列表
     * @param columns 更新列
     * @return 更新条数
     */
    public int updateByMultiIds(RawMapper mapper, Object entity, List<Column> columns, List<? extends MultiId> ids) {
        return CollectionUtils.isEmpty(ids) ? 0 : IdSqlUtils.updateByMultiIds(mapper, entity, columns, ids).executeUpdate();
    }

    /**
     * 通过主键获取实体
     *
     * @param mapper 实体对应mapper
     * @param clazz  实体类
     * @param id     主键
     * @param <T>    实体类类型
     * @return 实体
     */
    public <T> T getById(RawMapper<T> mapper, Class<T> clazz, Object id) {
        return null == id ? null : IdSqlUtils.getById(mapper, clazz, id).executeQueryOne();
    }

    /**
     * 通过主键批量获取实体
     *
     * @param mapper 实体对应mapper
     * @param clazz  实体类
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @return 实体列表
     */
    public <T> List<T> getByIds(RawMapper<T> mapper, Class<T> clazz, Collection<?> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : IdSqlUtils.getByIds(mapper, clazz, ids).executeQuery();
    }

    /**
     * 通过主键获取实体
     *
     * @param mapper 实体对应mapper
     * @param clazz  实体类
     * @param id     主键
     * @param <T>    实体类类型
     * @return 实体
     */
    public <T> T getByMultiId(RawMapper<T> mapper, Class<T> clazz, MultiId id) {
        return null == id ? null : IdSqlUtils.getByMultiId(mapper, clazz, id).executeQueryOne();
    }

    /**
     * 通过主键批量获取实体
     *
     * @param mapper 实体对应mapper
     * @param clazz  实体类
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @return 实体列表
     */
    public <T> List<T> getByMultiIds(RawMapper<T> mapper, Class<T> clazz, List<? extends MultiId> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : IdSqlUtils.getByMultiIds(mapper, clazz, ids).executeQuery();
    }


    /**
     * 通过主键获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param id     主键
     * @param <T>    模版类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象
     */
    public <T, P> P getPoById(RawMapper<T> mapper, Class<P> clazz, Object id) {
        T result = IdSqlUtils.getById(mapper, clazz, id).executeQueryOne();
        return EntityConverter.copyColumns(result, clazz);
    }

    /**
     * 通过主键批量获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param ids    主键列表
     * @param <T>    模版类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象列表
     */
    public <T, P> List<P> getPoByIds(RawMapper<T> mapper, Class<P> clazz, Collection<?> ids) {
        List<T> result = IdSqlUtils.getByIds(mapper, clazz, ids).executeQuery();
        return EntityConverter.copyColumns(result, clazz);
    }

    /**
     * 通过主键获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param id     主键
     * @param <T>    模版类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象
     */
    public <T, P> P getPoByMultiId(RawMapper<T> mapper, Class<P> clazz, MultiId id) {
        T result = IdSqlUtils.getByMultiId(mapper, clazz, id).executeQueryOne();
        return EntityConverter.copyColumns(result, clazz);
    }

    /**
     * 通过主键批量获取某个类型的持久化对象
     *
     * @param mapper 实体对应的mapper
     * @param clazz  持久化对象类
     * @param ids    主键列表
     * @param <T>    实体类类型
     * @param <P>    持久化对象类类型
     * @return 持久化对象列表
     */
    public <T, P> List<P> getPoByMultiIds(RawMapper<T> mapper, Class<P> clazz, List<? extends MultiId> ids) {
        List<T> result = IdSqlUtils.getByMultiIds(mapper, clazz, ids).executeQuery();
        return EntityConverter.copyColumns(result, clazz);
    }


    /**
     * 执行查询获取指定持久化对象
     *
     * @param sql     查询sql
     * @param poClass 持久化对象类
     * @param <T>     实体类类型
     * @param <P>     持久化对象类类型
     * @return 持久化对象列表
     */
    @SuppressWarnings("unchecked")
    public <T, P> List<P> executeQuery(QuerySql<T> sql, Class<P> poClass) {
        List<T> result = sql.executeQuery();
        return EntityConverter.copyColumns(result, poClass);
    }

    private <T> void updateAutoIncreaseId(T entity, AutoIncrementIdSetterMethod idSetterMethod, Number key)
            throws InvocationTargetException, IllegalAccessException {
        Class type = idSetterMethod.getType();
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
    public void addExactKeywords(WhereSupport sql, String keyword
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
    public void addKeywords(WhereSupport sql, String keyword, List<Column> searchColumns) {
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
    public String getKeyword(String keyword) {
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
    public String getExactKeyWord(String keyword) {
        keyword = StringUtils.trim(keyword);
        return null == keyword ? null : keyword.replaceAll("%", "\\\\%").replace("_", "\\_");

    }

    private void doLog(PrepareStatement sqlParams) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate SQL", "sql", sqlParams.getPrepareSql(), "params", sqlParams.getParams());
        }
    }
}
