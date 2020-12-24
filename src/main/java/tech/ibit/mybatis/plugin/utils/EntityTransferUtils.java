package tech.ibit.mybatis.plugin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.mybatis.plugin.annotation.Encrypt;
import tech.ibit.mybatis.plugin.strategy.TransferStrategy;
import tech.ibit.mybatis.utils.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * 实体加密工具类
 *
 * @author iBit程序猿
 * @since 2.10
 */
public class EntityTransferUtils {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityTransferUtils.class);

    /**
     * 工具类，构造方法私有
     */
    private EntityTransferUtils() {
    }

    /**
     * 定义遍历实体类继承深度
     */
    private static final int MAX_DEPTH = 3;

    /**
     * 加密
     *
     * @param object           待加密对象
     * @param transferStrategy 转换策略
     */
    public static void encrypt(Object object, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, transferStrategy::batchEncrypt);
    }


    /**
     * 加密
     *
     * @param objects          待加密对象列表
     * @param transferStrategy 转换策略
     */
    public static void encrypt(List<?> objects, TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects, transferStrategy::batchEncrypt);
    }

    /**
     * 加密
     *
     * @param object           待加密对象
     * @param propertyNames    加密属性名称列表
     * @param transferStrategy 转换策略
     */
    public static void encrypt(Object object, Set<String> propertyNames, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, propertyNames, transferStrategy::batchEncrypt);
    }


    /**
     * 加密
     *
     * @param objects          待加密对象列表
     * @param propertyNames    加密属性名称列表
     * @param transferStrategy 转换策略
     */
    public static void encrypt(List<?> objects, Set<String> propertyNames, TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects, propertyNames, transferStrategy::batchEncrypt);
    }


    /**
     * 加密并保存文本
     *
     * @param object           待加密对象
     * @param transferStrategy 转换策略
     */
    public static void encryptAndSaveText(Object object, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, transferStrategy::batchEncryptAndSaveText);
    }


    /**
     * 加密并保存文本
     *
     * @param objects          待加密对象列表
     * @param transferStrategy 转换策略
     */
    public static void encryptAndSaveText(List<?> objects, TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects, transferStrategy::batchEncryptAndSaveText);
    }

    /**
     * 加密并保存文本
     *
     * @param object           待加密对象
     * @param propertyNames    加密属性名
     * @param transferStrategy 转换策略
     */
    public static void encryptAndSaveText(Object object, Set<String> propertyNames, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, propertyNames, transferStrategy::batchEncryptAndSaveText);
    }


    /**
     * 加密并保存文本
     *
     * @param objects          待加密对象列表
     * @param propertyNames    加密属性名
     * @param transferStrategy 转换策略
     */
    public static void encryptAndSaveText(List<?> objects, Set<String> propertyNames
            , TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects,
                propertyNames, transferStrategy::batchEncryptAndSaveText);
    }


    /**
     * 解密
     *
     * @param object           待解密对象
     * @param transferStrategy 转换策略
     */
    public static void decrypt(Object object, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, transferStrategy::batchDecrypt);
    }

    /**
     * 解密
     *
     * @param objects          待解密对象列表
     * @param transferStrategy 转换策略
     */
    public static void decrypt(List<?> objects, TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects, transferStrategy::batchDecrypt);
    }

    /**
     * 解密
     *
     * @param object           待解密对象
     * @param propertyNames    加密属性名称列表
     * @param transferStrategy 转换策略
     */
    public static void decrypt(Object object, Set<String> propertyNames, TransferStrategy transferStrategy) {
        if (null == object || null == transferStrategy) {
            return;
        }
        transfer(object, propertyNames, transferStrategy::batchDecrypt);
    }

    /**
     * 解密
     *
     * @param objects          待解密对象列表
     * @param propertyNames    加密属性名称列表
     * @param transferStrategy 转换策略
     */
    public static void decrypt(List<?> objects, Set<String> propertyNames, TransferStrategy transferStrategy) {
        if (CollectionUtils.isEmpty(objects) || null == transferStrategy) {
            return;
        }
        transfer(objects, propertyNames, transferStrategy::batchDecrypt);
    }

    /**
     * 对象转化
     *
     * @param object                 待转化对象
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(Object object, TransferStrategyMethod transferStrategyMethod) {
        transfer(object,
                clazz -> clazz.isAnnotationPresent(Encrypt.class),
                field -> field.isAnnotationPresent(Encrypt.class) && field.getType() == String.class,
                transferStrategyMethod);
    }

    /**
     * 对象转化
     *
     * @param objects                待转化对象
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(List<?> objects, TransferStrategyMethod transferStrategyMethod) {
        transfer(objects,
                clazz -> clazz.isAnnotationPresent(Encrypt.class),
                field -> field.isAnnotationPresent(Encrypt.class) && field.getType() == String.class,
                transferStrategyMethod);
    }


    /**
     * 对象转化
     *
     * @param object                 待转化对象
     * @param propertyNames          属性名称
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(Object object, Set<String> propertyNames, TransferStrategyMethod transferStrategyMethod) {
        transfer(object,
                null,
                field -> propertyNames.contains(field.getName()) && field.getType() == String.class,
                transferStrategyMethod);
    }

    /**
     * 对象转化
     *
     * @param objects                待转化对象
     * @param propertyNames          属性名称
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(List<?> objects, Set<String> propertyNames, TransferStrategyMethod transferStrategyMethod) {
        transfer(objects,
                null,
                field -> propertyNames.contains(field.getName()) && field.getType() == String.class,
                transferStrategyMethod);
    }


    /**
     * 对象转化
     *
     * @param object                 待转化对象
     * @param classPredicate         类条件
     * @param fieldPredicate         字段接受条件
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(Object object, Predicate<Class<?>> classPredicate
            , Predicate<Field> fieldPredicate, TransferStrategyMethod transferStrategyMethod) {
        FieldMethods fieldMethods = getFieldMethods(object, classPredicate, fieldPredicate);
        if (fieldMethods == null) {
            return;
        }
        transfer(Collections.singletonList(object), transferStrategyMethod, fieldMethods);
    }

    /**
     * 对象转化
     *
     * @param objects                待转化对象
     * @param classPredicate         类条件
     * @param fieldPredicate         字段条件
     * @param transferStrategyMethod 转化方法
     */
    private static void transfer(List<?> objects, Predicate<Class<?>> classPredicate
            , Predicate<Field> fieldPredicate, TransferStrategyMethod transferStrategyMethod) {

        if (CollectionUtils.isEmpty(objects)) {
            return;
        }

        Object object = objects.get(0);
        FieldMethods fieldMethods = getFieldMethods(object, classPredicate, fieldPredicate);
        if (fieldMethods == null) {
            return;
        }
        transfer(objects, transferStrategyMethod, fieldMethods);
    }

    /**
     * 获取字段和方法映射
     *
     * @param object         对象
     * @param classPredicate 类断言
     * @param fieldPredicate 字段断言
     * @return 字段和方法映射
     */
    private static FieldMethods getFieldMethods(Object object
            , Predicate<Class<?>> classPredicate, Predicate<Field> fieldPredicate) {
        if (object instanceof Number || object instanceof String
                || object instanceof Collection || object instanceof Map) {
            return null;
        }

        Class<?> clazz = object.getClass();
        if (null != classPredicate && !classPredicate.test(clazz)) {
            return null;
        }

        FieldMethods fieldMethods = getFieldMethod(clazz, fieldPredicate);
        if (fieldMethods.getFieldGetterMethodMap().isEmpty() || fieldMethods.getFieldSetterMethodMap().isEmpty()) {
            return null;
        }
        return fieldMethods;
    }

    /**
     * 转化对象
     *
     * @param objectList             待转化对象列表
     * @param transferStrategyMethod 转化方法
     * @param fieldMethods           字段方法
     */
    private static void transfer(List<?> objectList
            , TransferStrategyMethod transferStrategyMethod, FieldMethods fieldMethods) {
        Map<String, Method> fieldGetterMethodMap = fieldMethods.getFieldGetterMethodMap();
        Map<String, Method> fieldSetterMethodMap = fieldMethods.getFieldSetterMethodMap();
        List<Map<String, String>> fieldTextList = new ArrayList<>();
        Set<String> textSet = new HashSet<>();

        //  读到所有内容
        objectList.forEach(object -> {
            Map<String, String> fieldText = new HashMap<>();
            fieldGetterMethodMap.forEach((fieldName, getterMethod) -> {
                if (null == getterMethod) {
                    LOGGER.warn("Cannot find getter method by ({})!", fieldName);
                    return;
                }
                try {
                    // 原始值
                    String originalText = (String) getterMethod.invoke(object);
                    fieldText.put(fieldName, originalText);
                    textSet.add(originalText);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Error to get field value by ({})'s ({})", fieldName, getterMethod.getName());
                }
            });
            fieldTextList.add(fieldText);
        });

        // 转换文本之后，再统一写入
        Map<String, String> transferTextMap = transferStrategyMethod.transfer(textSet);
        for (int i = 0; i < objectList.size(); i++) {
            Object object = objectList.get(i);
            Map<String, String> fieldText = fieldTextList.get(i);
            fieldSetterMethodMap.forEach((fieldName, setterMethod) -> {

                if (null == setterMethod) {
                    LOGGER.warn("Cannot find setter method by ({})!", fieldName);
                    return;
                }
                // 原始值
                String originalText = fieldText.get(fieldName);

                // 转化并回写
                String transferText = transferTextMap.get(originalText);
                try {
                    setterMethod.invoke(object, transferText);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Error to update field value by ({})'s ({})", fieldName, setterMethod.getName());
                }
            });

        }
    }

    /**
     * 解析加密字段方法
     *
     * @param clazz 类
     * @return 加密字段方法
     */
    private static FieldMethods getFieldMethod(Class<?> clazz, Predicate<Field> predicate) {

        Map<String, Method> fieldGetterMethodMap = new HashMap<>(10);
        Map<String, Method> fieldSetterMethodMap = new HashMap<>(10);


        int depth = 0;
        while (isContinue(clazz, depth)) {
            depth++;
            Class<?> finalClazz = clazz;
            Arrays.stream(finalClazz.getDeclaredFields())
                    .filter(predicate)
                    .forEach(field -> {
                        String fieldName = field.getName();
                        fieldGetterMethodMap.put(fieldName, MethodUtils.getGetterMethod(finalClazz, field));
                        fieldSetterMethodMap.put(fieldName, MethodUtils.getSetterMethod(finalClazz, field));
                    });
            clazz = clazz.getSuperclass();

        }

        return new FieldMethods(fieldGetterMethodMap, fieldSetterMethodMap);
    }


    /**
     * 判断是否继续遍历
     *
     * @param clazz 类
     * @param depth 当前层级
     * @return 判断结果
     */
    private static boolean isContinue(Class<?> clazz, int depth) {
        return clazz != Object.class && depth < MAX_DEPTH;
    }


    /**
     * 字段方法映射类
     */
    static class FieldMethods {

        /**
         * Getter方法map
         */
        Map<String, Method> fieldGetterMethodMap;

        /**
         * Setter方法map
         */
        Map<String, Method> fieldSetterMethodMap;

        public FieldMethods(Map<String, Method> fieldGetterMethodMap, Map<String, Method> fieldSetterMethodMap) {
            this.fieldGetterMethodMap = fieldGetterMethodMap;
            this.fieldSetterMethodMap = fieldSetterMethodMap;
        }

        /**
         * Gets the value of fieldGetterMethodMap
         *
         * @return the value of fieldGetterMethodMap
         */
        public Map<String, Method> getFieldGetterMethodMap() {
            return fieldGetterMethodMap;
        }

        /**
         * Gets the value of fieldSetterMethodMap
         *
         * @return the value of fieldSetterMethodMap
         */
        public Map<String, Method> getFieldSetterMethodMap() {
            return fieldSetterMethodMap;
        }
    }

    /**
     * 转换方法
     */
    @FunctionalInterface
    public interface TransferStrategyMethod {

        /**
         * 转化字符串
         *
         * @param textSet 转换前文本集合
         * @return 转换后文本Map
         */
        Map<String, String> transfer(Set<String> textSet);

    }


}
