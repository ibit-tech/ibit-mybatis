package tech.ibit.mybatis;

import tech.ibit.mybatis.demo.entity.Organization;
import tech.ibit.mybatis.demo.entity.OrganizationKey;
import tech.ibit.mybatis.demo.entity.property.OrganizationProperties;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * OrganizationTestMapper
 *
 * @author ben
 */
public class OrganizationTestMapper extends TestMapper<Organization>
        implements MultipleIdMapper<Organization, OrganizationKey> {

    @Override
    public Class<Organization> getPoClazz() {
        return Organization.class;
    }

    @Override
    public Table getDefaultTable() {
        return OrganizationProperties.TABLE;
    }
}
