package tech.ibit.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ibit.sqlbuilder.MultiId;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * 组织主键
 *
 * @author IBIT TECH
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@DbTable(name = "organization", alias = "o")
public class OrganizationKey implements MultiId {

    @DbId(name = "city_code")
    private String cityCode;

    @DbId(name = "name")
    private String name;
}
