package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.template.mapper.MultipleIdMapper;
import tech.ibit.mybatis.demo.entity.Organization;
import tech.ibit.mybatis.demo.entity.OrganizationKey;

/**
 * RawMapper for organization
 *
 * @author IBIT程序猿
 */
public interface OrganizationMapper extends MultipleIdMapper<Organization, OrganizationKey> {

    @Override
    default Class<Organization> getPoClazz() {
        return Organization.class;
    }
}
