package tech.ibit.mybatis.sqlbuilder.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.CommonTest;
import tech.ibit.mybatis.demo.entity.Organization;
import tech.ibit.mybatis.demo.entity.OrganizationKey;
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.UserKey;
import tech.ibit.mybatis.demo.entity.property.OrganizationProperties;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.mybatis.demo.mapper.OrganizationMapper;
import tech.ibit.mybatis.demo.mapper.User2Mapper;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author iBit程序猿
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class IdSqlUtilsTest extends CommonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private User2Mapper user2Mapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Test
    public void getByIds() {
        PrepareStatement prepareStatement = IdSqlUtils.getByIds(userMapper, Arrays.asList(1, 2, 3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?, ?) LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3),
                        getStartColumn().value(0),
                        getLimitColumn().value(3)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.getByIds(userMapper, Collections.singletonList(1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ), prepareStatement);
    }

    @Test
    public void getById() {
        PrepareStatement prepareStatement = IdSqlUtils.getById(userMapper, 1).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ), prepareStatement);
    }

    @Test
    public void getByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        PrepareStatement prepareStatement = IdSqlUtils.getByMultiIds(user2Mapper, Arrays.asList(uKey1, uKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        getStartColumn().value(0),
                        getLimitColumn().value(2)
                ), prepareStatement);


        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");
        prepareStatement = IdSqlUtils.getByMultiIds(organizationMapper, Arrays.asList(oKey1, oKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT o.city_code, o.name, o.type, o.phone FROM organization o WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) LIMIT ?, ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001"),
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("002"),
                        getStartColumn().value(0),
                        getLimitColumn().value(2)
                ),
                prepareStatement);
    }

    @Test
    public void getByMultiId() {
        UserKey uKey1 = new UserKey(1);
        PrepareStatement prepareStatement = IdSqlUtils.getByMultiId(user2Mapper, uKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ), prepareStatement);

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        prepareStatement = IdSqlUtils.getByMultiId(organizationMapper, oKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT o.city_code, o.name, o.type, o.phone FROM organization o WHERE o.city_code = ? AND o.name = ? LIMIT ?, ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001"),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ), prepareStatement);
    }

    @Test
    public void deleteByIds() {
        PrepareStatement prepareStatement = IdSqlUtils.deleteByIds(userMapper, Arrays.asList(1, 2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id IN(?, ?)",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.deleteByIds(userMapper, Collections.singletonList(1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);
    }

    @Test
    public void deleteById() {
        PrepareStatement prepareStatement = IdSqlUtils.deleteById(userMapper, 1).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);
    }

    @Test
    public void deleteByMultiIds() {
        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);

        PrepareStatement prepareStatement = IdSqlUtils.deleteByMultiIds(user2Mapper, Collections.singletonList(uKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.deleteByMultiIds(user2Mapper, Arrays.asList(uKey1, uKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id IN(?, ?)",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2)
                ), prepareStatement);

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        OrganizationKey oKey2 = new OrganizationKey("001", "002");

        prepareStatement = IdSqlUtils.deleteByMultiIds(organizationMapper, Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM organization WHERE city_code = ? AND name = ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.deleteByMultiIds(organizationMapper, Arrays.asList(oKey1, oKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM organization WHERE (city_code = ? AND name = ?) OR (city_code = ? AND name = ?)",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001"),
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("002")
                ), prepareStatement);
    }

    @Test
    public void deleteByMultiId() {
        UserKey uKey1 = new UserKey(1);
        PrepareStatement prepareStatement = IdSqlUtils.deleteByMultiId(user2Mapper, uKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);

        OrganizationKey oKey1 = new OrganizationKey("001", "001");
        prepareStatement = IdSqlUtils.deleteByMultiId(organizationMapper, oKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM organization WHERE city_code = ? AND name = ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001")
                ), prepareStatement);
    }

    @Test
    public void insertInto() {
        User user = new User();
        user.setLoginId("ibit_tech@aliyun.com");
        user.setName("ibit-tech");
        user.setEmail("ibit_tech@aliyun.com");
        user.setPassword("12345678");
        user.setMobilePhone("188");
        user.setType(UserType.u1);

        PrepareStatement prepareStatement = IdSqlUtils.insertInto(userMapper, user).getPrepareStatement();
        assertPrepareStatementEquals("INSERT INTO user(login_id, name, email, password, mobile_phone, type) VALUES(?, ?, ?, ?, ?, ?)",
                Arrays.asList(
                        UserProperties.loginId.value("ibit_tech@aliyun.com"),
                        UserProperties.name.value("ibit-tech"),
                        UserProperties.email.value("ibit_tech@aliyun.com"),
                        UserProperties.password.value("12345678"),
                        UserProperties.mobilePhone.value("188"),
                        UserProperties.type.value(UserType.u1)
                ), prepareStatement);
    }

    @Test
    public void insertInto1() {
        User user = new User();
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        IdSqlUtils.insertInto(userMapper, user).getPrepareStatement();
    }

    @Test
    public void insertInto2() {
        User user = new User();
        user.setLoginId("ibit_tech@aliyun.com");
        user.setName("ibit-tech");
        user.setEmail("ibit_tech@aliyun.com");
        user.setMobilePhone("188");
        user.setType(UserType.u1);
        user.setUserId(1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s id(user_id) cannot be inserted!");

        IdSqlUtils.insertInto(userMapper, user).getPrepareStatement();
    }

    @Test
    public void batchInsertInto() {
        User user = new User();
        user.setLoginId("ibit_tech@aliyun.com");
        user.setName("ibit-tech");
        user.setEmail("ibit_tech@aliyun.com");
        user.setMobilePhone("188");
        user.setType(UserType.u1);

        User user2 = new User();
        user2.setEmail("ibittech@ibit.tech");
        user2.setType(UserType.u2);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        IdSqlUtils.batchInsertInto(userMapper,
                Arrays.asList(user, user2),
                Arrays.asList(UserProperties.email, UserProperties.mobilePhone, UserProperties.type)
        ).getPrepareStatement();
    }

    @Test
    public void batchInsertInto2() {
        User user = new User();
        user.setLoginId("ibit_tech@aliyun.com");
        user.setName("ibit-tech");
        user.setEmail("ibit_tech@aliyun.com");
        user.setPassword("12345678");
        user.setMobilePhone("188");
        user.setType(UserType.u1);

        User user2 = new User();
        user2.setName("ibittech");
        user2.setEmail("ibittech@ibit.tech");
        user2.setPassword("12345609");
        user2.setMobilePhone("100");
        user2.setType(UserType.u2);


        PrepareStatement prepareStatement = IdSqlUtils.batchInsertInto(
                userMapper, Arrays.asList(user, user2),
                Arrays.asList(UserProperties.name, UserProperties.email, UserProperties.mobilePhone, UserProperties.type, UserProperties.password)
        ).getPrepareStatement();
        assertPrepareStatementEquals(
                "INSERT INTO user(name, email, mobile_phone, type, password) VALUES(?, ?, ?, ?, ?), (?, ?, ?, ?, ?)",
                Arrays.asList(
                        UserProperties.name.value("ibit-tech"),
                        UserProperties.email.value("ibit_tech@aliyun.com"),
                        UserProperties.mobilePhone.value("188"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.password.value("12345678"),
                        UserProperties.name.value("ibittech"),
                        UserProperties.email.value("ibittech@ibit.tech"),
                        UserProperties.mobilePhone.value("100"),
                        UserProperties.type.value(UserType.u2),
                        UserProperties.password.value("12345609")
                ), prepareStatement);
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setEmail("ibit_tech@aliyun.com");
        user.setMobilePhone("188");
        user.setType(UserType.u1);
        user.setUserId(1);

        PrepareStatement prepareStatement = IdSqlUtils.updateById(userMapper, user).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.email = ?, u.mobile_phone = ?, u.type = ? WHERE u.user_id = ?",
                Arrays.asList(
                        UserProperties.email.value("ibit_tech@aliyun.com"),
                        UserProperties.mobilePhone.value("188"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateById(
                userMapper, user, Arrays.asList(UserProperties.loginId, UserProperties.mobilePhone)
        ).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.login_id = ?, u.mobile_phone = ? WHERE u.user_id = ?",
                Arrays.asList(
                        UserProperties.loginId.value(null),
                        UserProperties.mobilePhone.value("188"),
                        UserProperties.userId.value(1)
                ), prepareStatement);

        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        org.setName("广州");
        prepareStatement = IdSqlUtils.updateById(organizationMapper, org).getPrepareStatement();
        assertPrepareStatementEquals("UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州")
                ), prepareStatement);

    }

    //测试id为null的情况
    @Test
    public void updateById2() {
        User user = new User();
        user.setEmail("ibit_tech@aliyun.com");
        user.setMobilePhone("188");
        user.setType(UserType.u1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s id(user_id) is null!");
        IdSqlUtils.updateById(userMapper, user).getPrepareStatement();
    }

    //多主键，某个主键为null
    @Test
    public void updateById3() {
        Organization org = new Organization();
        org.setType(1);
        org.setCityCode("0001");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s id(name) is null!");
        IdSqlUtils.updateById(organizationMapper, org).getPrepareStatement();
    }

    //某个不能为column设置为空
    @Test
    public void updateById4() {
        User user = new User();
        user.setUserId(1);

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        IdSqlUtils.updateById(userMapper, user, Collections.singletonList(UserProperties.password)).getPrepareStatement();
    }


    @Test
    public void updateByIds() {

        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");
        PrepareStatement prepareStatement = IdSqlUtils.updateByIds(userMapper, user, Arrays.asList(1, 2, 3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByIds(userMapper, user, Collections.singletonList(1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByIds(
                userMapper, user, Collections.singletonList(UserProperties.type), Arrays.asList(1, 2, 3)
        ).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);


        prepareStatement = IdSqlUtils.updateByIds(userMapper, user, Arrays.asList(UserProperties.type, UserProperties.password), Arrays.asList(1, 2, 3))
                .getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.password.value("12345678"),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByIds(
                userMapper, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId), Arrays.asList(1, 2, 3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.password.value("12345678"),
                        UserProperties.loginId.value(null),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);
    }


    //测试没有id值的情况
    @Test
    public void updateByIds1() {
        User user = new User();
        user.setType(UserType.u1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Id value not found");
        IdSqlUtils.updateByIds(userMapper, user, Collections.emptyList()).getPrepareStatement();
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByIds2() {
        User user = new User();
        user.setType(UserType.u1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(password) is null!");
        IdSqlUtils.updateByIds(
                userMapper, user, Collections.singletonList(UserProperties.password), Arrays.asList(1, 2)
        ).getPrepareStatement();
    }

    @Test
    public void updateByMultiIds() {
        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        UserKey uKey1 = new UserKey(1);
        UserKey uKey2 = new UserKey(2);
        UserKey uKey3 = new UserKey(3);
        PrepareStatement prepareStatement = IdSqlUtils.updateByMultiIds(
                user2Mapper, user, Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(
                user2Mapper, user, Collections.singletonList(uKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(user2Mapper, user, Collections.singletonList(UserProperties.type),
                Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);


        prepareStatement = IdSqlUtils.updateByMultiIds(user2Mapper, user, Arrays.asList(UserProperties.type, UserProperties.password),
                Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ?, u.password = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.password.value("12345678"),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(user2Mapper, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId),
                Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ?, u.password = ?, u.login_id = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.password.value("12345678"),
                        UserProperties.loginId.value(null),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);


        Organization org = new Organization();
        org.setType(1);

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        OrganizationKey oKey3 = new OrganizationKey("0003", "中山市");

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org, Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org,
                Arrays.asList(oKey1, oKey2, oKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市"),
                        OrganizationProperties.cityCode.value("0002"),
                        OrganizationProperties.name.value("深圳市"),
                        OrganizationProperties.cityCode.value("0003"),
                        OrganizationProperties.name.value("中山市")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org, Collections.singletonList(OrganizationProperties.type),
                Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type),
                Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.phone = ?, o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.phone.value(null),
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org, Collections.singletonList(OrganizationProperties.type)
                , Arrays.asList(oKey1, oKey2, oKey3)).getPrepareStatement();
        assertPrepareStatementEquals("UPDATE organization o SET o.type = ? WHERE (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市"),
                        OrganizationProperties.cityCode.value("0002"),
                        OrganizationProperties.name.value("深圳市"),
                        OrganizationProperties.cityCode.value("0003"),
                        OrganizationProperties.name.value("中山市")
                ), prepareStatement);

        prepareStatement = IdSqlUtils.updateByMultiIds(organizationMapper, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type)
                , Arrays.asList(oKey1, oKey2, oKey3)).getPrepareStatement();
        assertPrepareStatementEquals("UPDATE organization o SET o.phone = ?, o.type = ? WHERE (o.city_code = ? AND o.name = ?) "
                        + "OR (o.city_code = ? AND o.name = ?) OR (o.city_code = ? AND o.name = ?)",
                Arrays.asList(
                        OrganizationProperties.phone.value(null),
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市"),
                        OrganizationProperties.cityCode.value("0002"),
                        OrganizationProperties.name.value("深圳市"),
                        OrganizationProperties.cityCode.value("0003"),
                        OrganizationProperties.name.value("中山市")
                ), prepareStatement);

    }

    //测试没有id值的情况
    @Test
    public void updateByMultiIds1() {
        Organization org = new Organization();
        org.setType(1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Id value not found");
        IdSqlUtils.updateByMultiIds(organizationMapper, org, Collections.emptyList()).getPrepareStatement();
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByMultiIds2() {
        Organization org = new Organization();

        OrganizationKey oKey1 = new OrganizationKey("0001", "广州市");
        OrganizationKey oKey2 = new OrganizationKey("0002", "深圳市");
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        IdSqlUtils.updateByMultiIds(organizationMapper, org, Collections.singletonList(OrganizationProperties.type),
                Arrays.asList(oKey1, oKey2)).getPrepareStatement();
    }


}