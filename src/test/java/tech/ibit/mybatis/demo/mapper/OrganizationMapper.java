package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.MultipleIdMapper;
import tech.ibit.mybatis.demo.entity.Organization;
import tech.ibit.mybatis.demo.entity.OrganizationKey;
import tech.ibit.mybatis.demo.entity.property.OrganizationProperties;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * RawMapper for organization
 *
 * @author iBit程序猿
 */
public interface OrganizationMapper extends MultipleIdMapper<Organization, OrganizationKey> {

    @Override
    default Class<Organization> getPoClazz() {
        return Organization.class;
    }

    @Override
    default Table getDefaultTable() {
        return OrganizationProperties.TABLE;
    }
}
