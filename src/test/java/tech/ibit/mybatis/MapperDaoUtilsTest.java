package tech.ibit.mybatis;

import org.apache.ibatis.annotations.Param;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tech.ibit.mybatis.test.CommonTest;
import tech.ibit.mybatis.test.entity.*;
import tech.ibit.mybatis.test.entity.property.OrganizationProperties;
import tech.ibit.mybatis.test.entity.property.UserProperties;
import tech.ibit.mybatis.test.entity.type.UserType;
import tech.ibit.sqlbuilder.KeyValuePair;
import tech.ibit.sqlbuilder.OrderBy;
import tech.ibit.sqlbuilder.PrepareStatement;
import tech.ibit.sqlbuilder.SqlFactory;
import tech.ibit.sqlbuilder.converter.EntityConverter;
import tech.ibit.sqlbuilder.exception.SqlException;
import tech.ibit.sqlbuilder.sql.SearchSql;
import tech.ibit.sqlbuilder.sql.UpdateSql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 工具类测试
 *
 * @author IBIT程序猿
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
        user.setType(UserType.u1);
        user.setName("dev");
        MapperDaoUtils.insert(new TestMapper() {
            @Override
            public int insertWithGenerateKeys(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams,
                                              @Param(SqlBuilder.KEY) KeyValuePair key) {
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
        MapperDaoUtils.insert(new TestMapper() {
            @Override
            public int insert(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.insert(new TestMapper(), user);
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
        MapperDaoUtils.insert(new TestMapper(), user);
    }

    @Test
    public void deleteById() {
        MapperDaoUtils.deleteById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, User.class, 1);
    }

    @Test
    public void deleteByIds() {
        MapperDaoUtils.deleteByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id IN(?, ?)",
                        Arrays.asList(
                                UserProperties.userId.value(1),
                                UserProperties.userId.value(2)
                        ),
                        sqlParams);
                return 2;
            }
        }, User.class, Arrays.asList(1, 2));

        MapperDaoUtils.deleteByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
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
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.deleteByMultiId(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?)",
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
        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM user WHERE user_id = ?",
                        Collections.singletonList(
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(uKey1));

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "DELETE FROM organization WHERE (city_code = ? AND name = ?)",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001")
                        ),
                        sqlParams);
                return 1;
            }
        }, Collections.singletonList(oKey1));

        MapperDaoUtils.deleteByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.batchUpdateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.batchUpdateById(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.updateById(new TestMapper(), user);
    }

    //多主键，某个主键为null
    @Test
    public void updateById3() {
        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s id(name) is null!");
        MapperDaoUtils.updateById(new TestMapper(), org);
    }

    //某个不能为column设置为空
    @Test
    public void updateById4() {
        User user = new User();
        user.setUserId(1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperDaoUtils.updateById(new TestMapper(), user, Collections.singletonList(UserProperties.password));
    }

    @Test
    public void updateByIdAndIgnoreColumns() {
        User user = getUser1();
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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


        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.updateByIdAndIgnoreColumns(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        thrown.expect(SqlException.class);
        thrown.expectMessage("Id value not found");
        MapperDaoUtils.updateByIds(new TestMapper(), user, Collections.emptyList());
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByIds2() {
        User user = new User();
        user.setType(UserType.u1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        MapperDaoUtils.updateByIds(new TestMapper(), user, Collections.singletonList(UserProperties.password), Arrays.asList(1, 2));
    }

    @Test
    public void updateByMultiIds() {
        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        UserKey uKey3 = new UserKey(3);

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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


        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
                        Arrays.asList(
                                OrganizationProperties.type.value(1),
                                OrganizationProperties.cityCode.value("0001"),
                                OrganizationProperties.name.value("广州市")
                        ),
                        sqlParams);
                return 1;
            }
        }, org, Collections.singletonList(oKey1));

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
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


        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?)",
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.updateByMultiIds(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        thrown.expect(SqlException.class);
        thrown.expectMessage("Id value not found");
        MapperDaoUtils.updateByMultiIds(new TestMapper(), org, Collections.emptyList());
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByMultiIds2() {
        Organization org = new Organization();

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        MapperDaoUtils.updateByMultiIds(new TestMapper(), org,
                Collections.singletonList(OrganizationProperties.type), Arrays.asList(oKey1, oKey2));
    }

    @Test
    public void getById() {
        MapperDaoUtils.getById(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, User.class, 1);
    }

    @Test
    public void getByIds() {
        MapperDaoUtils.getByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, User.class, Arrays.asList(1, 2, 3));

        MapperDaoUtils.getByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, User.class, Collections.singletonList(1));
    }

    @Test
    public void getByMultiId() {
        UserKey uKey1 = new UserKey(1);
        MapperDaoUtils.getByMultiId(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, User.class, uKey1);


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        MapperDaoUtils.getByMultiId(new TestMapper<Organization>() {
            @Override
            public List<Organization> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT o.city_code, o.name, o.type, o.phone FROM organization o "
                                + "WHERE (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                        Arrays.asList(
                                OrganizationProperties.cityCode.value("001"),
                                OrganizationProperties.name.value("001"),
                                getStartColumn().value(0),
                                getLimitColumn().value(1)
                        ),
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
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, User.class, Arrays.asList(uKey1, uKey2));

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");
        MapperDaoUtils.getByMultiIds(new TestMapper<Organization>() {
            @Override
            public List<Organization> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, Organization.class, Arrays.asList(oKey1, oKey2));

    }

    @Test
    public void getPOById() {
        MapperDaoUtils.getPoById(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        }, UserPo.class, 1);
    }

    @Test
    public void getPOByIds() {
        MapperDaoUtils.getPoByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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

        MapperDaoUtils.getPoByIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.getPoByMultiId(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
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
        MapperDaoUtils.getPoByMultiIds(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type FROM user u "
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
    public void executeUpdate() {
        UpdateSql sql = SqlFactory.createUpdate()
                .update(UserProperties.TABLE)
                .set(UserProperties.name.set("小D"))
                .andWhere(UserProperties.userId.eq(1));
        MapperDaoUtils.executeUpdate(new TestMapper() {
            @Override
            public int update(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "UPDATE user u SET u.name = ? WHERE u.user_id = ?",
                        Arrays.asList(
                                UserProperties.name.value("小D"),
                                UserProperties.userId.value(1)
                        ),
                        sqlParams);
                return 1;
            }
        }, sql.getPrepareStatement());
    }

    @Test
    public void executeQuery() {
        SearchSql sql = SqlFactory.createSearch()
                .columnPo(User.class)
                .from(UserProperties.TABLE)
                .orderBy(new OrderBy(UserProperties.userId, true))
                .limit(10);
        List<User> users = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals(
                        "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type "
                                + "FROM user u ORDER BY u.user_id DESC LIMIT ?, ?",
                        Arrays.asList(
                                getStartColumn().value(0),
                                getLimitColumn().value(10)
                        ),
                        sqlParams
                );
                return Arrays.asList(getUser2(), getUser1());
            }

        }, sql.getPrepareStatement());
        assertEquals(2, users.size());
        assertUser(getUser2(), users.get(0));
        assertUser(getUser1(), users.get(1));
    }

    @Test
    public void executeQuery1() {
        SearchSql sql = SqlFactory.createSearch()
                .columnPo(UserPo.class)
                .from(UserProperties.TABLE)
                .orderBy(new OrderBy(UserProperties.userId, true))
                .limit(10);
        List<UserPo> users = MapperDaoUtils.executeQuery(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                assertPrepareStatementEquals("SELECT u.user_id, u.login_id, u.email, u.mobile_phone, u.type "
                                + "FROM user u ORDER BY u.user_id DESC LIMIT ?, ?",
                        Arrays.asList(
                                getStartColumn().value(0),
                                getLimitColumn().value(10)
                        ),
                        sqlParams);
                return Arrays.asList(getUser2(), getUser1());
            }

        }, UserPo.class, sql.getPrepareStatement());
        assertEquals(2, users.size());
        assertUserPo(EntityConverter.copyColumns(getUser2(), UserPo.class), users.get(0));
        assertUserPo(EntityConverter.copyColumns(getUser1(), UserPo.class), users.get(1));
    }

    @Test
    public void executeQueryOne() {
        SearchSql sql = SqlFactory.createSearch()
                .columnPo(User.class)
                .from(UserProperties.TABLE)
                .andWhere(UserProperties.userId.eq(1))
                .orderBy(new OrderBy(UserProperties.userId))
                .limit(1);
        User user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                return Collections.emptyList();
            }
        }, sql.getPrepareStatement());
        assertNull(user);

        user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                return Collections.singletonList(getUser1());
            }
        }, sql.getPrepareStatement());
        assertUser(getUser1(), user);
    }

    @Test
    public void executeQueryOne1() {
        SearchSql sql = SqlFactory.createSearch()
                .columnPo(User.class)
                .from(UserProperties.TABLE)
                .andWhere(UserProperties.userId.eq(1))
                .orderBy(new OrderBy(UserProperties.userId))
                .limit(1);
        UserPo user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                return Collections.emptyList();
            }
        }, UserPo.class, sql.getPrepareStatement());
        assertNull(user);

        user = MapperDaoUtils.executeQueryOne(new TestMapper<User>() {
            @Override
            public List<User> select(@Param(SqlBuilder.SQL_PARAMS) PrepareStatement sqlParams) {
                return Collections.singletonList(getUser1());
            }
        }, UserPo.class, sql.getPrepareStatement());
        assertUserPo(EntityConverter.copyColumns(getUser1(), UserPo.class), user);

    }

    @Test
    public void addKeywords() {

        SearchSql sql = SqlFactory.createSearch()
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperDaoUtils.addKeywords(sql, "dev", Arrays.asList(UserProperties.name, UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email LIKE ?)",
                Arrays.asList(
                        UserProperties.name.value("%dev%"),
                        UserProperties.email.value("%dev%")
                ),
                sql.getPrepareStatement());

        sql = SqlFactory.createSearch()
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperDaoUtils.addKeywords(sql, "_dev", Arrays.asList(UserProperties.name, UserProperties.email));
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
        SearchSql sql = SqlFactory.createSearch()
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperDaoUtils.addExactKeywords(sql, "dev"
                , Collections.singletonList(UserProperties.name), Collections.singletonList(UserProperties.email));
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.name, u.email FROM user u WHERE (u.name LIKE ? OR u.email = ?)",
                Arrays.asList(
                        UserProperties.name.value("%dev%"),
                        UserProperties.email.value("dev")
                ),
                sql.getPrepareStatement());

        sql = SqlFactory.createSearch()
                .column(
                        Arrays.asList(
                                UserProperties.userId,
                                UserProperties.name,
                                UserProperties.email
                        )
                )
                .from(UserProperties.TABLE);
        MapperDaoUtils.addExactKeywords(sql, "_dev"
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