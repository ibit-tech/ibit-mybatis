package tech.ibit.mybatis.demo.entity.property;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * Table for user
 *
 * @author iBit程序猿
 */
public interface UserProperties {

    /**
     * 表名
     */
    Table TABLE = new Table("user", "u");

    /**
     * 用户id
     */
    Column userId = Column.getIdInstance(TABLE, "user_id", true);

    /**
     * 用户名称
     */
    Column name = Column.getInstance(TABLE, "name");

    /**
     * 登录id
     */
    Column loginId = Column.getInstance(TABLE, "login_id", true);

    /**
     * 邮箱
     */
    Column email = Column.getInstance(TABLE, "email");

    /**
     * 密码
     */
    Column password = Column.getInstance(TABLE, "password");

    /**
     * 手机
     */
    Column mobilePhone = Column.getInstance(TABLE, "mobile_phone");

    /**
     * 用户类型
     */
    Column type = Column.getInstance(TABLE, "type");

    /**
     * 组织id
     */
    Column orgId = Column.getInstance(TABLE, "org_id");

    /**
     * 当前项目
     */
    Column currentProjectId = Column.getInstance(TABLE, "current_project_id");

    /**
     * 年龄
     */
    Column age = Column.getInstance(TABLE, "age");

    /**
     * 性别
     */
    Column gender = Column.getInstance(TABLE, "gender");

    /**
     * 头像
     */
    Column avatarId = Column.getInstance(TABLE, "avatar_id");

    /**
     * 登录次数
     */
    Column loginTimes = Column.getInstance(TABLE, "login_times");


}
