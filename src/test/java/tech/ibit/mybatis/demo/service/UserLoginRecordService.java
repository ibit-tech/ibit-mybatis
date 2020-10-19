package tech.ibit.mybatis.demo.service;

import tech.ibit.mybatis.demo.entity.UserLoginRecord;

import java.util.List;

/**
 * 用户登录记录 Service
 *
 * @author iBit程序猿
 */
public interface UserLoginRecordService {

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
