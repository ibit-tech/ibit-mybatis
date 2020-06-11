package tech.ibit.mybatis.template.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import tech.ibit.mybatis.test.dao.OrganizationDao;
import tech.ibit.mybatis.test.entity.Organization;
import tech.ibit.mybatis.test.entity.OrganizationKey;
import tech.ibit.mybatis.test.entity.property.OrganizationProperties;
import tech.ibit.sqlbuilder.exception.SqlException;
import tech.ibit.sqlbuilder.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 多主键dao测试
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class MultipleIdDaoTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private OrganizationDao organizationDao;

    private List<Organization> testOrganizations;

    @Before
    public void setUp() {
        testOrganizations = new ArrayList<>();
    }

    @After
    public void tearDown() {
        if (CollectionUtils.isNotEmpty(testOrganizations)) {
            testOrganizations.forEach(organization -> {
                organizationDao.deleteById(new OrganizationKey(organization.getCityCode(), organization.getName()));
            });
        }
    }

    @Test
    public void insert() {
        Organization organization = insertOrganization();
        assertEquals(organization, organizationDao.getById(new OrganizationKey(organization.getCityCode(), organization.getName())));
    }

    @Test
    public void insert2() {
        insertOrganization();
        thrown.expect(DuplicateKeyException.class);
        insertOrganization();
    }

    @Test
    public void insertException() {
        Organization organization = getOrganization();
        organization.setCityCode(null);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(city_code) is null!");
        organizationDao.insert(organization);
    }

    @Test
    public void insertException2() {
        Organization organization = getOrganization();
        organization.setType(null);
        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        organizationDao.insert(organization);
    }

    @Test
    public void deleteById() {
        int result = organizationDao.deleteById(new OrganizationKey("0000", ""));
        assertEquals(0, result);

        Organization organization = insertOrganization();
        result = organizationDao.deleteById(new OrganizationKey(organization.getCityCode(), organization.getName()));
        assertEquals(1, result);
    }

    @Test
    public void deleteByIds() {
        int result = organizationDao.deleteByIds(Arrays.asList(
                new OrganizationKey("0000", "0001"),
                new OrganizationKey("0000", "0002")
        ));
        assertEquals(0, result);

        Organization organization = insertOrganization();
        result = organizationDao.deleteByIds(Arrays.asList(
                new OrganizationKey("0000", "0001"),
                new OrganizationKey(organization.getCityCode(), organization.getName())
        ));
        assertEquals(1, result);
    }

    @Test
    public void updateById() {
        Organization organization = insertOrganization();

        Organization organizationUpdate = new Organization();
        organizationUpdate.setCityCode(organization.getCityCode());
        organizationUpdate.setName(organization.getName());
        organizationUpdate.setPhone("1190");
        organizationDao.updateById(organizationUpdate);


        Organization organization1 = organizationDao.getById(new OrganizationKey(organization.getCityCode(), organization.getName()));
        organization.setPhone("1190");
        assertEquals(organization, organization1);
    }

    @Test
    public void updateById1() {

        Organization organization = insertOrganization();

        Organization organizationUpdate = new Organization();
        organizationUpdate.setCityCode(organization.getCityCode());
        organizationUpdate.setName(organization.getName());
        organizationUpdate.setType(3);
        organizationUpdate.setPhone("1190");
        organizationDao.updateById(organizationUpdate, Collections.singletonList(OrganizationProperties.type));

        Organization organization1 = organizationDao.getById(new OrganizationKey(organization.getCityCode(), organization.getName()));
        organization.setType(3);
        assertEquals(organization, organization1);

        organizationUpdate.setPhone(null);
        organizationDao.updateById(organizationUpdate, Arrays.asList(OrganizationProperties.phone, OrganizationProperties.type));

        organization1 = organizationDao.getById(new OrganizationKey(organization.getCityCode(), organization.getName()));
        organization.setPhone(""); // h2会将值为NULL的字符串转为""
        assertEquals(organization, organization1);
    }


    @Test
    public void updateByIdException2() {

        Organization organization = insertOrganization();

        Organization organizationUpdate = new Organization();
        organizationUpdate.setCityCode(organization.getCityCode());
        organizationUpdate.setName(null);

        thrown.expect(SqlException.class);
        organizationDao.updateById(organizationUpdate, Collections.singletonList(OrganizationProperties.name));
    }


    @Test
    public void updateByIdException3() {

        Organization organization = insertOrganization();

        Organization organizationUpdate = new Organization();
        organizationUpdate.setCityCode(organization.getCityCode());
        organizationUpdate.setName(organization.getName());

        thrown.expect(SqlException.class);
        thrown.expectMessage("Table(organization)'s column(type) is null!");
        organizationDao.updateById(organizationUpdate, Collections.singletonList(OrganizationProperties.type));
    }



    @Test
    public void updateByIds() {
        Organization organization0 = insertOrganization();
        Organization organization1 = insertOrganization1();


        Organization organizationUpdate = new Organization();
        organizationUpdate.setType(3);
        organizationDao.updateByIds(organizationUpdate,
                Arrays.asList(
                        new OrganizationKey(organization0.getCityCode(), organization0.getName()),
                        new OrganizationKey(organization1.getCityCode(), organization1.getName()))
        );

        List<Organization> organizations = organizationDao.getByIds(Arrays.asList(
                new OrganizationKey(organization0.getCityCode(), organization0.getName()),
                new OrganizationKey(organization1.getCityCode(), organization1.getName())));
        organization0.setType(3);
        organization1.setType(3);
        assertEquals(organization0, organizations.get(0));
        assertEquals(organization1, organizations.get(1));
    }

    @Test
    public void updateByIds1() {

        Organization organization0 = insertOrganization();
        Organization organization1 = insertOrganization1();

        Organization organizationUpdate = new Organization();
        organizationUpdate.setType(3);
        organizationDao.updateByIds(organizationUpdate,
                Collections.singletonList(OrganizationProperties.phone),
                Arrays.asList(
                        new OrganizationKey(organization0.getCityCode(), organization0.getName()),
                        new OrganizationKey(organization1.getCityCode(), organization1.getName()))
        );
        List<Organization> organizations = organizationDao.getByIds(Arrays.asList(
                new OrganizationKey(organization0.getCityCode(), organization0.getName()),
                new OrganizationKey(organization1.getCityCode(), organization1.getName())));
        organization0.setPhone("");
        organization1.setPhone("");
        assertEquals(organization0, organizations.get(0));
        assertEquals(organization1, organizations.get(1));
    }

    @Test
    public void getById() {
        Organization organization = insertOrganization();
        Organization organization1 = organizationDao.getById(new OrganizationKey(organization.getCityCode(), organization.getName()));
        assertEquals(organization, organization1);
    }

    @Test
    public void getByIds() {
        Organization organization0 = insertOrganization();
        Organization organization1 = insertOrganization1();

        List<Organization> organizations = organizationDao.getByIds(
                Arrays.asList(
                        new OrganizationKey(organization0.getCityCode(), organization0.getName()),
                        new OrganizationKey(organization1.getCityCode(), organization1.getName())));
        assertEquals(organization0, organizations.get(0));
        assertEquals(organization1, organizations.get(1));
    }

    private Organization getOrganization() {
        Organization organization = new Organization();
        organization.setCityCode("0001");
        organization.setName("IBIT");
        organization.setPhone("100");
        organization.setType(1);
        return organization;
    }

    private Organization insertOrganization() {
        Organization organization = getOrganization();
        organizationDao.insert(organization);
        testOrganizations.add(organization);
        return organization;
    }

    private Organization getOrganization1() {
        Organization organization = new Organization();
        organization.setCityCode("0001");
        organization.setName("TECH");
        organization.setPhone("100");
        organization.setType(1);
        return organization;
    }

    private Organization insertOrganization1() {
        Organization organization = getOrganization1();
        organizationDao.insert(organization);
        testOrganizations.add(organization);
        return organization;
    }
}