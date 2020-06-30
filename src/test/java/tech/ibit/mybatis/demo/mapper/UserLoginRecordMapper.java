package tech.ibit.mybatis.demo.mapper;

import tech.ibit.mybatis.template.mapper.NoIdMapper;
import tech.ibit.mybatis.demo.entity.UserLoginRecord;

/**
 * RawMapper for user_login_record
 *
 * @author IBIT程序猿
 */
public interface UserLoginRecordMapper extends NoIdMapper<UserLoginRecord> {

    @Override
    default Class<UserLoginRecord> getPoClazz() {
        return UserLoginRecord.class;
    }
}
