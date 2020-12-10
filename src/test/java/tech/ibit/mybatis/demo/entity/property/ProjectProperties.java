package tech.ibit.mybatis.demo.entity.property;

import tech.ibit.mybatis.sqlbuilder.Column;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * DbTable for sz_project
 *
 * @author iBit程序猿
 * @version 1.0
 */
public interface ProjectProperties {

    Table TABLE = new Table("project", "p");

    Column projectId = Column.getIdInstance(TABLE, "project_id", true);
    
    Column name = Column.getInstance(TABLE, "name", false);

}
