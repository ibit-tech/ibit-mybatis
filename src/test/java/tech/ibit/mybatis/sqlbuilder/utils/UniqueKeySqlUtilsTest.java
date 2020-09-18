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
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.property.OrganizationProperties;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.mybatis.demo.mapper.OrganizationMapper;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.UniqueKey;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;

import java.util.Arrays;
import java.util.Collections;

/**
 * UniqueKeySqlUtilsTest
 *
 * @author ben
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class UniqueKeySqlUtilsTest extends CommonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Test
    public void getByUniqueKeys() {

        UniqueKey uKey1 = UserProperties.userId.uniqueKey(1);
        UniqueKey uKey2 = UserProperties.userId.uniqueKey(2);
        PrepareStatement prepareStatement = UniqueKeySqlUtils.getByUniqueKeys(
                userMapper, User.class, Arrays.asList(uKey1, uKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id IN(?, ?) LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        getStartColumn().value(0),
                        getLimitColumn().value(2)
                ), prepareStatement);


        UniqueKey oKey1 = new UniqueKey(
                OrganizationProperties.cityCode.value("001"),
                OrganizationProperties.name.value("001")
        );
        UniqueKey oKey2 = new UniqueKey(
                OrganizationProperties.cityCode.value("001"),
                OrganizationProperties.name.value("002")
        );
        prepareStatement = UniqueKeySqlUtils.getByUniqueKeys(organizationMapper, Organization.class, Arrays.asList(oKey1, oKey2)).getPrepareStatement();
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

        prepareStatement = UniqueKeySqlUtils.getByUniqueKeys(organizationMapper, Organization.class, Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT o.city_code, o.name, o.type, o.phone FROM organization o WHERE o.city_code = ? AND o.name = ? LIMIT ?, ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001"),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ),
                prepareStatement);
    }

    @Test
    public void getByUniqueKey() {
        UniqueKey uKey1 = UserProperties.userId.uniqueKey(1);
        PrepareStatement prepareStatement = UniqueKeySqlUtils.getByUniqueKey(userMapper, User.class, uKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "SELECT u.user_id, u.login_id, u.name, u.email, u.password, u.mobile_phone, u.type FROM user u WHERE u.user_id = ? LIMIT ?, ?",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        getStartColumn().value(0),
                        getLimitColumn().value(1)
                ), prepareStatement);

        UniqueKey oKey1 = new UniqueKey(
                OrganizationProperties.cityCode.value("001"),
                OrganizationProperties.name.value("001")
        );
        prepareStatement = UniqueKeySqlUtils.getByUniqueKey(organizationMapper, Organization.class, oKey1).getPrepareStatement();
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
    public void deleteByUniqueKeys() {
        UniqueKey uKey1 = UserProperties.userId.uniqueKey(1);
        UniqueKey uKey2 = UserProperties.userId.uniqueKey(2);

        PrepareStatement prepareStatement = UniqueKeySqlUtils.deleteByUniqueKeys(
                userMapper, Collections.singletonList(uKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.deleteByUniqueKeys(userMapper, Arrays.asList(uKey1, uKey2)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id IN(?, ?)",
                Arrays.asList(
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2)
                ), prepareStatement);

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("001"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("001"), OrganizationProperties.name.value("002"));

        prepareStatement = UniqueKeySqlUtils.deleteByUniqueKeys(organizationMapper, Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM organization WHERE city_code = ? AND name = ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001")
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.deleteByUniqueKeys(organizationMapper, Arrays.asList(oKey1, oKey2)).getPrepareStatement();
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
    public void deleteByUniqueKey() {
        UniqueKey uKey1 = UserProperties.userId.uniqueKey(1);
        PrepareStatement prepareStatement = UniqueKeySqlUtils.deleteByUniqueKey(userMapper, uKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM user WHERE user_id = ?",
                Collections.singletonList(
                        UserProperties.userId.value(1)
                ), prepareStatement);

        UniqueKey oKey1 = new UniqueKey(
                OrganizationProperties.cityCode.value("001"),
                OrganizationProperties.name.value("001"));
        prepareStatement = UniqueKeySqlUtils.deleteByUniqueKey(organizationMapper, oKey1).getPrepareStatement();
        assertPrepareStatementEquals(
                "DELETE FROM organization WHERE city_code = ? AND name = ?",
                Arrays.asList(
                        OrganizationProperties.cityCode.value("001"),
                        OrganizationProperties.name.value("001")
                ), prepareStatement);
    }

    //测试没有id值的情况
    @Test
    public void deleteByUniqueKey1() {
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        UniqueKeySqlUtils.deleteByUniqueKey(organizationMapper, null).getPrepareStatement();
    }

    @Test
    public void deleteByUniqueKey2() {
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        UniqueKeySqlUtils.deleteByUniqueKey(organizationMapper, new UniqueKey()).getPrepareStatement();
    }

    //测试没有id值的情况
    @Test
    public void deleteByUniqueKeys1() {
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        UniqueKeySqlUtils.deleteByUniqueKeys(organizationMapper, null).getPrepareStatement();
    }

    @Test
    public void deleteByUniqueKeys2() {
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        UniqueKeySqlUtils.deleteByUniqueKeys(organizationMapper, Arrays.asList(new UniqueKey(UserProperties.userId.value(1)), null)).getPrepareStatement();
    }

    @Test
    public void updateByUniqueKeys() {
        User user = new User();
        user.setType(UserType.u1);
        user.setPassword("12345678");

        UniqueKey uKey1 = UserProperties.userId.uniqueKey(1);
        UniqueKey uKey2 = UserProperties.userId.uniqueKey(2);
        UniqueKey uKey3 = UserProperties.userId.uniqueKey(3);
        PrepareStatement prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(
                userMapper, user, Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(
                userMapper, user, Collections.singletonList(uKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.password = ?, u.type = ? WHERE u.user_id = ?",
                Arrays.asList(
                        UserProperties.password.value("12345678"),
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1)
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(userMapper, user, Collections.singletonList(UserProperties.type),
                Arrays.asList(uKey1, uKey2, uKey3)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE user u SET u.type = ? WHERE u.user_id IN(?, ?, ?)",
                Arrays.asList(
                        UserProperties.type.value(UserType.u1),
                        UserProperties.userId.value(1),
                        UserProperties.userId.value(2),
                        UserProperties.userId.value(3)
                ), prepareStatement);


        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(userMapper, user, Arrays.asList(UserProperties.type, UserProperties.password),
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

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(userMapper, user, Arrays.asList(UserProperties.type, UserProperties.password, UserProperties.loginId),
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

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("0001"), OrganizationProperties.name.value("广州市"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("0002"), OrganizationProperties.name.value("深圳市"));
        UniqueKey oKey3 = new UniqueKey(OrganizationProperties.cityCode.value("0003"), OrganizationProperties.name.value("中山市"));

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org,
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

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Collections.singletonList(OrganizationProperties.type),
                Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type),
                Collections.singletonList(oKey1)).getPrepareStatement();
        assertPrepareStatementEquals(
                "UPDATE organization o SET o.phone = ?, o.type = ? WHERE o.city_code = ? AND o.name = ?",
                Arrays.asList(
                        OrganizationProperties.phone.value(null),
                        OrganizationProperties.type.value(1),
                        OrganizationProperties.cityCode.value("0001"),
                        OrganizationProperties.name.value("广州市")
                ), prepareStatement);

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Collections.singletonList(OrganizationProperties.type)
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

        prepareStatement = UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type)
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
    public void updateByUniqueKeys1() {
        Organization org = new Organization();
        org.setType(1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Unique key value not found");
        UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Collections.emptyList()).getPrepareStatement();
    }

    //测试设置某个不允许为null的列为null
    @Test
    public void updateByUniqueKeys2() {
        Organization org = new Organization();

        UniqueKey oKey1 = new UniqueKey(OrganizationProperties.cityCode.value("0001"), OrganizationProperties.name.value("广州市"));
        UniqueKey oKey2 = new UniqueKey(OrganizationProperties.cityCode.value("0002"), OrganizationProperties.name.value("深圳市"));
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        UniqueKeySqlUtils.updateByUniqueKeys(organizationMapper, org, Collections.singletonList(OrganizationProperties.type),
                Arrays.asList(oKey1, oKey2)).getPrepareStatement();
    }
}