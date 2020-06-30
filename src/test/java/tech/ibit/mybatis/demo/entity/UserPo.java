package tech.ibit.mybatis.demo.entity;

import lombok.Data;
import tech.ibit.mybatis.demo.entity.type.UserType;
import tech.ibit.sqlbuilder.annotation.DbColumn;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * User po
 *
 * @author IBIT程序猿
 */
@Data
@DbTable(name = "user", alias = "u")
public class UserPo {

    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

    @DbColumn(name = "login_id", nullable = true)
    private String loginId;

    @DbColumn(name = "email")
    private String email;

    @DbColumn(name = "mobile_phone")
    private String mobilePhone;

    @DbColumn(name = "type")
    private UserType type;
}
