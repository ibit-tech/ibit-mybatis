package tech.ibit.mybatis.test.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ibit.sqlbuilder.MultiId;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * Entity for organization
 *
 * @author IBIT TECH
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@DbTable(name = "organization", alias = "o")
public class OrganizationKey implements MultiId {

    /**
     * 城市编码
     * VARCHAR(16)
     */
    @DbId(name = "city_code")
    private String cityCode;

    /**
     * 组织名称
     * VARCHAR(32)
     */
    @DbId(name = "name")
    private String name;

}
