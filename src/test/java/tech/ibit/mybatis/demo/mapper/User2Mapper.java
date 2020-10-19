package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.MultipleIdMapper;
import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.UserKey;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * RawMapper for user
 *
 * @author iBit程序猿
 */
public interface User2Mapper extends MultipleIdMapper<User, UserKey> {

    @Override
    default Class<User> getPoClazz() {
        return User.class;
    }

    @Override
    default Table getDefaultTable() {
        return UserProperties.TABLE;
    }
}
