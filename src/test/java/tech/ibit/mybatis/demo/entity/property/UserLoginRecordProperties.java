package tech.ibit.mybatis.demo.entity.property;


import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * Table for user_login_record
 *
 * @author iBit程序猿
 */
public interface UserLoginRecordProperties {

    /**
     * 表对象
     */
    Table TABLE = new Table("user_login_record", "ulr");

    /**
     * 用户id
     */
    Column userId = Column.getInstance(TABLE, "user_id", false);

    /**
     * 组织名称
     */
    Column loginTime = Column.getInstance(TABLE, "login_time", false);


}
