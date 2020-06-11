package tech.ibit.mybatis;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import tech.ibit.mybatis.template.mapper.Mapper;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.converter.EntityConverter;
import tech.ibit.sqlbuilder.sql.support.WhereSupport;
import tech.ibit.sqlbuilder.utils.CollectionUtils;
import tech.ibit.sqlbuilder.utils.DaoUtils;
import tech.ibit.structlog4j.Logger;
import tech.ibit.structlog4j.StructLoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapperDAO工具类
 *
 * @author IBIT程序猿
 */
@UtilityClass
public class MapperDaoUtils {

    private Logger logger = StructLoggerFactory.getLogger(MapperDaoUtils.class);


    /**
     * 插入实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param <T>    实体类类型
     * @return 插入条数
     */
    public <T> int insert(Mapper mapper, T entity) {
        PrepareStatement sqlParams = DaoUtils.insertInto(entity);
        doLog(sqlParams);
        AutoIncrementIdSetterMethod idSetterMethod =
                EntityConverter.getAutoIncrementIdSetterMethod(entity.getClass());
        if (null == idSetterMethod) {
            return mapper.insert(sqlParams);
        }
        KeyValuePair key = new KeyValuePair(SqlBuilder.KEY, null);
        //write auto increase key
        int result = mapper.insertWithGenerateKeys(sqlParams, key);
        if (result == 0) {
            return result;
        }
        try {
            updateAutoIncreaseId(entity, idSetterMethod, (Number) key.getValue());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("Error update id", e);
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
    public <T> int deleteById(Mapper mapper, Class<T> clazz, Object id) {
        PrepareStatement sqlParams = DaoUtils.deleteById(clazz, id);
        doLog(sqlParams);
        return mapper.update(sqlParams);
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
    public <T> int deleteByIds(Mapper mapper, Class<T> clazz, Collection<?> ids) {
        PrepareStatement sqlParams = DaoUtils.deleteByIds(clazz, ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }

    /**
     * 通过主键删除
     *
     * @param mapper 实体对应mapper
     * @param id     主键对象
     * @return 删除条数
     */
    public int deleteByMultiId(Mapper mapper, MultiId id) {
        PrepareStatement sqlParams = DaoUtils.deleteByMultiId(id);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }

    /**
     * 通过主键批量删除
     *
     * @param mapper 实体对应mapper
     * @param ids    主键对象
     * @return 删除条数
     */
    public int deleteByMultiIds(Mapper mapper, List<? extends MultiId> ids) {
        PrepareStatement sqlParams = DaoUtils.deleteByMultiIds(ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }

    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @return 更新条数
     */
    public int updateById(Mapper mapper, Object entity) {
        PrepareStatement sqlParams = DaoUtils.updateById(entity);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }


    /**
     * 通过主键更新实体指定字段
     *
     * @param mapper  实体对应mapper
     * @param entity  实体对象
     * @param columns 更新字段列表
     * @return 更新条数
     */
    public int updateById(Mapper mapper, Object entity, List<Column> columns) {
        PrepareStatement sqlParams = DaoUtils.updateById(entity, columns);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }


    /**
     * 通过主键批量更新实体
     *
     * @param mapper   实体对应mapper
     * @param entities 实体对象列表
     */
    public void batchUpdateById(Mapper mapper, List<?> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        if (entities.size() == 1) {
            updateById(mapper, entities.get(0));
        } else {
            List<PrepareStatement> sqlParamsList = entities.stream()
                    .map(DaoUtils::updateById).collect(Collectors.toList());
            PrepareStatement sqlParams = merge(sqlParamsList);
            doLog(sqlParams);
            mapper.update(sqlParams);
        }

    }

    /**
     * 通过主键批量更新实体
     *
     * @param mapper   实体对应mapper
     * @param entities 实体对象列表
     * @param columns  更新列
     */
    public void batchUpdateById(Mapper mapper, List<?> entities, List<Column> columns) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        if (entities.size() == 1) {
            updateById(mapper, entities.get(0), columns);
        } else {
            List<PrepareStatement> sqlParamsList = entities.stream()
                    .map(entity -> DaoUtils.updateById(entity, columns)).collect(Collectors.toList());
            PrepareStatement sqlParams = merge(sqlParamsList);
            doLog(sqlParams);
            mapper.update(sqlParams);
        }

    }

    /**
     * 通过主键更新非忽略的列
     *
     * @param mapper        实体对应mapper
     * @param entities      实体对象列表
     * @param ignoreColumns 忽略的列
     */
    public void batchUpdateByIdAndIgnoreColumns(Mapper mapper, List<?> entities, List<Column> ignoreColumns) {
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
    public int updateByIdAndIgnoreColumns(Mapper mapper, Object entity, List<Column> ignoreColumns) {
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
    public int updateByIdAndIgnoreColumns(Mapper mapper, Object entity, List<Column> ignoreColumns, Collection<?> ids) {
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
    public int updateByIds(Mapper mapper, Object entity, Collection<?> ids) {
        PrepareStatement sqlParams = DaoUtils.updateByIds(entity, ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
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
    public int updateByIds(Mapper mapper, Object entity, List<Column> columns, Collection<?> ids) {
        PrepareStatement sqlParams = DaoUtils.updateByIds(entity, columns, ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }


    /**
     * 通过主键更新实体
     *
     * @param mapper 实体对应mapper
     * @param entity 实体对象
     * @param ids    主键列表
     * @return 更新条数
     */
    public int updateByMultiIds(Mapper mapper, Object entity, List<? extends MultiId> ids) {
        PrepareStatement sqlParams = DaoUtils.updateByMultiIds(entity, ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
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
    public int updateByMultiIds(Mapper mapper, Object entity, List<Column> columns, List<? extends MultiId> ids) {
        PrepareStatement sqlParams = DaoUtils.updateByMultiIds(entity, columns, ids);
        doLog(sqlParams);
        return mapper.update(sqlParams);
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
    public <T> T getById(Mapper<T> mapper, Class<T> clazz, Object id) {
        PrepareStatement sqlParams = DaoUtils.getById(clazz, id);
        doLog(sqlParams);
        return executeQueryOne(mapper, sqlParams);
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
    public <T> List<T> getByIds(Mapper<T> mapper, Class<T> clazz, Collection<?> ids) {
        PrepareStatement sqlParams = DaoUtils.getByIds(clazz, ids);
        doLog(sqlParams);
        return executeQuery(mapper, sqlParams);
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
    public <T> T getByMultiId(Mapper<T> mapper, Class<T> clazz, MultiId id) {
        PrepareStatement sqlParams = DaoUtils.getByMultiId(clazz, id);
        doLog(sqlParams);
        return executeQueryOne(mapper, sqlParams);
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
    public <T> List<T> getByMultiIds(Mapper<T> mapper, Class<T> clazz, List<? extends MultiId> ids) {
        PrepareStatement sqlParams = DaoUtils.getByMultiIds(clazz, ids);
        doLog(sqlParams);
        return executeQuery(mapper, sqlParams);
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
    public <T, P> P getPoById(Mapper<T> mapper, Class<P> clazz, Object id) {
        PrepareStatement sqlParams = DaoUtils.getById(clazz, id);
        doLog(sqlParams);
        List<P> result = executeQuery(mapper, clazz, sqlParams);
        return result.isEmpty() ? null : result.get(0);
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
    public <T, P> List<P> getPoByIds(Mapper<T> mapper, Class<P> clazz, Collection<?> ids) {
        PrepareStatement sqlParams = DaoUtils.getByIds(clazz, ids);
        doLog(sqlParams);
        return executeQuery(mapper, clazz, sqlParams);
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
    public <T, P> P getPoByMultiId(Mapper<T> mapper, Class<P> clazz, MultiId id) {
        PrepareStatement sqlParams = DaoUtils.getByMultiId(clazz, id);
        doLog(sqlParams);
        return executeQueryOne(mapper, clazz, sqlParams);
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
    public <T, P> List<P> getPoByMultiIds(Mapper<T> mapper, Class<P> clazz, List<? extends MultiId> ids) {
        PrepareStatement sqlParams = DaoUtils.getByMultiIds(clazz, ids);
        doLog(sqlParams);
        return executeQuery(mapper, clazz, sqlParams);
    }

    /**
     * 根据SqlParams直接执行更新
     *
     * @param mapper    实体对应的mapper
     * @param sqlParams Sql参数对象
     * @return 更新条数
     */
    public int executeUpdate(Mapper mapper, PrepareStatement sqlParams) {
        doLog(sqlParams);
        return mapper.update(sqlParams);
    }


    /**
     * 执行查询获取指定持久化对象
     *
     * @param mapper    实体对应的mapper
     * @param poClass   持久化对象类
     * @param sqlParams Sql参数
     * @param <T>       实体类类型
     * @param <P>       持久化对象类类型
     * @return 持久化对象列表
     */
    @SuppressWarnings("unchecked")
    public <T, P> List<P> executeQuery(Mapper<T> mapper, Class<P> poClass, PrepareStatement sqlParams) {
        doLog(sqlParams);
        return EntityConverter.copyColumns(mapper.select(sqlParams), poClass);
    }


    /**
     * 执行查询
     *
     * @param mapper    实体对应的mapper
     * @param sqlParams Sql参数
     * @param <T>       实体类类型
     * @return 实体列表
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeQuery(Mapper<T> mapper, PrepareStatement sqlParams) {
        doLog(sqlParams);
        return mapper.select(sqlParams);
    }

    /**
     * 执行查询
     *
     * @param mapper    实体对应的mapper
     * @param sqlParams Sql参数
     * @param resultMap 指定resultMap
     * @param <P>       持久化对象类类型
     * @return 实体列表
     */
    @SuppressWarnings("unchecked")
    public <P> List<P> executeQuery(Mapper mapper, PrepareStatement sqlParams, String resultMap) {
        doLog(sqlParams);
        return (List<P>) mapper.selectWithResultMap(sqlParams, resultMap);
    }

    /**
     * 查找单个实体
     *
     * @param mapper    实体对应的mapper
     * @param sqlParams Sql参数
     * @param <T>       持久化对象类类型
     * @return 实体
     */
    public <T> T executeQueryOne(Mapper<T> mapper, PrepareStatement sqlParams) {
        List<T> result = executeQuery(mapper, sqlParams);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 查找单个实体
     *
     * @param mapper    实体对应的mapper
     * @param sqlParams Sql参数
     * @param resultMap 指定resultMap
     * @param <P>       持久化对象类类型
     * @return 实体
     */
    public <P> P executeQueryOne(Mapper mapper, PrepareStatement sqlParams, String resultMap) {
        List<P> result = executeQuery(mapper, sqlParams, resultMap);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 获取单个持久化对象
     *
     * @param mapper    实体对应的mapper
     * @param poClass   持久化对象类
     * @param sqlParams Sql参数
     * @param <T>       实体类类型
     * @param <P>       持久化对象类类型
     * @return 持久化对象
     */
    public <T, P> P executeQueryOne(Mapper<T> mapper, Class<P> poClass, PrepareStatement sqlParams) {
        List<P> result = executeQuery(mapper, poClass, sqlParams);
        return result.isEmpty() ? null : result.get(0);
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

        if (StringUtils.isBlank(keyword)
                || (CollectionUtils.isEmpty(searchColumns) && CollectionUtils.isEmpty(exactSearchColumns))) {
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
