package tech.ibit.mybatis.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ibit.mybatis.demo.entity.UserLoginRecord;
import tech.ibit.mybatis.demo.entity.property.UserLoginRecordProperties;
import tech.ibit.mybatis.demo.mapper.UserLoginRecordMapper;
import tech.ibit.mybatis.demo.service.UserLoginRecordService;
import tech.ibit.mybatis.sqlbuilder.SqlFactory;

import java.util.List;

/**
 * @author iBit程序猿
 */
@Service
public class UserLoginRecordServiceImpl implements UserLoginRecordService {

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
                .andWhere(UserLoginRecordProperties.userId.gt(0)).executeDelete();
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
