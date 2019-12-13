package tech.ibit.mybatis.test.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.ibit.mybatis.template.dao.impl.NoIdDaoImpl;
import tech.ibit.mybatis.template.mapper.Mapper;
import tech.ibit.mybatis.test.dao.UserLoginRecordDao;
import tech.ibit.mybatis.test.entity.UserLoginRecord;
import tech.ibit.mybatis.test.entity.property.UserLoginRecordProperties;
import tech.ibit.mybatis.test.mapper.UserLoginRecordMapper;
import tech.ibit.sqlbuilder.CriteriaItemMaker;
import tech.ibit.sqlbuilder.Sql;

import java.util.List;

/**
 * Dao for user_login_record
 *
 * @author IBIT TECH
 */
@Repository
public class UserLoginRecordDaoImpl extends NoIdDaoImpl<UserLoginRecord> implements UserLoginRecordDao {

    @Autowired
    private UserLoginRecordMapper mapper;

    @Override
    public Mapper<UserLoginRecord> getMapper() {
        return mapper;
    }

    @Override
    public Class<UserLoginRecord> getPoClazz() {
        return UserLoginRecord.class;
    }

    /**
     * 删除所有记录
     *
     * @return 删除条数
     */
    @Override
    public int deleteAll() {
        Sql sql = new Sql()
                .deleteFrom(UserLoginRecordProperties.TABLE)
                .andWhere(CriteriaItemMaker.greaterThan(UserLoginRecordProperties.userId, 0));
        return mapper.update(sql.getSqlParams());
    }

    /**
     * 列举所有记录
     *
     * @return 所有记录
     */
    @Override
    public List<UserLoginRecord> listAll() {
        Sql sql = new Sql()
                .selectPo(getPoClazz())
                .from(UserLoginRecordProperties.TABLE);
        return mapper.select(sql.getSqlParams());
    }

}
