package tech.ibit.mybatis.test.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.ibit.mybatis.type.CommonEnum;

/**
 * 用户类型
 *
 * @author IBIT-TECH
 * mailto: ibit_tech@aliyun.com
 */
@Getter
@AllArgsConstructor
public enum UserType implements CommonEnum {

    u1(1),
    u2(2),
    u3(3),
    ;

    private int value;
}
