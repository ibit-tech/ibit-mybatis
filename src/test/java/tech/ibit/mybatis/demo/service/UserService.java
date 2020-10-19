package tech.ibit.mybatis.demo.service;


import tech.ibit.mybatis.demo.entity.UserTypeTotal;
import tech.ibit.mybatis.sqlbuilder.sql.Page;

import java.util.List;

/**
 * Dao for user
 *
 * @author iBit程序猿
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
     * 按照用户类型统计数量
     *
     * @return 用户类型统计
     */
    List<UserTypeTotal> listTypeTotals();
}
