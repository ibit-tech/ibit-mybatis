package tech.ibit.mybatis.demo.service;


import tech.ibit.mybatis.demo.entity.UserPo;
import tech.ibit.mybatis.sqlbuilder.sql.Page;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT程序猿
 */
public interface UserService {

    /**
     * 列举用户id
     *
     * @return 用户id列表
     */
    List<Integer> listUserIds();

    /**
     * 列举用户id
     *
     * @return 用户id列表
     */
    Page<Integer> listUserIdsWithTotal();

    /**
     * 通过用id获取用户po
     *
     * @param userId 用户id
     * @return 用户po
     */
    UserPo getPoById(Integer userId);
}
