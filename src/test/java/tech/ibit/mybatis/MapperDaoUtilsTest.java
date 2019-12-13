package tech.ibit.mybatis;

import org.apache.ibatis.annotations.Param;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tech.ibit.mybatis.test.entity.*;
import tech.ibit.mybatis.test.entity.property.OrganizationProperties;
import tech.ibit.mybatis.test.entity.property.UserProperties;
import tech.ibit.mybatis.test.CommonTest;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.exception.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 工具类测试
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
public class MapperDaoUtilsTest extends CommonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void insert() {
        User user = new User();
        user.setLoginId("dev");
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setPassword("12345678");
        user.setType(1);
        user.setName("dev");
        MapperDaoUtils.insert(new TestMapper() {
            @Override
            public int insertWithGenerateKeys(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams,
                                              @Param(SqlBuilder.KEY) KeyValuePair key) {
                assertParamsEquals(
                        "INSERT INTO user(login_id, name, email, password, mobile_phone, type) VALUES(?, ?, ?, ?, ?, ?)",
                        Arrays.asList("login_id", "dev", "name", "dev", "email", "dev@ibit.tech", "password", "12345678", "mobile_phone", "188", "type", 1),
                        sqlParams);
                key.setValue(1);
                return 1;
            }
        }, user);
        assertEquals(Integer.valueOf(1), user.getUserId());

        Organization organization = new Organization();
        organization.setCityCode("0001");
        organization.setName("广州市");
        organization.setType(1);
        organization.setPhone("188");
        MapperDaoUtils.insert(new TestMapper() {
            @Override
            public int insert(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "INSERT INTO organization(city_code, name, type, phone) VALUES(?, ?, ?, ?)",
                        Arrays.asList("city_code", "0001", "name", "广州市", "type", 1, "phone", "188"),
                        sqlParams);
                return 1;
            }
        }, organization);
    }

    @Test
    public void insert1() {
        User user = new User();
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        MapperDaoUtils.insert(new TestMapper(), user);
    }

    @Test
    public void insert2() {
        User user = new User();
        user.setLoginId("dev@ibit.tech");
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setType(1);
        user.setUserId(1);

        thrown.expect(IdAutoIncreaseException.class);
        thrown.expectMessage("Table(user)'s id(user_id) cannot be inserted!");
        MapperDaoUtils.insert(new TestMapper(), user);
    }

    @Test
    public void deleteById() {
        MapperDaoUtils.deleteById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Arrays.asList("user_id", 1),
                        sqlParams);
                return 1;
            }
        }, User.class, 1);
    }

    @Test
    public void deleteByIds() {
        MapperDaoUtils.deleteByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList("user_id", 1, "user_id", 2),
                        sqlParams);
                return 2;
            }
        }, User.class, Arrays.asList(1, 2));

        MapperDaoUtils.deleteByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Arrays.asList("user_id", 1),
                        sqlParams);
                return 1;
            }
        }, User.class, Collections.singletonList(1));
    }

    @Test
    public void deleteByMultiId() {
        UserKey uKey1 = new UserKey(1);

        MapperDaoUtils.deleteByMultiId(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Arrays.asList("user_id", 1),
                        sqlParams);
                return 1;
            }
        }, uKey1);

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        MapperDaoUtils.deleteByMultiId(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?)",
                        Arrays.asList("city_code", "001", "name", "001"),
                        sqlParams);
                return 1;
            }
        }, oKey1);
    }

    @Test
    public void deleteByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Arrays.asList("user_id", 1),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(uKey1));

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList("user_id", 1, "user_id", 2),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(uKey1, uKey2));


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?)",
                        Arrays.asList("city_code", "001", "name", "001"),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(oKey1));

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?) OR (city_code = ? AND name = ?)",
                        Arrays.asList("city_code", "001", "name", "001", "city_code", "001", "name", "002"),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(oKey1, oKey2));
    }


    @Test
    public void updateById() {
        User user = new User();
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setType(1);
        user.setUserId(1);

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.email", "dev@ibit.tech", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user);

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?",
                        Arrays.asList("u.login_id", null, "u.mobile_phone", "188", "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.mobilePhone));

        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        org.setName("广州");

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList("o.type", 1, "o.city_code", "0001", "o.name", "广州"),
                        sqlParams);
                return 1;
            }
        }, org);
    }


    @Test
    public void batchUpdateById() {
        User user = new User();
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setType(1);
        user.setUserId(1);

        MapperDaoUtils.batchUpdateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?;"
                                + "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.email", "dev@ibit.tech", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1,
                                "u.email", "dev@ibit.tech", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, Arrays.asList(user, user));

        MapperDaoUtils.batchUpdateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals("UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?;"
                                + "UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?",
                        Arrays.asList("u.login_id", null, "u.mobile_phone", "188", "u.user_id", 1,
                                "u.login_id", null, "u.mobile_phone", "188", "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, Arrays.asList(user, user), Arrays.asList(UserProperties.loginId, UserProperties.mobilePhone));
    }

    //测试id为null的情况
    @Test
    public void updateById2() {
        User user = new User();
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setType(1);

        thrown.expect(IdNullPointerException.class);
        thrown.expectMessage("Table(user)'s id(user_id) is null!");
        MapperDaoUtils.updateById(new TestMapper(), user);
    }

    //多主键，某个主键为null
    @Test
    public void updateById3() {
        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        thrown.expect(IdNullPointerException.class);
        thrown.expectMessage("Table(organization)'s id(name) is null!");
        MapperDaoUtils.updateById(new TestMapper(), org);
    }

    //某个不能为column设置为空
    @Test
    public void updateById4() {
        User user = new User();
        user.setUserId(1);

        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperDaoUtils.updateById(new TestMapper(), user, Collections.singletonList(UserProperties.password));
    }

    @Test
    public void updateByIdAndIgnoreColumns() {
        User user = getUser1();
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals("UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.login_id", "dev1", "u.name", "dev1", "u.email", "dev1@ibit.tech",
                                "u.password", "12345678", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user, Collections.emptyList());

        user.setLoginId(null);
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.login_id", null, "u.name", "dev1", "u.email", "dev1@ibit.tech", "u.password", "12345678",
                                "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user, Collections.emptyList());


        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.name = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.name", "dev1", "u.password", "12345678", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.email));

        user.setUserId(null);
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.name = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id IN(?, ?)",
                        Arrays.asList("u.name", "dev1", "u.password", "12345678", "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1, "u.user_id", 2),
                        sqlParams);
                return 2;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.email), Arrays.asList(1, 2));


        user = getUser1();
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id IN(?, ?)",
                        Arrays.asList("u.login_id", "dev1", "u.name", "dev1", "u.email", "dev1@ibit.tech", "u.password", "12345678",
                                "u.mobile_phone", "188", "u.type", 1, "u.user_id", 1, "u.user_id", 2),
                        sqlParams);
                return 2;
            }
        }, user, Collections.emptyList(), Arrays.asList(1, 2));

    }

    @Test
    public void updateByIds() {

        User user = new User();
        user.setType(1);
        user.setPassword("12345678");

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.password", "12345678", "u.type", 1, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(1, 2, 3));

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.password", "12345678", "u.type", 1, "u.user_id", 1),
                        sqlParams
                );
                return 1;
            }
        }, user, Collections.singletonList(1));

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.password", "12345678", "u.type", 1, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3), 
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(1, 2, 3));

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.type", 1, "u.password", "12345678", "u.user_id", 1, "u.user_id", 2, "u.user_id", 3), 
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(1, 2, 3));

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.type", 1, "u.password", "12345678", "u.login_id", null, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(1, 2, 3));
    }

    @Test
    public void updateByIds1() {
        User user = new User();
        user.setType(1);
        thrown.expect(IdValueNotFoundException.class);
        thrown.expectMessage("Id value not found");
        MapperDaoUtils.updateByIds(new TestMapper(), user, Collections.emptyList());
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByIds2() {
        User user = new User();
        user.setType(1);
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperDaoUtils.updateByIds(new TestMapper(), user, Collections.singletonList(UserProperties.password), Arrays.asList(1, 2));
    }

    @Test
    public void updateByMultiIds() {
        User user = new User();
        user.setType(1);
        user.setPassword("12345678");

        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        UserKey uKey3 = new UserKey(3);

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.password", "12345678", "u.type", 1, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(uKey1, uKey2, uKey3));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList("u.password", "12345678", "u.type", 1, "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, user, Collections.singletonList(uKey1));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.type", 1, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Collections.singletonList(UserProperties.type), Arrays.asList(uKey1, uKey2, uKey3));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.type", 1, "u.password", "12345678", "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(uKey1, uKey2, uKey3));


        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList("u.type", 1, "u.password", "12345678", "u.login_id", null, "u.user_id", 1, "u.user_id", 2, "u.user_id", 3),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(uKey1, uKey2, uKey3));

        Organization org = new Organization();
        org.setType(1);

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        OrganizationKey oKey3 = new OrganizationKey("0003", "中山市");

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.type", 1, "o.city_code", "0001", "o.name", "广州市"),
                        sqlParams);
                return 1;
            }
        }, org, Collections.singletonList(oKey1));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.type", 1, "o.city_code", "0001", "o.name", "广州市",
                                "o.city_code", "0002", "o.name", "深圳市", "o.city_code", "0003", "o.name", "中山市"),
                        sqlParams);
                return 3;
            }
        }, org, Arrays.asList(oKey1, oKey2, oKey3));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.type", 1, "o.city_code", "0001", "o.name", "广州市"),
                        sqlParams
                );
                return 1;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Collections.singletonList(oKey1));


        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.phone", null, "o.type", 1, "o.city_code", "0001", "o.name", "广州市"),
                        sqlParams);
                return 1;
            }
        }, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type), Collections.singletonList(oKey1));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.type", 1, "o.city_code", "0001", "o.name", "广州市",
                                "o.city_code", "0002", "o.name", "深圳市", "o.city_code", "0003", "o.name", "中山市"),
                        sqlParams);
                return 3;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2, oKey3));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?)"
                                + " OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList("o.phone", null, "o.type", 1, "o.city_code", "0001", "o.name", "广州市",
                                "o.city_code", "0002", "o.name", "深圳市", "o.city_code", "0003", "o.name", "中山市"),
                        sqlParams);
                return 3;
            }
        }, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type), Arrays.asList(oKey1, oKey2, oKey3));
    }

    //测试没有id值的情况
    @Test
    public void updateByMultiIds1() {
        Organization org = new Organization();
        org.setType(1);
        thrown.expect(IdValueNotFoundException.class);
        thrown.expectMessage("Id value not found");
        MapperDaoUtils.updateByMultiIds(new TestMapper(), org, Collections.emptyList());
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByMultiIds2() {
        Organization org = new Organization();

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        MapperDaoUtils.updateByMultiIds(new TestMapper(), org, Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2));
    }

    @Test
    public void getById() {
        MapperDaoUtils.getById(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.singletonList(getUser1());
            }
        }, User.class, 1);
    }

    @Test
    public void getByIds() {
        MapperDaoUtils.getByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?, ?) LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "u.user_id", 2, "u.user_id", 3, "$start", 0, "$limit", 3),
                        sqlParams);
                return Arrays.asList(getUser1(), getUser2());
            }
        }, User.class, Arrays.asList(1, 2, 3));

        MapperDaoUtils.getByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.singletonList(getUser1());
            }
        }, User.class, Collections.singletonList(1));
    }

    @Test
    public void getByMultiId() {
        UserKey uKey1 = new UserKey(1);
        MapperDaoUtils.getByMultiId(new TestMapper<User>() {
            @Override
            public List select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.singletonList(getUser1());
            }
        }, User.class, uKey1);


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        MapperDaoUtils.getByMultiId(new TestMapper<Organization>() {
            @Override
            public List select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o WHERE (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                        Arrays.asList("o.city_code", "001", "o.name", "001", "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Organization.class, oKey1);
    }

    @Test
    public void getByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperDaoUtils.getByMultiIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "u.user_id", 2, "$start", 0, "$limit", 2), 
                        sqlParams);
                return Collections.emptyList();
            }
        }, User.class, Arrays.asList(uKey1, uKey2));

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");
        MapperDaoUtils.getByMultiIds(new TestMapper<Organization>() {
            @Override
            public List<Organization> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                        Arrays.asList("o.city_code", "001", "o.name", "001", "o.city_code", "001", "o.name", "002", "$start", 0, "$limit", 2),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Organization.class, Arrays.asList(oKey1, oKey2));

    }

    @Test
    public void getPOById() {
        MapperDaoUtils.getPoById(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1), 
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, 1);
    }

    @Test
    public void getPOByIds() {
        MapperDaoUtils.getPoByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?, ?) LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "u.user_id", 2, "u.user_id", 3, "$start", 0, "$limit", 3),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Arrays.asList(1, 2, 3));

        MapperDaoUtils.getPoByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Collections.singletonList(1));
    }

    @Test
    public void getPoByMultiId() {
        UserKey uKey1 = new UserKey(1);
        MapperDaoUtils.getPoByMultiId(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "$start", 0, "$limit", 1),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, uKey1);
    }

    @Test
    public void getPoByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperDaoUtils.getPoByMultiIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                        Arrays.asList("u.user_id", 1, "u.user_id", 2, "$start", 0, "$limit", 2),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Arrays.asList(uKey1, uKey2));
    }

    @Test
    public void executeUpdate() {
        Sql sql = new Sql().update(UserProperties.TABLE)
                .set(new ColumnValue(UserProperties.name, "小D"))
                .andWhere(CriteriaItemMaker.equalsTo(UserProperties.userId, 1));
        MapperDaoUtils.executeUpdate(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "UPDATE user u SET u.name = ? WHERE u.user_id = ?",
                        Arrays.asList("u.name", "小D", "u.user_id", 1),
                        sqlParams);
                return 1;
            }
        }, sql.getSqlParams());
    }

    @Test
    public void executeQuery() {
        Sql sql = new Sql()
                .selectPo(User.class)
                .from(UserProperties.TABLE)
                .orderBy(new OrderBy(UserProperties.userId, true))
                .limit(10);
        List<User> users = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u ORDER BY u.user_id DESC LIMIT ?, ?",
                        Arrays.asList("$start", 0, "$limit", 10),
                        sqlParams
                );
                return Arrays.asList(getUser2(), getUser1());
            }

        }, sql.getSqlParams());
        assertEquals(2, users.size());
        assertUser(getUser2(), users.get(0));
        assertUser(getUser1(), users.get(1));
    }

    @Test
    public void executeQuery1() {
        Sql sql = new Sql()
                .selectPo(UserPo.class)
                .from(UserProperties.TABLE)
                .orderBy(new OrderBy(UserProperties.userId, true))
                .limit(10);
        List<UserPo> users = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals("SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u ORDER BY u.user_id DESC LIMIT ?, ?",
                        Arrays.asList("$start", 0, "$limit", 10),
                        sqlParams);
                return Arrays.asList(getUser2(), getUser1());
            }

        }, UserPo.class, sql.getSqlParams());
        assertEquals(2, users.size());
        assertUserPo(EntityConverter.copyColumns(getUser2(), UserPo.class), users.get(0));
        assertUserPo(EntityConverter.copyColumns(getUser1(), UserPo.class), users.get(1));
    }

    @Test
    public void executeQuery2() {
        Sql sql = new Sql()
                .selectPo(User.class)
                .from(UserProperties.TABLE)
                .andWhere(CriteriaItemMaker.equalsTo(UserProperties.type, 1))
                .limit(2);
        TotalList totalList = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.type = ? LIMIT ?, ?",
                        Arrays.asList("u.type", 1, "$start", 0, "$limit", 2),
                        sqlParams
                );
                return Arrays.asList(getUser1(), getUser2());
            }

            @Override
            public int count(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT COUNT(*) FROM user u WHERE u.type = ?",
                        Arrays.asList("u.type", 1),
                        sqlParams);
                return 2;
            }
        }, sql);
        assertEquals(new Integer(2), totalList.getTotal());
        assertUser(getUser1(), (User) totalList.getList().get(0));
        assertUser(getUser2(), (User) totalList.getList().get(1));
    }

    @Test
    public void executeQuery3() {
        Sql sql = new Sql()
                .selectPo(UserPo.class)
                .from(UserProperties.TABLE)
                .andWhere(CriteriaItemMaker.equalsTo(UserProperties.type, 1))
                .limit(2);
        TotalList<UserPo> totalList = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u WHERE u.type = ? LIMIT ?, ?",
                        Arrays.asList("u.type", 1, "$start", 0, "$limit", 2),
                        sqlParams);
                return Arrays.asList(getUser1(), getUser2());
            }

            @Override
            public int count(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                assertParamsEquals(
                        "SELECT COUNT(*) FROM user u WHERE u.type = ?",
                        Arrays.asList("u.type", 1), 
                        sqlParams);
                return 2;
            }
        }, UserPo.class, sql);
        assertEquals(new Integer(2), totalList.getTotal());
        assertUserPo(EntityConverter.copyColumns(getUser1(), UserPo.class), totalList.getList().get(0));
        assertUserPo(EntityConverter.copyColumns(getUser2(), UserPo.class), totalList.getList().get(1));
    }

    @Test
    public void executeQueryOne() {
        Sql sql = new Sql()
                .selectPo(User.class)
                .from(UserProperties.TABLE)
                .andWhere(CriteriaItemMaker.equalsTo(UserProperties.userId, 1))
                .orderBy(new OrderBy(UserProperties.userId))
                .limit(1);
        User user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                return Collections.emptyList();
            }
        }, sql.getSqlParams());
        assertNull(user);

        user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                return Collections.singletonList(getUser1());
            }
        }, sql.getSqlParams());
        assertUser(getUser1(), user);
    }

    @Test
    public void executeQueryOne1() {
        Sql sql = new Sql()
                .selectPo(User.class)
                .from(UserProperties.TABLE)
                .andWhere(CriteriaItemMaker.equalsTo(UserProperties.userId, 1))
                .orderBy(new OrderBy(UserProperties.userId))
                .limit(1);
        UserPo user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                return Collections.emptyList();
            }
        }, UserPo.class, sql.getSqlParams());
        assertNull(user);

        user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) SqlParams sqlParams) {
                return Collections.singletonList(getUser1());
            }
        }, UserPo.class, sql.getSqlParams());
        assertUserPo(EntityConverter.copyColumns(getUser1(), UserPo.class), user);

    }

    @Test
    public void addKeywords() {
        Sql sql = new Sql()
                .select(Arrays.asList(UserProperties.userId, UserProperties.name, UserProperties.email))
                .from(UserProperties.TABLE);
        MapperDaoUtils.addKeywords(sql, "dev", Arrays.asList(UserProperties.name, UserProperties.email));
        assertParamsEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email LIKE ?)",
                Arrays.asList("u.name", "%dev%", "u.email", "%dev%"),
                sql.getSqlParams());

        sql = new Sql()
                .select(Arrays.asList(UserProperties.userId, UserProperties.name, UserProperties.email))
                .from(UserProperties.TABLE);
        MapperDaoUtils.addKeywords(sql, "_dev", Arrays.asList(UserProperties.name, UserProperties.email));
        assertParamsEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email LIKE ?)",
                Arrays.asList("u.name", "%\\_dev%", "u.email", "%\\_dev%"),
                sql.getSqlParams());
    }

    @Test
    public void addExactKeywords() {
        Sql sql = new Sql()
                .select(Arrays.asList(UserProperties.userId, UserProperties.name, UserProperties.email))
                .from(UserProperties.TABLE);
        MapperDaoUtils.addExactKeywords(sql, "dev"
                , Collections.singletonList(UserProperties.name), Collections.singletonList(UserProperties.email));
        assertParamsEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email = ?)",
                Arrays.asList("u.name", "%dev%", "u.email", "dev"),
                sql.getSqlParams());

        sql = new Sql()
                .select(Arrays.asList(UserProperties.userId, UserProperties.name, UserProperties.email))
                .from(UserProperties.TABLE);
        MapperDaoUtils.addExactKeywords(sql, "_dev"
                , Collections.singletonList(UserProperties.name), Collections.singletonList(UserProperties.email));
        assertParamsEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email = ?)",
                Arrays.asList("u.name", "%\\_dev%", "u.email", "\\_dev"),
                sql.getSqlParams());
    }

    @Test
    public void getKeyword() {

        String keyword = MapperDaoUtils.getKeyword("小D");
        assertEquals("%小D%", keyword);

        keyword = MapperDaoUtils.getKeyword("小D_");
        assertEquals("%小D\\_%", keyword);

        keyword = MapperDaoUtils.getKeyword("小D%");
        assertEquals("%小D\\%%", keyword);

    }


    @Test
    public void getExactKeyword() {

        String keyword = MapperDaoUtils.getExactKeyWord("小D");
        assertEquals("小D", keyword);

        keyword = MapperDaoUtils.getExactKeyWord("小D_");
        assertEquals("小D\\_", keyword);

        keyword = MapperDaoUtils.getExactKeyWord("小D%");
        assertEquals("小D\\%", keyword);

    }

    private void assertUser(User expectedUser, User user) {
        assertNotNull(user);
        assertEquals(expectedUser.getUserId(), user.getUserId());
        assertEquals(expectedUser.getLoginId(), user.getLoginId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getPassword(), user.getPassword());
        assertEquals(expectedUser.getMobilePhone(), user.getMobilePhone());
        assertEquals(expectedUser.getType(), user.getType());
    }

    private void assertUserPo(UserPo expectedUserPo, UserPo actualUserPo) {
        assertNotNull(actualUserPo);
        assertEquals(expectedUserPo.getUserId(), actualUserPo.getUserId());
        assertEquals(expectedUserPo.getLoginId(), actualUserPo.getLoginId());
        assertEquals(expectedUserPo.getEmail(), actualUserPo.getEmail());
        assertEquals(expectedUserPo.getMobilePhone(), actualUserPo.getMobilePhone());
        assertEquals(expectedUserPo.getType(), actualUserPo.getType());
    }

    private User getUser1() {
        User user = new User();
        user.setUserId(1);
        user.setLoginId("dev1");
        user.setName("dev1");
        user.setEmail("dev1@ibit.tech");
        user.setPassword("12345678");
        user.setMobilePhone("188");
        user.setType(1);
        return user;
    }

    private User getUser2() {
        User user = new User();
        user.setUserId(2);
        user.setLoginId("dev2");
        user.setName("dev2");
        user.setEmail("dev2@ibit.tech");
        user.setPassword("12345678");
        user.setMobilePhone("100");
        user.setType(1);
        return user;
    }

}