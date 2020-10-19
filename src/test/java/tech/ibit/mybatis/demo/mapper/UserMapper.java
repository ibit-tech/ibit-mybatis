package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.SingleIdMapper;
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * RawMapper for user
 *
 * @author iBit程序猿
 */
public interface UserMapper extends SingleIdMapper<User, Integer> {

    @Override
    default Class<User> getPoClazz() {
        return User.class;
    }

    @Override
    default Table getDefaultTable() {
        return UserProperties.TABLE;
    }

    @Override
    default Column getId() {
        return UserProperties.userId;
    }
}
