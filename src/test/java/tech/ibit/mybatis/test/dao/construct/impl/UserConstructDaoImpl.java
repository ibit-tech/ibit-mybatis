package tech.ibit.mybatis.test.dao.construct.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.ibit.mybatis.MapperDaoUtils;
import tech.ibit.mybatis.test.dao.construct.UserConstructDao;
import tech.ibit.mybatis.test.entity.UserPo;
import tech.ibit.mybatis.test.entity.property.UserProperties;
import tech.ibit.mybatis.test.mapper.UserMapper;
import tech.ibit.sqlbuilder.OrderBy;
import tech.ibit.sqlbuilder.sql.Page;

import java.util.List;

/**
 * Dao for user
 *
 * @author IBIT程序猿
 */
@Repository
public class UserConstructDaoImpl implements UserConstructDao {

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
                .orderBy(new OrderBy(UserProperties.userId))
                .doQueryDefault();
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
                .orderBy(new OrderBy(UserProperties.userId))
                .doQueryDefaultPage();
    }

    /**
     * 通过用id获取用户po
     *
     * @param userId 用户id
     * @return 用户po
     */
    @Override
    public UserPo getPoById(Integer userId) {
        return MapperDaoUtils.getPoById(mapper, UserPo.class, userId);
    }


}
