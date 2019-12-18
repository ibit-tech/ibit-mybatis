package tech.ibit.mybatis.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用枚举类定义（如果需要将类型转换为枚举类的字段，枚举类都要继承此类）
 *
 * @author IBIT-TECH
 */
public interface CommonEnum {

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    int getValue();

    /**
     * 获取枚举值对应的枚举
     *
     * @param enumClass 枚举类
     * @param enumValue 枚举值
     * @param <E>       枚举类型
     * @return 枚举
     */
    static <E extends CommonEnum> E getEnum(final Class<E> enumClass, final Integer enumValue) {
        if (enumValue == null) {
            return null;
        }
        try {
            return valueOf(enumClass, enumValue);
        } catch (final IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * 获取枚举值对应的枚举
     *
     * @param enumClass 枚举类
     * @param enumValue 枚举值
     * @param <E>       枚举类型
     * @return 枚举
     */
    static <E extends CommonEnum> E valueOf(Class<E> enumClass, Integer enumValue) {
        if (enumValue == null) {
            throw new NullPointerException("EnumValue is null");
        }
        return getEnumMap(enumClass).get(enumValue);
    }

    /**
     * 获取枚举键值对
     *
     * @param enumClass 枚举类型
     * @param <E>       枚举类型
     * @return 键值对
     */
    static <E extends CommonEnum> Map<Integer, E> getEnumMap(Class<E> enumClass) {
        E[] enums = enumClass.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(enumClass.getSimpleName() + " does not represent an enum type.");
        }
        Map<Integer, E> map = new HashMap<>(2 * enums.length);
        for (E t : enums) {
            map.put(t.getValue(), t);
        }
        return map;
    }
}