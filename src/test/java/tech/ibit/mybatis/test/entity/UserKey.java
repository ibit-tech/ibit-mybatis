package tech.ibit.mybatis.test.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tech.ibit.sqlbuilder.MultiId;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * User主键
 *
 * @author IBIT TECH
 */
@AllArgsConstructor
@NoArgsConstructor
@DbTable(name = "user", alias = "u")
public class UserKey implements MultiId {

    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

}
