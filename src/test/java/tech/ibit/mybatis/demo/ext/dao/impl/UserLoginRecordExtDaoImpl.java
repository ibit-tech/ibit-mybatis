package tech.ibit.mybatis.demo.ext.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.ibit.mybatis.demo.ext.dao.UserLoginRecordExtDao;
import tech.ibit.mybatis.demo.entity.UserLoginRecord;
import tech.ibit.mybatis.demo.entity.property.UserLoginRecordProperties;
import tech.ibit.mybatis.demo.mapper.UserLoginRecordMapper;
import tech.ibit.sqlbuilder.SqlFactory;

import java.util.List;

/**
 * Dao for user_login_record
 *
 * @author IBIT程序猿
 */
@Repository
public class UserLoginRecordExtDaoImpl implements UserLoginRecordExtDao {

    @Autowired
    private UserLoginRecordMapper mapper;

    /**
     * 删除所有记录
     *
     * @return 删除条数
     */
    @Override
    public int deleteAll() {
        return SqlFactory.createDelete(mapper)
                .deleteFrom(UserLoginRecordProperties.TABLE)
                .andWhere(UserLoginRecordProperties.userId.gt(0)).doDelete();
    }

    /**
     * 列举所有记录
     *
     * @return 所有记录
     */
    @Override
    public List<UserLoginRecord> listAll() {
        return SqlFactory.createQuery(mapper)
                .columnPo(mapper.getPoClazz())
                .from(UserLoginRecordProperties.TABLE).executeQuery();
    }

}
