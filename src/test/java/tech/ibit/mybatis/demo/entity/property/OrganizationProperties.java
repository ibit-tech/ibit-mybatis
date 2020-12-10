package tech.ibit.mybatis.demo.entity.property;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * Table for organization
 *
 * @author iBit程序猿
 */
public interface OrganizationProperties {

    /**
     * 表名
     */
    Table TABLE = new Table("organization", "o");


    /**
     * 组织id
     */
    Column orgId = Column.getIdInstance(TABLE, "org_id", true);

    /**
     * 城市编码
     */
    Column cityCode = Column.getIdInstance(TABLE, "city_code");

    /**
     * 组织名称
     */
    Column name = Column.getIdInstance(TABLE, "name");

    /**
     * 组织类型
     */
    Column type = Column.getInstance(TABLE, "type");

    /**
     * 手机
     */
    Column phone = Column.getInstance(TABLE, "phone", true);


}
