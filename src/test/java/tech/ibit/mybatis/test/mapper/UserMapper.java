package tech.ibit.mybatis.test.mapper;

import tech.ibit.mybatis.template.mapper.SingleIdMapper;
import tech.ibit.mybatis.test.entity.User;

/**
 * RawMapper for user
 *
 * @author IBIT程序猿
 */
public interface UserMapper extends SingleIdMapper<User, Integer> {

    @Override
    default Class<User> getPoClazz() {
        return User.class;
    }

}
