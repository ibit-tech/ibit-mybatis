package tech.ibit.mybatis.template.dao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.test.dao.UserLoginRecordDao;
import tech.ibit.mybatis.test.entity.UserLoginRecord;
import tech.ibit.sqlbuilder.exception.ColumnNullPointerException;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 无主键dao测试
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class NoIdDaoTest {

    @Autowired
    private UserLoginRecordDao userLoginRecordDao;

    private UserLoginRecord userLoginRecord;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() {
        userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(1);
        userLoginRecord.setLoginTime(new Date());

        userLoginRecordDao.deleteAll();
    }

    @Test
    public void insert() {

        userLoginRecordDao.insert(userLoginRecord);

        List<UserLoginRecord> allRecords = userLoginRecordDao.listAll();
        assertEquals(1, allRecords.size());
        assertEquals(userLoginRecord.getUserId(), allRecords.get(0).getUserId());
        assertEquals(userLoginRecord.getLoginTime(), allRecords.get(0).getLoginTime());
    }

    @Test
    public void insertException() {
        userLoginRecord.setLoginTime(null);
        thrown.expect(ColumnNullPointerException.class);
        thrown.expectMessage("Table(user_login_record)'s column(login_time) is null!");
        userLoginRecordDao.insert(userLoginRecord);
    }

    @Test
    public void deleteAll() {
        userLoginRecordDao.insert(userLoginRecord);
        userLoginRecordDao.insert(userLoginRecord);

        int result = userLoginRecordDao.deleteAll();
        assertEquals(2, result);
    }

}