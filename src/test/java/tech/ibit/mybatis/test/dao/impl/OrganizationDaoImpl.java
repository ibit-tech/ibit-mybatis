package tech.ibit.mybatis.test.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.ibit.mybatis.template.dao.impl.MultipleIdDaoImpl;
import tech.ibit.mybatis.template.mapper.Mapper;
import tech.ibit.mybatis.test.dao.OrganizationDao;
import tech.ibit.mybatis.test.entity.Organization;
import tech.ibit.mybatis.test.entity.OrganizationKey;
import tech.ibit.mybatis.test.mapper.OrganizationMapper;

/**
 * Dao for organization
 *
 * @author IBIT程序猿
 */
@Repository
public class OrganizationDaoImpl
        extends MultipleIdDaoImpl<Organization, OrganizationKey> implements OrganizationDao {

    @Autowired
    private OrganizationMapper mapper;

    @Override
    public Mapper<Organization> getMapper() {
        return mapper;
    }

    @Override
    public Class<Organization> getPoClazz() {
        return Organization.class;
    }

}
