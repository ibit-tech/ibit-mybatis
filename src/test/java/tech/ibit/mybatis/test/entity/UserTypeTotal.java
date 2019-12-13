package tech.ibit.mybatis.test.entity;

import lombok.Data;

/**
 * 用户类型统计
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
@Data
public class UserTypeTotal {

    /**
     * 类型
     */
    private Integer type;

    /**
     * 总数
     */
    private Integer total;

}
