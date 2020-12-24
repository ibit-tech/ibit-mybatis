package tech.ibit.mybatis.plugin.strategy;


import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 转化的策略
 *
 * @author iBit程序猿
 * @since 2.10
 */
public interface TransferStrategy {

    /**
     * 加密
     *
     * @param plainText 明文
     * @return 加密后的字符串
     */
    default String encrypt(String plainText) {
        return batchEncrypt(Collections.singleton(plainText)).get(plainText);
    }

    /**
     * 批量加密
     *
     * @param plainTextSet 明文集合
     * @return 加密后的map
     */
    Map<String, String> batchEncrypt(Set<String> plainTextSet);

    /**
     * 加密并保存
     *
     * @param plainText 明文
     * @return 加密后的字符串
     */
    default String encryptAndSaveText(String plainText) {
        return batchEncryptAndSaveText(Collections.singleton(plainText)).get(plainText);
    }

    /**
     * 批量加密并保存
     *
     * @param plainTextSet 明文集合
     * @return 加密后的map
     */
    default Map<String, String> batchEncryptAndSaveText(Set<String> plainTextSet) {
        Map<String, String> result = batchEncrypt(plainTextSet);
        Consumer<Map<String, String>> textSaver = getTextSaver();
        if (null != textSaver) {
            textSaver.accept(result);
        }
        return result;
    }


    /**
     * 解密
     *
     * @param cipherText 密文
     * @return 解密后的字符串
     */
    default String decrypt(String cipherText) {
        return batchEncrypt(Collections.singleton(cipherText)).get(cipherText);
    }


    /**
     * 批量解密
     *
     * @param cipherTextSet 密文集合
     * @return 解密后的map
     */
    Map<String, String> batchDecrypt(Set<String> cipherTextSet);

    /**
     * 获取文本保存器
     *
     * @return 文本保存器
     */
    Consumer<Map<String, String>> getTextSaver();
}
