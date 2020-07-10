package tech.ibit.mybatis.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ibit.mybatis.demo.entity.UserPo;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.mybatis.demo.service.UserService;
import tech.ibit.mybatis.sqlbuilder.sql.Page;
import tech.ibit.mybatis.utils.MapperUtils;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT程序猿
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
     * 通过用id获取用户po
     *
     * @param userId 用户id
     * @return 用户po
     */
    @Override
    public UserPo getPoById(Integer userId) {
        return MapperUtils.getPoById(mapper, UserPo.class, userId);
    }


}
