package tech.ibit.mybatis.test.entity;

import lombok.Data;
import tech.ibit.sqlbuilder.annotation.DbColumn;
import tech.ibit.sqlbuilder.annotation.DbTable;

import java.util.Date;

/**
 * Entity for user_login_record
 *
 * @author IBIT程序猿
 */
@Data
@DbTable(name = "user_login_record", alias = "ulr")
public class UserLoginRecord {

    /**
     * 用户id
     * VARCHAR(16)
     */
    @DbColumn(name = "user_id")
    private Integer userId;

    /**
     * 组织名称
     * TIMESTAMP(19)
     */
    @DbColumn(name = "login_time")
    private Date loginTime;

}
