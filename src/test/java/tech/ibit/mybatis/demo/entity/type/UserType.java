package tech.ibit.mybatis.demo.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.ibit.mybatis.CommonEnum;

/**
 * 用户类型
 *
 * @author IBIT程序猿
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
