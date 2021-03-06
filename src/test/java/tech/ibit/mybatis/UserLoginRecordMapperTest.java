package tech.ibit.mybatis;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.demo.entity.UserLoginRecord;
import tech.ibit.mybatis.demo.mapper.UserLoginRecordMapper;
import tech.ibit.mybatis.demo.service.UserLoginRecordService;
import tech.ibit.mybatis.sqlbuilder.exception.SqlException;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 无主键dao测试
 *
 * @author iBit程序猿
 * mailto: ibit_tech@aliyun.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class UserLoginRecordMapperTest {

    @Autowired
    private UserLoginRecordService userLoginRecordService;
    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;


    private UserLoginRecord userLoginRecord;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() {
        userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(1);
        userLoginRecord.setLoginTime(new Date());

        userLoginRecordService.deleteAll();
    }

    @Test
    public void insert() {

        userLoginRecordMapper.insert(userLoginRecord);

        List<UserLoginRecord> allRecords = userLoginRecordService.listAll();
        assertEquals(1, allRecords.size());
        assertEquals(userLoginRecord.getUserId(), allRecords.get(0).getUserId());
        assertEquals(userLoginRecord.getLoginTime(), allRecords.get(0).getLoginTime());
    }

    @Test
    public void insertException() {
        userLoginRecord.setLoginTime(null);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(user_login_record)'s column(login_time) is null!");
        userLoginRecordMapper.insert(userLoginRecord);
    }

    @Test
    public void deleteAll() {
        userLoginRecordMapper.insert(userLoginRecord);
        userLoginRecordMapper.insert(userLoginRecord);

        int result = userLoginRecordService.deleteAll();
        assertEquals(2, result);
    }

}