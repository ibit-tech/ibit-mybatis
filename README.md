# ibit-mybatis 2.x 介绍

## 概述

&nbsp;&nbsp; ibit-mybatis 是一个 Mybatis 的增强工具，在 Mybatis 的基础上增加了新的特性与功能，志在简化开发流程、提高开发效率。</br>

### 特性

* 无侵入，引入”ibit-mybatis”对现有工程不会产生影响。
* 无 xml 配置，基于注解的方式实现
* 灵活的CRUD（增、删、改、查）操作，Mapper，支持常用的单表CRUD操作，更有强大的SQL构造器（[sql-builder](https://github.com/ibit-tech/sql-builder)），满足更为复杂的操作（如聚合函数、分组、连表、分页），为了让sql-builder更好的支持 ibit-mybatis，从 ibit-mybatis 2.0 开始，sql-builder 合并到 ibit-mybatis 中。
* 内置代码生成器（[ibit-mybatis-generator](https://github.com/ibit-tech/ibit-mybatis-generator)），指定数据库表，自动生成Mapper（无主键、单主键和多主键 Mapper）、Entity、Properties等基础类，减少重复或者相似代码编写。
* 扩展支持，数据脱敏（后续支持）


### sql-builder描述

`sql-builder`定义动态SQL的生成规则，用来实现单表的CRUD操作。

#### 核心 sql 接口

详细 api 文档参考：[ibit-mybatis 2.x API 文档](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) 

| 说明 | 接口 | 
| --- | --- |
| 搜索 | [QuerySql](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) |
| 计数 | [CountSql](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) |
| 删除 | [DeleteSql](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) |
| 插入 | [InsertSql](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) |
| 更新 | [UpdateSql](https://ibit.tech/apidocs/ibit-mybatis/2.x/index.html) |

#### sql 接口支持

不同类型的 sql， 其语句的约束不一样，下表列举所有的语句支持。

| 接口 | 支持方法 | 说明 |
| --- | --- | --- |
| ColumnSupport | column </br> columnPo | `SELECT column1[, column2...]` 语句|
| DeleteSupport | delete | `DELETE t1.*` 语句|
| DistinctSupport | distinct | `DISTINCT` 语句 |
| FromSupport | from | `FROM table1 t1[, table2 t2...]` 语句|
| GroupBySupport | groupBy | `GROUP BY t1.column1[, t2.column2, ...]`语句| 
| HavingSupport | having </br> andHaving </br> orHaving | `HAVING`语句|
| InsertTableSupport | insert | `INSERT INTO table1 t1` 语句, t1表示 "表别名" |
| JoinOnSupport | joinOn </br> leftJoinOn </br> rightJoinOn </br> fullJoinOn </br> innerJoinOn </br>complexLeftJoinOn </br> complexRightJoinOn </br> complexFullJoinOn </br> complexInnerJoinOn | `[LEFT\|RIGHT\|FULL\|INNER] JOIN ON`语句 |
| LimitSupport | limit | `LIMIT #{start}, #{limit}`语句 |
| OrderBySupport | orderBy | `ORDER BY` 语句 |
| SetSupport | set | SET 条件语句 |
| UpdateTableSupport | update | `UPDATE table1 t1[, table2 t2...]`语句，t1，t2表示"表别名" |
| ValuesSupport | values | `(column1, column2, ...) VALUES(?, ?, ...)`语句 |
| WhereSupport | where </br> andWhere </br> orWhere | `WHERE` 语句 |
| OnDuplicateKeyUpdateSupport | onDuplicateKeyUpdate | `ON DUPLICATE KEY UPDATE column1 = ? [, column2= ?...]` 语句 | 
| IColumnFullTextSupport | fullText </br> fullTextMatch | MATCH(column1 [, column2, ...]) AGAINST(? [IN (BOOLEAN \| NATURAL LANGUAGE) MODE]) |

#### sql 工厂类

工厂类：`tech.ibit.mybatis.sqlbuilder.SqlFactory`，一般不直接使用，继承 `RawMapper` 的 Mapper 可直接创建 `QuerySql`、`CountSql`、`DeleteSql`、`InsertSql` 和 `UpdateSql` 对应实例。

### Mapper 说明

#### Mapper 基础支持
`ibit-mybatis` 定义了 5 种 Mapper，分别是 `RawMapper`，`Mapper`， `NoIdMapper`，`SingleIdMapper`，`MultipleIdMapper`。以下分别说明。
  
| Mapper 类型 | 父接口 | 说明 |
| --- | --- | --- |
| RawMapper | / | 基于注解方式增、删、改、查|
| Mapper | RawMapper | 在 RawMapper 增加 Sql 实例创建，unique key 查询默认方法 |
| NoIdMapper | Mapper |  扩展无主键表的增 |
| SingleIdMapper | Mapper | 扩展单主键表的根据id增、删、改、查 |
| MultipleIdMapper | Mapper | 扩展多主键表的根据id增、删、改、查 |
  
  
  使用 [ibit-mybatis-generator](https://github.com/ibit-tech/ibit-mybatis-generator) 2.x 版本，会根据表主键数量，继承不同的 Mapper。
  
#### Mapper 结合 Sql 自定义增、删、改、查

| Mapper 创建 Sql 实例方法 | 实例类型 | 实例执行方法说明 | 
| --- | --- | --- |
| createQuery | QuerySql | executeQueryPage：查询（包含分页信息）</br> executeQuery：查询列表 </br> executeQueryOne：查询单条 </br> executeQueryDefaultPage：查询基本类型（包含分页信息）</br> executeQueryDefault：查询基本类型|
| createCount | CountSql | executeCount：计数 |
| createDelete | DeleteSql | executeDelete：执行删除 |
| createInsert | InsertSql | executeInsert：执行插入 </br> executeInsertWithGenerateKeys：执行插入并生成主键 |
| createUpdate | UpdateSql | executeUpdate：执行更新|

自定义查询例子：

```
public User getByUsername(String username) {
    if (StringUtils.isBlank(username)) {
        return null;
    }
    return mapper
            .createQuery()
            .columnPo(User.class)
            .from(UserProperties.TABLE)
            .andWhere(UserProperties.username.eq(username))
            .limit(1)
            .executeQueryOne();
}

// 如果 mapper 的实体类型为User，则可以简化为以下方式

public User getByUsername(String username) {
    if (StringUtils.isBlank(username)) {
        return null;
    }
    return mapper
            .createQuery()
            .andWhere(UserProperties.username.eq(username))
            .limit(1)
            .executeQueryOne();
}

```

## 用法

### 相关引用

#### Gradle

```
compile 'tech.ibit:ibit-mybatis:${lastest}'
```

#### Maven

```
<dependency>
  <groupId>tech.ibit</groupId>
  <artifactId>ibit-mybatis</artifactId>
  <version>${latest}</version>
</dependency>
```

**说明**: 将 "${latest}" 替换成 `2.0` 以上版本。

### 配置说明

需要将 Mybatis Configuration 的 `mapUnderscoreToCamelCase` 的值设置为 true。

**方式1**：使用 mybatis-config.xml

```
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>    
```

**方式2**：java 代码方式

```
Configuration configuration = new Configuration(environment);
configuration.setMapUnderscoreToCamelCase(true);
```

**方式3**：使用了 `mybatis-spring-boot-starter`，修改配置如下

```
# 字段映射驼峰
mybatis.configuration.map-underscore-to-camel-case=true
```

### 其他说明

#### 指定 CommonEnumTYpeHandler

`ibit-mybatis` 定义了枚举类型（`CommonEnum`，枚举-Integer转换），其`TypeHandler`为 `CommonEnumTypeHandler`。

如果使用 `CommonEnum` 作为系统通用枚举类，则需要做以下改造。 

**a.** 新的枚举需要实现`CommonEnum#getValue`方法。

**b.** SqlProvider 需要做配置

```
SqlProvider.setValueFormatter(new LinkedHashMap<Class, Function<Object, Object>>() {{
    put(tech.ibit.mybatis.CommonEnum.class, o -> ((tech.ibit.mybatis.CommonEnum) o).getValue());
}});
```

**c.** 修改默认的枚举 TypeHandler

**方式1**：配置mybatis-config.xml

```
<configuration>
    <settings>
        <setting name="defaultEnumTypeHandler" value="tech.ibit.mybatis.CommonEnumTypeHandler"/>
    </settings>
</configuration>    
```

**方式2**：java 代码方式

```
Configuration configuration = new Configuration(environment);
configuration.setDefaultEnumTypeHandler(tech.ibit.mybatis.CommonEnumTypeHandler.class);
```

**方式3**：使用了 `mybatis-spring-boot-starter`，修改配置如下

```
# 指定默认的枚举处理类
mybatis.configuration.default-enum-type-handler=tech.ibit.mybatis.CommonEnumTypeHandler
```

### 版本升级说明

#### ibit\-mybatis 从 2.0 升级到 2.1+ 需要做的事情，针对生成的 `mapper`

继承 `SingleIdMapper`、`MultipleIdMapper` 和 `NoIdMapper` 的接口，需要重载以下方法

```
/**
 * 获取默认的表对象
 *
 * @return 表对象
 */
@Override
default Table getDefaultTable() {
    return %propertyName%.TABLE;
}

```

继承`SingleIdMapper`需要再重载以下方法：
```

/**
 * 获取主键列
 *
 * @return 主键列
 */
@Override
default Column getId() {
    return %propertyName%.%idColumn%;
}
```

最简单的方式，就是拿 ibit-mybatis-generator 2.1 重新生成一下 mapper 即可。

#### 升级到2.6+，需要增加拦截类`ResultMapInterceptor`，动态指定`ResultType`

tech.ibit.mybatis.plugin.ResultMapInterceptor

**相关用例**：

```
/**
 * 按照用户类型统计数量
 *
 * @return 用户类型统计
 */
@Override
public List<UserTypeTotal> listTypeTotals() {
    return mapper.createQuery()
            .column(UserProperties.type)
            .column(UserProperties.type.count("total"))
            .groupBy(UserProperties.type)
            .orderBy(UserProperties.type.orderBy())
            .executeQuery(UserTypeTotal.class);
}
```

**方式1**：配置mybatis-config.xml

```
<plugins>
  <plugin interceptor="org.mybatis.example.ExamplePlugin">
  </plugin>
</plugins>

```

**方式2**：配置Bean

```
@Bean
public ResultMapInterceptor getResultMapInterceptor() {
    return new ResultMapInterceptor();
}
```

### 新功能介绍

#### 2.6 新增mysql全文搜索

```
/**
 * 给出搜索关键字，查找名称关联度
 *
 * @param keyword 查询关键字
 * @return 名称关联度
 */
public List<UserNameScoreDto> listUserNameScores(String keyword) {
    if (StringUtils.isBlank(keyword)) {
        return Collections.emptyList();
    }
    FullTextColumn score = UserProperties.userName.fullText(new IColumn[]{UserProperties.nickName}, keyword, "score");
    return mapper.createQuery()
            .column(Arrays.asList(
                    UserProperties.userId,
                    UserProperties.nickName,
                    UserProperties.userName,
                    score))
            .andHaving(score.gt(0))
            .orderBy(score.orderBy(true))
            .executeQuery(UserNameScoreDto.class);
}

/**
 * 给出关键字，全文搜索用户名或者昵称
 *
 * @param keyword 查询关键字
 * @return 用户名列表
 */
public List<UserNameDto> listUsers(String keyword) {
    if (StringUtils.isBlank(keyword)) {
        return Collections.emptyList();
    }
    return mapper.createQuery()
            .column(Arrays.asList(
                    UserProperties.userId,
                    UserProperties.userName,
                    UserProperties.nickName
            ))
            .andWhere(UserProperties.userName.fullTextMatch(new IColumn[]{UserProperties.nickName}, keyword))
            .executeQuery(UserNameDto.class);
}
```

## 公众号

喜欢我的文章，请关注公众号

![iBit程序猿](https://x-halo.oss-cn-beijing.aliyuncs.com/halo/wechat_1589272852710.jpg)




