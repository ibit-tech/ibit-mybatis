package tech.ibit.mybatis.test.entity.property;

import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.Table;

/**
 * Table for user
 *
 * @author IBIT TECH
 */
public interface UserProperties {

    /**
     * 表名
     */
    Table TABLE = new Table("user", "u");

    /**
     * 用户id
     */
    Column userId = new Column(TABLE, "user_id");

    /**
     * 用户名称
     */
    Column name = new Column(TABLE, "name");

    /**
     * 登录id
     */
    Column loginId = new Column(TABLE, "login_id");

    /**
     * 邮箱
     */
    Column email = new Column(TABLE, "email");

    /**
     * 密码
     */
    Column password = new Column(TABLE, "password");

    /**
     * 手机
     */
    Column mobilePhone = new Column(TABLE, "mobile_phone");

    /**
     * 用户类型
     */
    Column type = new Column(TABLE, "type");


}
