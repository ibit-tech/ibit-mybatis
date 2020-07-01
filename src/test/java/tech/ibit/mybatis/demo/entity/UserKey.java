package tech.ibit.mybatis.demo.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tech.ibit.mybatis.sqlbuilder.MultiId;
import tech.ibit.mybatis.sqlbuilder.annotation.DbId;
import tech.ibit.mybatis.sqlbuilder.annotation.DbTable;

/**
 * User主键
 *
 * @author IBIT程序猿
 */
@AllArgsConstructor
@NoArgsConstructor
@DbTable(name = "user", alias = "u")
public class UserKey implements MultiId {

    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

}
