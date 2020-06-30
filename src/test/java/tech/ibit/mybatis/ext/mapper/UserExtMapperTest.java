package tech.ibit.mybatis.ext.mapper;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.ext.mapper.UserExtMapper;

import java.util.List;

/**
 * MapperTest
 *
 * @author IBIT程序猿
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Sql("classpath:/db/init.sql")
public class UserExtMapperTest {


    @Autowired
    private UserExtMapper mapper;

    @After
    public void tearDown() throws Exception {
        mapper.deleteAllUsers();
    }

    @Test
    public void listAllUsers() {
        List<User> users = mapper.listAllUsers();
        System.out.println(users);
    }


}
