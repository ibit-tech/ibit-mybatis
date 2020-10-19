package tech.ibit.mybatis.demo.ext.mapper;

import tech.ibit.mybatis.demo.entity.User;

import java.util.List;

/**
 * UserExtMapper
 *
 * @author iBit程序猿
 */
public interface UserExtMapper {

    List<User> listAllUsers();

    int deleteAllUsers();

}
