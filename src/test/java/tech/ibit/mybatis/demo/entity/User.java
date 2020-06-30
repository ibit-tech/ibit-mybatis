package tech.ibit.mybatis.demo.entity;

import lombok.Data;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.sqlbuilder.annotation.DbColumn;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * Entity for user
 *
 * @author IBIT程序猿
 */
@Data
@DbTable(name = "user", alias = "u")
public class User {

    /**
     * 用户id
     * INT(10, 0)
     */
    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

    /**
     * 登录id
     * VARCHAR(32)
     */
    @DbColumn(name = "login_id", nullable = true)
    private String loginId;

    /**
     * 用户名称
     * VARCHAR(64)
     */
    @DbColumn(name = "name")
    private String name;

    /**
     * 邮箱
     * VARCHAR(200)
     */
    @DbColumn(name = "email")
    private String email;

    /**
     * 密码
     * VARCHAR(64)
     */
    @DbColumn(name = "password")
    private String password;

    /**
     * 手机
     * VARCHAR(16)
     */
    @DbColumn(name = "mobile_phone")
    private String mobilePhone;

    /**
     * 用户类型
     * INT(10, 0)
     */
    @DbColumn(name = "type")
    private UserType type;

}
