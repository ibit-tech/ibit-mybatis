package tech.ibit.mybatis;


import lombok.Data;
import tech.ibit.sqlbuilder.annotation.DbColumn;
import tech.ibit.sqlbuilder.annotation.DbId;
import tech.ibit.sqlbuilder.annotation.DbTable;

/**
 * 组织
 *
 * @author IBIT TECH
 */
@Data
@DbTable(name = "organization", alias = "o")
public class Organization {

    @DbId(name = "city_code")
    private String cityCode;

    @DbId(name = "name")
    private String name;

    @DbColumn(name = "type")
    private Integer type;

    @DbColumn(name = "phone", nullable = true)
    private String phone;
}
