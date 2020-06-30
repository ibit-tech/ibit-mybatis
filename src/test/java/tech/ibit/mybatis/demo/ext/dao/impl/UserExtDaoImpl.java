package tech.ibit.mybatis.demo.ext.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.ibit.mybatis.MapperUtils;
import tech.ibit.mybatis.demo.entity.UserPo;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.demo.ext.dao.UserExtDao;
import tech.ibit.mybatis.demo.mapper.UserMapper;
import tech.ibit.sqlbuilder.sql.Page;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT程序猿
 */
@Repository
public class UserExtDaoImpl implements UserExtDao {

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
