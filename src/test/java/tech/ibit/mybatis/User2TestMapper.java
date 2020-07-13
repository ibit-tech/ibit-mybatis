package tech.ibit.mybatis;

import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.demo.entity.UserKey;
import tech.ibit.mybatis.demo.entity.property.UserProperties;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * User2TestMapper
 *
 * @author ben
 */
public class User2TestMapper extends TestMapper<User> implements MultipleIdMapper<User, UserKey> {

    @Override
    public Class<User> getPoClazz() {
        return User.class;
    }

    @Override
    public Table getDefaultTable() {
        return UserProperties.TABLE;
    }

}
