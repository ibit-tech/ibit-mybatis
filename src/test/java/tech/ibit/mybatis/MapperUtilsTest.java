package tech.ibit.mybatis;

import org.apache.ibatis.annotations.Param;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tech.ibit.mybatis.demo.entity.*;
import tech.ibit.mybatis.demo.entity.property.OrganizationProperties;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SqlFactory;
import tech.ibit.mybatis.sqlbuilder.UniqueKey;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;
import tech.ibit.mybatis.sqlbuilder.sql.QuerySql;
import tech.ibit.mybatis.utils.MapperUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 工具类测试
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
public class MapperUtilsTest extends CommonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void rawInsert() {
        User user = new User();
        user.setLoginId("dev");
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setPassword("12345678");
        user.setType(UserType.u1);
        user.setName("dev");
        MapperUtils.insert(new UserTestMapper() {
            @Override
            public int rawInsertWithGenerateKeys(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams,
                                                 @Param(SqlProvider.PARAM_KEY) KeyValuePair key) {
                assertPrepareStatementEquals(
                        "INSERT INTO user(login_id, name, email, password, mobile_phone, type) VALUES(?, ?, ?, ?, ?, ?)",
                        Arrays.asList(
                                UserProperties.loginId.value("dev"),
                                UserProperties.name.value(("dev")),
                                UserProperties.email.value("dev@ibit.tech"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1)
                        ),
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
        MapperUtils.insert(new OrganizationTestMapper() {
            @Override
            public int rawInsert(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "INSERT INTO organization(city_code, name, type, phone) VALUES(?, ?, ?, ?)",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.phone.value("188")
                        ),
                        sqlParams);
                return 1;
            }
        }, organization);
    }

    @Test
    public void insert1() {
        User user = new User();
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        MapperUtils.insert(new UserTestMapper(), user);
    }

    @Test
    public void insert2() {
        User user = new User();
        user.setLoginId("dev@ibit.tech");
        user.setEmail("dev@ibit.tech");
        user.setMobilePhone("188");
        user.setType(UserType.u1);
        user.setUserId(1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s id(user_id) cannot be inserted!");
        MapperUtils.insert(new UserTestMapper(), user);
    }

    @Test
    public void deleteById() {
        MapperUtils.deleteById(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, 1);
    }

    @Test
    public void deleteByIds() {
        MapperUtils.deleteByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(1, 2));

        MapperUtils.deleteByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(1));
    }

    @Test
    public void deleteByMultiId() {
        UserKey uKey1 = new UserKey(1);

        MapperUtils.deleteByMultiId(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, uKey1);

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        MapperUtils.deleteByMultiId(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE city_code = ? AND name = ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001")
                        ),
                        sqlParams);
                return 1;
            }
        }, oKey1);
    }

    @Test
    public void deleteByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperUtils.deleteByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(uKey1));

        MapperUtils.deleteByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(uKey1, uKey2));


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");

        MapperUtils.deleteByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE city_code = ? AND name = ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001")
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(oKey1));

        MapperUtils.deleteByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?) OR (city_code = ? AND name = ?)",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("002")
                        ),
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
        user.setType(UserType.u1);
        user.setUserId(1);

        MapperUtils.updateById(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.email.value("dev@ibit.tech"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user);

        MapperUtils.updateById(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.loginId.value(null),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.mobilePhone));

        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        org.setName("广州");

        MapperUtils.updateById(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州")
                        ),
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
        user.setType(UserType.u1);
        user.setUserId(1);

        MapperUtils.batchUpdateById(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?;"
                                + "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.email.value("dev@ibit.tech"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.email.value("dev@ibit.tech"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, Arrays.asList(user, user));

        MapperUtils.batchUpdateById(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals("UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?;"
                                + "UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.loginId.value(null),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.userId.value(1),
                                UserProperties.loginId.value(null),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.userId.value(1)
                        ),
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
        user.setType(UserType.u1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s id(user_id) is null!");
        MapperUtils.updateById(new UserTestMapper(), user);
    }

    //多主键，某个主键为null
    @Test
    public void updateById3() {
        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s id(name) is null!");
        MapperUtils.updateById(new OrganizationTestMapper(), org);
    }

    //某个不能为column设置为空
    @Test
    public void updateById4() {
        User user = new User();
        user.setUserId(1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperUtils.updateById(new UserTestMapper(), user, Collections.singletonList(UserProperties.password));
    }

    @Test
    public void updateByIdAndIgnoreColumns() {
        User user = getUser1();
        MapperUtils.updateByIdAndIgnoreColumns(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals("UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?"
                                + ", u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.loginId.value("dev1"),
                                UserProperties.name.value("dev1"),
                                UserProperties.email.value("dev1@ibit.tech"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Collections.emptyList());

        user.setLoginId(null);
        MapperUtils.updateByIdAndIgnoreColumns(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?, "
                                + "u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.loginId.value(null),
                                UserProperties.name.value("dev1"),
                                UserProperties.email.value("dev1@ibit.tech"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Collections.emptyList());


        MapperUtils.updateByIdAndIgnoreColumns(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.name = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.name.value("dev1"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.email));

        user.setUserId(null);
        MapperUtils.updateByIdAndIgnoreColumns(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.name = ?, u.password = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.name.value("dev1"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, user, Arrays.asList(UserProperties.loginId, UserProperties.email), Arrays.asList(1, 2));


        user = getUser1();
        MapperUtils.updateByIdAndIgnoreColumns(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.login_id = ?, u.name = ?, u.email = ?, u.password = ?, "
                                + "u.mobile_phone = ?, u.type = ? WHERE u.user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.loginId.value("dev1"),
                                UserProperties.name.value("dev1"),
                                UserProperties.email.value("dev1@ibit.tech"),
                                UserProperties.password.value("12345678"),
                                UserProperties.mobilePhone.value("188"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, user, Collections.emptyList(), Arrays.asList(1, 2));

    }

    @Test
    public void updateByIds() {

        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        MapperUtils.updateByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(1, 2, 3));

        MapperUtils.updateByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams
                );
                return 1;
            }
        }, user, Collections.singletonList(1));

        MapperUtils.updateByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(1, 2, 3));

        MapperUtils.updateByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(1, 2, 3));

        MapperUtils.updateByIds(new UserTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.loginId.value(null),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(1, 2, 3));
    }

    @Test
    public void updateByIds1() {
        User user = new User();
        user.setType(UserType.u1);
        assertEquals(0, MapperUtils.updateByIds(new UserTestMapper(), user, Collections.emptyList()));
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByIds2() {
        User user = new User();
        user.setType(UserType.u1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperUtils.updateByIds(new UserTestMapper(), user, Collections.singletonList(UserProperties.password), Arrays.asList(1, 2));
    }

    @Test
    public void updateByMultiIds() {
        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        UserKey uKey3 = new UserKey(3);

        MapperUtils.updateByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(uKey1, uKey2, uKey3));

        MapperUtils.updateByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Collections.singletonList(uKey1));

        MapperUtils.updateByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Collections.singletonList(UserProperties.type), Arrays.asList(uKey1, uKey2, uKey3));

        MapperUtils.updateByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(uKey1, uKey2, uKey3));


        MapperUtils.updateByMultiIds(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.loginId.value(null),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(uKey1, uKey2, uKey3));

        Organization org = new Organization();
        org.setType(1);

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        OrganizationKey oKey3 = new OrganizationKey("0003", "中山市");

        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams);
                return 1;
            }
        }, org, Collections.singletonList(oKey1));

        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
                        sqlParams);
                return 3;
            }
        }, org, Arrays.asList(oKey1, oKey2, oKey3));

        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams
                );
                return 1;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Collections.singletonList(oKey1));


        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.phone.value(null),
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams);
                return 1;
            }
        }, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type), Collections.singletonList(oKey1));

        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
                        sqlParams);
                return 3;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2, oKey3));

        MapperUtils.updateByMultiIds(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?)"
                                + " OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.phone.value(null),
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
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
        assertEquals(0, MapperUtils.updateByMultiIds(new OrganizationTestMapper(), org, Collections.emptyList()));
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByMultiIds2() {
        Organization org = new Organization();

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        MapperUtils.updateByMultiIds(new OrganizationTestMapper(), org,
                Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2));
    }

    @Test
    public void getById() {
        MapperUtils.getById(new UserTestMapper() {
            @Override
            public User rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return getUser1();
            }
        }, 1);
    }

    @Test
    public void getByIds() {
        MapperUtils.getByIds(new UserTestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id IN(?, ?, ?) LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3),
                                getStartColumn().value(0),
                                getLimitColumn().value(3)
                        ),
                        sqlParams);
                return Arrays.asList(getUser1(), getUser2());
            }
        }, Arrays.asList(1, 2, 3));

        MapperUtils.getByIds(new UserTestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return Collections.singletonList(getUser1());
            }
        }, Collections.singletonList(1));
    }

    @Test
    public void getByMultiId() {
        UserKey uKey1 = new UserKey(1);
        MapperUtils.getByMultiId(new User2TestMapper() {
            @Override
            public User rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return getUser1();
            }
        }, uKey1);


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        MapperUtils.getByMultiId(new OrganizationTestMapper() {
            @Override
            public Organization rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE o.city_code = ? AND o.name = ? LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return null;
            }
        }, oKey1);
    }

    @Test
    public void getByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperUtils.getByMultiIds(new User2TestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                getStartColumn().value(0),
                                getLimitColumn().value(2)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Arrays.asList(uKey1, uKey2));

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");
        MapperUtils.getByMultiIds(new OrganizationTestMapper() {
            @Override
            public List<Organization> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("002"),
                                getStartColumn().value(0),
                                getLimitColumn().value(2)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Arrays.asList(oKey1, oKey2));

    }

    @Test
    public void getPOById() {
        MapperUtils.getPoById(new UserTestMapper() {
            @Override
            public User rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return new User();
            }
        }, UserPo.class, 1);
    }

    @Test
    public void getPOByIds() {
        MapperUtils.getPoByIds(new UserTestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id IN(?, ?, ?) LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3),
                                getStartColumn().value(0),
                                getLimitColumn().value(3)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Arrays.asList(1, 2, 3));

        MapperUtils.getPoByIds(new UserTestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Collections.singletonList(1));
    }

    @Test
    public void getPoByMultiId() {
        UserKey uKey1 = new UserKey(1);
        MapperUtils.getPoByMultiId(new User2TestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, uKey1);
    }

    @Test
    public void getPoByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperUtils.getPoByMultiIds(new User2TestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                getStartColumn().value(0),
                                getLimitColumn().value(2)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, UserPo.class, Arrays.asList(uKey1, uKey2));
    }

    @Test
    public void addKeywords() {

        QuerySql<?> sql = SqlFactory.createQuery(null)
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperUtils.addKeywords(sql, "dev", Arrays.asList(UserProperties.name, UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email LIKE ?)",
                Arrays.asList(
                        UserProperties.name.value("%dev%"),
                        UserProperties.email.value("%dev%")
                ),
                sql.getPrepareStatement());

        sql = SqlFactory.createQuery(null)
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperUtils.addKeywords(sql, "_dev", Arrays.asList(UserProperties.name, UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email LIKE ?)",
                Arrays.asList(
                        UserProperties.name.value("%\\_dev%"),
                        UserProperties.email.value("%\\_dev%")
                ),
                sql.getPrepareStatement());
    }

    @Test
    public void addExactKeywords() {
        QuerySql<?> sql = SqlFactory.createQuery(null)
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperUtils.addExactKeywords(sql, "dev"
                , Collections.singletonList(UserProperties.name), Collections.singletonList(UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email = ?)",
                Arrays.asList(
                        UserProperties.name.value("%dev%"),
                        UserProperties.email.value("dev")
                ),
                sql.getPrepareStatement());

        sql = SqlFactory.createQuery(null)
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperUtils.addExactKeywords(sql, "_dev"
                , Collections.singletonList(UserProperties.name), Collections.singletonList(UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email = ?)",
                Arrays.asList(
                        UserProperties.name.value("%\\_dev%"),
                        UserProperties.email.value("\\_dev")
                ),
                sql.getPrepareStatement());
    }

    @Test
    public void getKeyword() {

        String keyword = MapperUtils.getKeyword("小D");
        assertEquals("%小D%", keyword);

        keyword = MapperUtils.getKeyword("小D_");
        assertEquals("%小D\\_%", keyword);

        keyword = MapperUtils.getKeyword("小D%");
        assertEquals("%小D\\%%", keyword);

    }


    @Test
    public void getExactKeyword() {

        String keyword = MapperUtils.getExactKeyWord("小D");
        assertEquals("小D", keyword);

        keyword = MapperUtils.getExactKeyWord("小D_");
        assertEquals("小D\\_", keyword);

        keyword = MapperUtils.getExactKeyWord("小D%");
        assertEquals("小D\\%", keyword);

    }

    @Test
    public void getByUniqueKey() {
        UniqueKey uKey1 = new UniqueKey(UserProperties.userId.value(1));
        MapperUtils.getByUniqueKey(new User2TestMapper() {
            @Override
            public User rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id = ? LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return getUser1();
            }
        }, uKey1);


        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("001"));
        MapperUtils.getByUniqueKey(new OrganizationTestMapper() {
            @Override
            public Organization rawSelectOne(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE o.city_code = ? AND o.name = ? LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return null;
            }
        }, oKey1);
    }

    @Test
    public void getByUniqueKeys() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        MapperUtils.getByMultiIds(new User2TestMapper() {
            @Override
            public List<User> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u "
                                + "WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                getStartColumn().value(0),
                                getLimitColumn().value(2)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Arrays.asList(uKey1, uKey2));

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");
        MapperUtils.getByMultiIds(new OrganizationTestMapper() {
            @Override
            public List<Organization> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("002"),
                                getStartColumn().value(0),
                                getLimitColumn().value(2)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Arrays.asList(oKey1, oKey2));

        MapperUtils.getByMultiIds(new OrganizationTestMapper() {
            @Override
            public List<Organization> rawSelect(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE o.city_code = ? AND o.name = ? LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
                        sqlParams);
                return Collections.emptyList();
            }
        }, Collections.singletonList(oKey1));
    }

    @Test
    public void deleteByUniqueKey() {
        UniqueKey uKey1 = new UniqueKey(UserProperties.userId.value(1));

        MapperUtils.deleteByUniqueKey(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, uKey1);

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("001"));
        MapperUtils.deleteByUniqueKey(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE city_code = ? AND name = ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001")
                        ),
                        sqlParams);
                return 1;
            }
        }, oKey1);
    }


    @Test
    public void deleteByUniqueKeys() {
        UniqueKey uKey1 = new UniqueKey(UserProperties.userId.value(1));
        UniqueKey uKey2 = new UniqueKey(UserProperties.userId.value(2));
        MapperUtils.deleteByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(uKey1));

        MapperUtils.deleteByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(uKey1, uKey2));


        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("001"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("002"));

        MapperUtils.deleteByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE city_code = ? AND name = ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001")
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(oKey1));

        MapperUtils.deleteByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?) OR (city_code = ? AND name = ?)",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("002")
                        ),
                        sqlParams);
                return 2;
            }
        }, Arrays.asList(oKey1, oKey2));
    }

    @Test
    public void updateByUniqueKeys() {
        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        UniqueKey uKey1 = new UniqueKey(UserProperties.userId.value(1));
        UniqueKey uKey2 = new UniqueKey(UserProperties.userId.value(2));
        UniqueKey uKey3 = new UniqueKey(UserProperties.userId.value(3));

        MapperUtils.updateByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(uKey1, uKey2, uKey3));

        MapperUtils.updateByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.password.value("12345678"),
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, user, Collections.singletonList(uKey1));

        MapperUtils.updateByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Collections.singletonList(UserProperties.type), Arrays.asList(uKey1, uKey2, uKey3));

        MapperUtils.updateByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(uKey1, uKey2, uKey3));


        MapperUtils.updateByUniqueKeys(new User2TestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                        Arrays.asList(
                                UserProperties.type.value(UserType.u1),
                                UserProperties.password.value("12345678"),
                                UserProperties.loginId.value(null),
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2),
                                UserProperties.userId.value(3)
                        ),
                        sqlParams);
                return 3;
            }
        }, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(uKey1, uKey2, uKey3));

        Organization org = new Organization();
        org.setType(1);

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("0001"), OrganizationProperties.name.value("广州市"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("0002"), OrganizationProperties.name.value("深圳市"));
        UniqueKey oKey3 = new UniqueKey(OrganizationProperties.cityCode.value("0003"), OrganizationProperties.name.value("中山市"));

        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams);
                return 1;
            }
        }, org, Collections.singletonList(oKey1));

        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
                        sqlParams);
                return 3;
            }
        }, org, Arrays.asList(oKey1, oKey2, oKey3));

        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams
                );
                return 1;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Collections.singletonList(oKey1));


        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE o.city_code = ? AND o.name = ?",
                        Arrays.asList(
                                OrganizationProperties.phone.value(null),
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams);
                return 1;
            }
        }, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type), Collections.singletonList(oKey1));

        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                                + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
                        sqlParams);
                return 3;
            }
        }, org, Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2, oKey3));

        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper() {
            @Override
            public int rawUpdate(@Param(SqlProvider.PARAM_SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?)"
                                + " OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.phone.value(null),
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市"),
                                OrganizationProperties.cityCode.value("0002"),
                                OrganizationProperties.name.value("深圳市"),
                                OrganizationProperties.cityCode.value("0003"),
                                OrganizationProperties.name.value("中山市")
                        ),
                        sqlParams);
                return 3;
            }
        }, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type), Arrays.asList(oKey1, oKey2, oKey3));
    }

    //测试没有id值的情况
    @Test
    public void updateByUniqueKeys1() {
        Organization org = new Organization();
        org.setType(1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        assertEquals(0, MapperUtils.updateByUniqueKeys(new OrganizationTestMapper(), org, Collections.emptyList()));
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByUniqueKeys2() {
        Organization org = new Organization();

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("0001"), OrganizationProperties.name.value("广州市"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("0002"), OrganizationProperties.name.value("深圳市"));
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        MapperUtils.updateByUniqueKeys(new OrganizationTestMapper(), org,
                Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2));
    }

    private User getUser1() {
        User user = new User();
        user.setUserId(1);
        user.setLoginId("dev1");
        user.setName("dev1");
        user.setEmail("dev1@ibit.tech");
        user.setPassword("12345678");
        user.setMobilePhone("188");
        user.setType(UserType.u1);
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
        user.setType(UserType.u1);
        return user;
    }

}