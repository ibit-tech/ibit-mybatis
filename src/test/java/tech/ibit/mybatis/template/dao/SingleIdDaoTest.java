package tech.ibit.mybatis.template.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.test.dao.UserDao;
import tech.ibit.mybatis.test.entity.User;
import tech.ibit.mybatis.test.entity.UserPo;
import tech.ibit.mybatis.test.entity.UserTypeTotal;
import tech.ibit.mybatis.test.entity.property.UserProperties;
import tech.ibit.sqlbuilder.exception.ColumnNullPointerException;
import tech.ibit.sqlbuilder.exception.IdAutoIncreaseException;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * 单主键dao测试
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class SingleIdDaoTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<User> testUsers;

    @Before
    public void setUp() {
        testUsers = new ArrayList<>();
    }

    @After
    public void tearDown() {
        if (CollectionUtils.isNotEmpty(testUsers)) {
            testUsers.forEach(testUser -> userDao.deleteById(testUser.getUserId()));
        }
    }

    @Autowired
    private UserDao userDao;


    private User getUser() {
        User user = new User();
        user.setLoginId("dev1");
        user.setName("dev1");
        user.setEmail("dev1@ibit.tech");
        user.setPassword("password**");
        user.setMobilePhone("1098");
        user.setType(1);
        return user;
    }

    private User insertUser() {
        User user = getUser();
        userDao.insert(user);
        testUsers.add(user);
        return user;
    }

    @Test
    public void insert() {
        User user = insertUser();
        assertEquals(user, userDao.getById(user.getUserId()));
    }


    @Test
    public void insertException() {
        User user = getUser();
        user.setUserId(1);
        thrown.expect(IdAutoIncreaseException.class);
        thrown.expectMessage("Table(user)'s id(user_id) cannot be inserted!");
        userDao.insert(user);
    }


    @Test
    public void insertException2() {
        User user = getUser();
        user.setName(null);
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userDao.insert(user);
    }


    @Test
    public void deleteById() {
        int result = userDao.deleteById(-1);
        assertEquals(0, result);

        User user = insertUser();
        result = userDao.deleteById(user.getUserId());
        assertEquals(1, result);
    }

    @Test
    public void deleteByIds() {
        int result = userDao.deleteByIds(Arrays.asList(-1, -2));
        assertEquals(0, result);

        User user1 = insertUser();
        User user2 = insertUser();
        result = userDao.deleteByIds(Arrays.asList(user1.getUserId(), user2.getUserId()));
        assertEquals(2, result);
    }

    @Test
    public void updateById() {
        User user = insertUser();

        User userUpdate = new User();
        userUpdate.setUserId(user.getUserId());
        userUpdate.setName("dev-haha");
        userDao.updateById(userUpdate);

        user = userDao.getById(user.getUserId());
        assertEquals("dev-haha", user.getName());


        userUpdate = user;
        userUpdate.setMobilePhone("109");
        userDao.updateById(userUpdate);
        user = userDao.getById(userUpdate.getUserId());
        assertEquals("109", user.getMobilePhone());

    }

    @Test
    public void updateById1() {
        User user = insertUser();

        Integer userId = user.getUserId();
        String mobilePhone = user.getMobilePhone();
        String email = user.getEmail();

        user.setMobilePhone(mobilePhone + "0");
        user.setEmail(email + "0");
        userDao.updateById(user, Collections.singletonList(UserProperties.mobilePhone));
        user = userDao.getById(userId);
        assertEquals(email, user.getEmail());
        assertEquals(mobilePhone + "0", user.getMobilePhone());

        User userUpdate = new User();
        userUpdate.setUserId(userId);
        userUpdate.setEmail(email + "0");
        userDao.updateById(userUpdate, Arrays.asList(UserProperties.loginId, UserProperties.email));
        user = userDao.getById(userId);
        assertNull(user.getLoginId());
        assertEquals(email + "0", user.getEmail());
    }

    @Test
    public void updateByIdException1() {
        User user = insertUser();
        user.setName(null);
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userDao.updateById(user, Collections.singletonList(UserProperties.name));
    }

    @Test
    public void updateByIds() {
        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(3);
        userDao.updateByIds(userUpdate, Arrays.asList(user1.getUserId(), user2.getUserId()));

        user1 = userDao.getById(user1.getUserId());
        user2 = userDao.getById(user2.getUserId());
        assertEquals(Integer.valueOf(3), user1.getType());
        assertEquals(Integer.valueOf(3), user2.getType());

    }

    @Test
    public void updateByIds1() {

        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(3);
        userDao.updateByIds(userUpdate, Arrays.asList(UserProperties.loginId, UserProperties.type), Arrays.asList(user1.getUserId(), user2.getUserId()));

        user1 = userDao.getById(user1.getUserId());
        user2 = userDao.getById(user2.getUserId());
        assertNull(user1.getLoginId());
        assertNull(user2.getLoginId());
        assertEquals(Integer.valueOf(3), user1.getType());
        assertEquals(Integer.valueOf(3), user2.getType());
    }


    @Test
    public void updateByIdsException1() {

        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(3);
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userDao.updateByIds(userUpdate,
                Arrays.asList(UserProperties.name, UserProperties.loginId, UserProperties.type),
                Arrays.asList(user1.getUserId(), user2.getUserId()));
    }

    @Test
    public void getById() {
        User user = insertUser();
        User user1 = userDao.getById(user.getUserId());
        assertEquals(user, user1);
    }

    @Test
    public void getByIds() {
        User user0 = insertUser();
        User user1 = insertUser();

        List<User> users = userDao.getByIds(Arrays.asList(user0.getUserId(), user1.getUserId()));
        assertEquals(user0, users.get(0));
        assertEquals(user1, users.get(1));
    }

    @Test
    public void listUserIds() {
        User user0 = insertUser();
        User user1 = insertUser();
        List<Integer> userIds = userDao.listUserIds();
        assertEquals(user0.getUserId(), userIds.get(0));
        assertEquals(user1.getUserId(), userIds.get(1));
    }

    @Test
    public void getPoById() {
        User user = insertUser();
        UserPo userPo = userDao.getPoById(user.getUserId());
        assertEquals(user.getUserId(), userPo.getUserId());
        assertEquals(user.getLoginId(), userPo.getLoginId());
        assertEquals(user.getEmail(), userPo.getEmail());
        assertEquals(user.getMobilePhone(), userPo.getMobilePhone());
        assertEquals(user.getType(), userPo.getType());

    }

    @Test
    public void listTypeTotals() {
        List<UserTypeTotal> typeTotals = userDao.listTypeTotals();
        assertEquals(0, typeTotals.size());

        insertUser();
        typeTotals = userDao.listTypeTotals();
        assertEquals(1, typeTotals.size());
        assertEquals(Integer.valueOf(1), typeTotals.get(0).getTotal());
        assertEquals(Integer.valueOf(1), typeTotals.get(0).getType());

        insertUser();
        typeTotals = userDao.listTypeTotals();
        assertEquals(1, typeTotals.size());
        assertEquals(Integer.valueOf(2), typeTotals.get(0).getTotal());
        assertEquals(Integer.valueOf(1), typeTotals.get(0).getType());
    }
}