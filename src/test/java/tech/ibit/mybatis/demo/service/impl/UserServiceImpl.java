package tech.ibit.mybatis.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ibit.mybatis.demo.entity.UserTypeTotal;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.mybatis.demo.service.UserService;
import tech.ibit.mybatis.sqlbuilder.sql.Page;

import java.util.List;

/**
 * Dao for user
 *
 * @author iBit程序猿
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    /**
     * 列举用户id
     *
     * @return 用户id列表
     */
    @Override
    public List<Integer> listUserIds() {
        return mapper.createQuery()
                .column(UserProperties.userId)
                .from(UserProperties.TABLE)
                .orderBy(UserProperties.userId.orderBy())
                .executeQueryDefault();
    }

    /**
     * 列举用户id
     *
     * @return 用户id列表
     */
    @Override
    public Page<Integer> listUserIdsWithTotal() {
        return mapper.createQuery()
                .column(UserProperties.userId)
                .from(UserProperties.TABLE)
                .orderBy(UserProperties.userId.orderBy())
                .executeQueryDefaultPage();
    }

    /**
     * 按照用户类型统计数量
     *
     * @return 用户类型统计
     */
    @Override
    public List<UserTypeTotal> listTypeTotals() {
        return mapper.createQuery()
                .column(UserProperties.type)
                .column(UserProperties.type.count("total"))
                .groupBy(UserProperties.type)
                .orderBy(UserProperties.type.orderBy())
                .executeQuery(UserTypeTotal.class);
    }
}
