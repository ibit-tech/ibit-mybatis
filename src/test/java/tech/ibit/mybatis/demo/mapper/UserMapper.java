package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.demo.entity.User;
import tech.ibit.mybatis.SingleIdMapper;

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
