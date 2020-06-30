package tech.ibit.mybatis.template.mapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.template.provider.SqlBuilder;
import tech.ibit.mybatis.test.dao.construct.UserConstructDao;
import tech.ibit.mybatis.test.entity.User;
import tech.ibit.mybatis.test.entity.UserPo;
import tech.ibit.mybatis.test.entity.property.UserProperties;
import tech.ibit.mybatis.test.entity.type.UserType;
import tech.ibit.mybatis.test.mapper.UserMapper;
import tech.ibit.mybatis.type.CommonEnum;
import tech.ibit.sqlbuilder.exception.SqlException;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 单主键dao测试
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class SingleIdMapperTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<User> testUsers;

    @Before
    public void setUp() {
        testUsers = new ArrayList<>();

        SqlBuilder.setValueFormatter(new LinkedHashMap<Class, Function<Object, Object>>() {{
            put(CommonEnum.class, o -> ((CommonEnum) o).getValue());
        }});
    }

    @After
    public void tearDown() {
        if (CollectionUtils.isNotEmpty(testUsers)) {
            testUsers.forEach(testUser -> userMapper.deleteById(testUser.getUserId()));
        }

        System.out.println(userExtMapper.listUserIds());
    }

    @Autowired
    private UserConstructDao userExtMapper;

    @Autowired
    private UserMapper userMapper;

    private User getUser() {
        User user = new User();
        user.setLoginId("dev1");
        user.setName("dev1");
        user.setEmail("dev1@ibit.tech");
        user.setPassword("password**");
        user.setMobilePhone("1098");
        user.setType(UserType.u1);
        return user;
    }

    private User insertUser() {
        User user = getUser();
        userMapper.insert(user);
        testUsers.add(user);
        return user;
    }

    @Test
    public void insert() {
        User user = insertUser();
        assertEquals(user, userMapper.getById(user.getUserId()));
    }


    @Test
    public void insertException() {
        User user = getUser();
        user.setUserId(1);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s id(user_id) cannot be inserted!");
        userMapper.insert(user);
    }


    @Test
    public void insertException2() {
        User user = getUser();
        user.setName(null);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userMapper.insert(user);
    }


    @Test
    public void deleteById() {
        int result = userMapper.deleteById(100);
        assertEquals(0, result);

        User user = insertUser();
        result = userMapper.deleteById(user.getUserId());
        assertEquals(1, result);
    }

    @Test
    public void deleteByIds() {
        int result = userMapper.deleteByIds(Arrays.asList(-1, -2));
        assertEquals(0, result);

        User user1 = insertUser();
        User user2 = insertUser();
        result = userMapper.deleteByIds(Arrays.asList(user1.getUserId(), user2.getUserId()));
        assertEquals(2, result);
    }

    @Test
    public void updateById() {
        User user = insertUser();

        User userUpdate = new User();
        userUpdate.setUserId(user.getUserId());
        userUpdate.setName("dev-haha");
        userMapper.updateById(userUpdate);

        user = userMapper.getById(user.getUserId());
        assertEquals("dev-haha", user.getName());


        userUpdate = user;
        userUpdate.setMobilePhone("109");
        userMapper.updateById(userUpdate);
        user = userMapper.getById(userUpdate.getUserId());
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
        userMapper.updateByIdWithColumns(user, Collections.singletonList(UserProperties.mobilePhone));
        user = userMapper.getById(userId);
        assertEquals(email, user.getEmail());
        assertEquals(mobilePhone + "0", user.getMobilePhone());

        User userUpdate = new User();
        userUpdate.setUserId(userId);
        userUpdate.setEmail(email + "0");
        userMapper.updateByIdWithColumns(userUpdate, Arrays.asList(UserProperties.loginId, UserProperties.email));
        user = userMapper.getById(userId);
        assertNull(user.getLoginId());
        assertEquals(email + "0", user.getEmail());
    }

    @Test
    public void updateByIdException1() {
        User user = insertUser();
        user.setName(null);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userMapper.updateByIdWithColumns(user, Collections.singletonList(UserProperties.name));
    }

    @Test
    public void updateByIds() {
        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(UserType.u3);
        userMapper.updateByIds(userUpdate, Arrays.asList(user1.getUserId(), user2.getUserId()));

        user1 = userMapper.getById(user1.getUserId());
        user2 = userMapper.getById(user2.getUserId());
        assertEquals(UserType.u3, user1.getType());
        assertEquals(UserType.u3, user2.getType());

    }

    @Test
    public void updateByIds1() {

        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(UserType.u3);
        userMapper.updateByIdsWithColumns(userUpdate, Arrays.asList(UserProperties.loginId, UserProperties.type), Arrays.asList(user1.getUserId(), user2.getUserId()));

        user1 = userMapper.getById(user1.getUserId());
        user2 = userMapper.getById(user2.getUserId());
        assertNull(user1.getLoginId());
        assertNull(user2.getLoginId());
        assertEquals(UserType.u3, user1.getType());
        assertEquals(UserType.u3, user2.getType());
    }


    @Test
    public void updateByIdsException1() {

        User user1 = insertUser();
        User user2 = insertUser();

        User userUpdate = new User();
        userUpdate.setType(UserType.u3);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user)'s column(name) is null!");
        userMapper.updateByIdsWithColumns(userUpdate,
                Arrays.asList(UserProperties.name, UserProperties.loginId, UserProperties.type),
                Arrays.asList(user1.getUserId(), user2.getUserId()));
    }

    @Test
    public void getById() {
        User user = insertUser();
        User user1 = userMapper.getById(user.getUserId());
        assertEquals(user, user1);
    }

    @Test
    public void getByIds() {
        User user0 = insertUser();
        User user1 = insertUser();

        List<User> users = userMapper.getByIds(Arrays.asList(user0.getUserId(), user1.getUserId()));
        assertEquals(user0, users.get(0));
        assertEquals(user1, users.get(1));
    }

    @Test
    public void listUserIds() {
        User user0 = insertUser();
        User user1 = insertUser();
        List<Integer> userIds = userExtMapper.listUserIds();
        assertEquals(user0.getUserId(), userIds.get(0));
        assertEquals(user1.getUserId(), userIds.get(1));
    }

    @Test
    public void getPoById() {
        User user = insertUser();
        UserPo userPo = userExtMapper.getPoById(user.getUserId());
        assertEquals(user.getUserId(), userPo.getUserId());
        assertEquals(user.getLoginId(), userPo.getLoginId());
        assertEquals(user.getEmail(), userPo.getEmail());
        assertEquals(user.getMobilePhone(), userPo.getMobilePhone());
        assertEquals(user.getType(), userPo.getType());

    }
}