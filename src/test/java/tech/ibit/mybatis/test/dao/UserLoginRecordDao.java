package tech.ibit.mybatis.test.dao;


import tech.ibit.mybatis.template.dao.NoIdDao;
import tech.ibit.mybatis.test.entity.UserLoginRecord;

import java.util.List;

/**
 * Dao for user_login_record
 *
 * @author IBIT程序猿
 */
public interface UserLoginRecordDao extends NoIdDao<UserLoginRecord> {

    /**
     * 删除所有记录
     *
     * @return 删除条数
     */
    int deleteAll();

    /**
     * 列举所有记录
     *
     * @return 所有记录
     */
    List<UserLoginRecord> listAll();
}
