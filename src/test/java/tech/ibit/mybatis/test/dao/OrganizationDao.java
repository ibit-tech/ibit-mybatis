package tech.ibit.mybatis.test.dao;


import tech.ibit.mybatis.template.dao.MultipleIdDao;
import tech.ibit.mybatis.test.entity.Organization;
import tech.ibit.mybatis.test.entity.OrganizationKey;

/**
 * Dao for organization
 *
 * @author IBIT程序猿
 */
public interface OrganizationDao extends MultipleIdDao<Organization, OrganizationKey> {

}
