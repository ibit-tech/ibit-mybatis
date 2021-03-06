package tech.ibit.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.KeyValuePair;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MapperTest
 *
 * @author iBit程序猿
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class MapperTest {


    @Autowired
    private UserMapper mapper;

    @Test
    public void testSelect() {
        String sql = "select * from user";
        PrepareStatement statement = new PrepareStatement(sql, Collections.emptyList());
        List<User> user = mapper.rawSelect(statement);
        System.out.println(user);
    }

    @Test
    public void testSelectOne() {
        String sql = "select * from user where user_id = ?";
        PrepareStatement statement = new PrepareStatement(sql, Collections.singletonList(UserProperties.userId.value(1)));
        User user = mapper.rawSelectOne(statement);
        System.out.println(user);
    }

    @Test
    public void insert() {
        String sql = "insert into user(login_id, name, email, password, mobile_phone, type) values(?, ?, ?, ?, ?, ?)";
        List<ColumnValue> values = Arrays.asList(
                UserProperties.loginId.value("u3"),
                UserProperties.name.value("u3"),
                UserProperties.email.value("u3@ibit.tech"),
                UserProperties.password.value("12345678"),
                UserProperties.mobilePhone.value("100"),
                UserProperties.type.value(UserType.u2)
        );
        PrepareStatement statement = new PrepareStatement(sql, values);
        KeyValuePair key = new KeyValuePair(SqlProvider.PARAM_KEY, null);
        mapper.rawInsertWithGenerateKeys(statement, key);
        System.out.println(key.getValue());

        mapper.deleteById(((Long) key.getValue()).intValue());

        //'2', 'u2', 'u2', 'u2@ibit.tech', '12345678', '188', '2'
    }

    @Test
    public void insert2() {
        User user = new User();
        user.setLoginId("u4");
        user.setName("u4");
        user.setEmail("u4@ibit.tech");
        user.setPassword("12345678");
        user.setMobilePhone("101");
        user.setType(UserType.u1);

        mapper.insert(user);
        System.out.println(user);

        mapper.deleteById(user.getUserId());
    }


}
