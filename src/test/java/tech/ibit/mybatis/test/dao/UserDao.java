package tech.ibit.mybatis.test.dao;


import tech.ibit.mybatis.template.dao.SingleIdDao;
import tech.ibit.mybatis.test.entity.User;
import tech.ibit.mybatis.test.entity.UserPo;
import tech.ibit.mybatis.test.entity.UserTypeTotal;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT TECH
 */
public interface UserDao extends SingleIdDao<User, Integer> {

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

    /**
     * 按照用户类型统计数量
     *
     * @return 用户类型统计
     */
    List<UserTypeTotal> listTypeTotals();
}
