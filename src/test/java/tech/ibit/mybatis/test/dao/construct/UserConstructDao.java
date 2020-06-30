package tech.ibit.mybatis.test.dao.construct;


import tech.ibit.mybatis.test.entity.UserPo;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT程序猿
 */
public interface UserConstructDao {

    /**
     * 列举用户id
     *
     * @return 用户id列表
     */
    List<Integer> listUserIds();

    /**
     * 通过用id获取用户po
     *
     * @param userId 用户id
     * @return 用户po
     */
    UserPo getPoById(Integer userId);
}
