package tech.ibit.mybatis.demo.entity;

import lombok.Data;
import tech.ibit.mybatis.demo.entity.type.UserType;

/**
 * 用户类型统计
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
@Data
public class UserTypeTotal {

    /**
     * 类型
     */
    private UserType type;

    /**
     * 总数
     */
    private Integer total;

}