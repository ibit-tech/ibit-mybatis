package tech.ibit.mybatis.demo.entity.type;

import tech.ibit.mybatis.CommonEnum;

/**
 * 用户类型
 *
 * @author IBIT程序猿
 * mailto: ibit_tech@aliyun.com
 */
public enum UserType implements CommonEnum {

    u1(1),
    u2(2),
    u3(3),
    ;

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of value
     *
     * @return the value of value
     */
    @Override
    public int getValue() {
        return value;
    }


}
