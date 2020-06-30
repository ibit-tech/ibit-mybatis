package tech.ibit.mybatis.demo.ext.dao;

import tech.ibit.mybatis.demo.entity.UserLoginRecord;

import java.util.List;

/**
 * Dao for user_login_record
 *
 * @author IBIT程序猿
 */
public interface UserLoginRecordExtDao {

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
