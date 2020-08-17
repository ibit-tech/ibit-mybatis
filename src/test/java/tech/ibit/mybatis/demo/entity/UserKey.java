package tech.ibit.mybatis.demo.entity;

import tech.ibit.mybatis.sqlbuilder.MultiId;
import tech.ibit.mybatis.sqlbuilder.annotation.DbId;
import tech.ibit.mybatis.sqlbuilder.annotation.DbTable;

/**
 * User主键
 *
 * @author IBIT程序猿
 */
@DbTable(name = "user", alias = "u")
public class UserKey implements MultiId {

    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

    public UserKey(Integer userId) {
        this.userId = userId;
    }

    public UserKey() {
    }

    /**
     * Gets the value of userId
     *
     * @return the value of userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the userId
     * <p>You can use getUserId() to get the value of userId</p>
     *
     * @param userId userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
