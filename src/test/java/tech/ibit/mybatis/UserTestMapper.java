package tech.ibit.mybatis;

import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * UserTestMapper
 *
 * @author ben
 */
public class UserTestMapper extends TestMapper<User> implements SingleIdMapper<User, Integer> {

    @Override
    public Column getId() {
        return UserProperties.userId;
    }

    @Override
    public Class<User> getPoClazz() {
        return User.class;
    }

    @Override
    public Table getDefaultTable() {
        return UserProperties.TABLE;
    }

}
