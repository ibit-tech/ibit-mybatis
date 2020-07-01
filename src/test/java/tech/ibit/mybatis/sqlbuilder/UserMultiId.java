package tech.ibit.mybatis.sqlbuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.ibit.mybatis.sqlbuilder.annotation.DbId;
import tech.ibit.mybatis.sqlbuilder.annotation.DbTable;

/**
 * @author IBIT程序猿
 * @version 1.0
 */
@Data
@DbTable(name = "user", alias = "u")
@AllArgsConstructor
public class UserMultiId implements MultiId {

    @DbId(name = "user_id", autoIncrease = true)
    private Integer userId;

    public UserMultiId() {
    }
}