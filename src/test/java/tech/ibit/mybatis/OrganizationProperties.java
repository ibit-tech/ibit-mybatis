package tech.ibit.mybatis;

import tech.ibit.sqlbuilder.Column;
import tech.ibit.sqlbuilder.Table;

/**
 * Table for organization
 *
 * @author IBIT TECH
 */
public interface OrganizationProperties {

    Table TABLE = new Table("organization", "o");

    Column cityCode = new Column(TABLE, "city_code");
    Column name = new Column(TABLE, "name");
    Column type = new Column(TABLE, "type");
    Column phone = new Column(TABLE, "phone");

}
