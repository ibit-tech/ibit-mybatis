package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.NoIdMapper;
import tech.ibit.mybatis.demo.entity.UserLoginRecord;
import tech.ibit.mybatis.demo.entity.property.UserLoginRecordProperties;
import tech.ibit.mybatis.sqlbuilder.Table;

/**
 * RawMapper for user_login_record
 *
 * @author iBit程序猿
 */
public interface UserLoginRecordMapper extends NoIdMapper<UserLoginRecord> {

    @Override
    default Class<UserLoginRecord> getPoClazz() {
        return UserLoginRecord.class;
    }

    @Override
    default Table getDefaultTable() {
        return UserLoginRecordProperties.TABLE;
    }
}
