package tech.ibit.mybatis.plugin.utils;

import org.junit.Assert;
import org.junit.Test;
import tech.ibit.common.collection.CollectionUtils;
import tech.ibit.common.crypto.AesUtils;
import tech.ibit.mybatis.plugin.annotation.Encrypt;
import tech.ibit.mybatis.plugin.strategy.TransferStrategy;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * EntityTransferUtilsTest
 *
 * @author iBit程序猿
 */
public class EntityTransferUtilsTest {

    private final String key = "ibit-mybatis";

    private final String email = "ibit@ibit.tech";

    private final String cipherEmail = "idxsdEwdl+IfFOWx5Wfvsw==";

    private final String password = "12345678";

    private final String cipherPassword = "eKXPFx2eLsTwn+mWnZmKcw==";

    private final String idCardNo = "567890099876";

    private final String cipherIdCardNo = "NBioZJYPL5tu3BbciLSwNw==";

    private TransferStrategy strategy = new AesStrategy();


    @Test
    public void encrypt() {

        //  整体加密，单个对象
        User user = new User("iBit程序猿", email, password, idCardNo);
        EntityTransferUtils.encrypt(user, strategy);
        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(AesUtils.encrypt(email, key), user.getEmail());
        assertEquals(AesUtils.encrypt(password, key), user.getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), user.getIdCardNo());

        List<User> users = Arrays.asList(
                new User("iBit", email, password, idCardNo),
                new User("程序猿", email, password, idCardNo)
        );

        // 整体加密，列表
        EntityTransferUtils.encrypt(users, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(AesUtils.encrypt(email, key), users.get(0).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(0).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(AesUtils.encrypt(email, key), users.get(1).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(1).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(1).getIdCardNo());

        // 加密，指定属性
        Set<String> propertyNames = new HashSet<>(Arrays.asList("password", "idCardNo"));

        // 指定属性，单个对象
        user = new User("iBit程序猿", email, password, idCardNo);
        EntityTransferUtils.encrypt(user, propertyNames, strategy);
        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(AesUtils.encrypt(password, key), user.getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), user.getIdCardNo());

        // 执行属性，对象列表
        users = Arrays.asList(
                new User("iBit", email, password, idCardNo),
                new User("程序猿", email, password, idCardNo)
        );

        EntityTransferUtils.encrypt(users, propertyNames, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(email, users.get(0).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(0).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(email, users.get(1).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(1).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(1).getIdCardNo());

    }

    @Test
    public void encryptAndSaveText() {
        //  整体加密，单个对象
        User user = new User("iBit程序猿", email, password, idCardNo);
        EntityTransferUtils.encryptAndSaveText(user, strategy);
        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(AesUtils.encrypt(email, key), user.getEmail());
        assertEquals(AesUtils.encrypt(password, key), user.getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), user.getIdCardNo());

        List<User> users = Arrays.asList(
                new User("iBit", email, password, idCardNo),
                new User("程序猿", email, password, idCardNo)
        );

        // 整体加密，列表
        EntityTransferUtils.encryptAndSaveText(users, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(AesUtils.encrypt(email, key), users.get(0).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(0).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(AesUtils.encrypt(email, key), users.get(1).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(1).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(1).getIdCardNo());

        // 加密，指定属性
        Set<String> propertyNames = new HashSet<>(Arrays.asList("password", "idCardNo"));

        // 指定属性，单个对象
        user = new User("iBit程序猿", email, password, idCardNo);
        EntityTransferUtils.encryptAndSaveText(user, propertyNames, strategy);
        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(AesUtils.encrypt(password, key), user.getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), user.getIdCardNo());

        // 执行属性，对象列表
        users = Arrays.asList(
                new User("iBit", email, password, idCardNo),
                new User("程序猿", email, password, idCardNo)
        );

        EntityTransferUtils.encryptAndSaveText(users, propertyNames, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(email, users.get(0).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(0).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(email, users.get(1).getEmail());
        assertEquals(AesUtils.encrypt(password, key), users.get(1).getPassword());
        assertEquals(AesUtils.encrypt(idCardNo, key), users.get(1).getIdCardNo());
    }

    @Test
    public void decrypt() {
        // 整体解密，单个对象
        User user = new User("iBit程序猿", cipherEmail, cipherPassword, cipherIdCardNo);
        EntityTransferUtils.decrypt(user, strategy);

        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(AesUtils.decrypt(cipherEmail, key), user.getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), user.getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), user.getIdCardNo());

        // 整体解密，对象列表
        List<User> users = Arrays.asList(
                new User("iBit", cipherEmail, cipherPassword, cipherIdCardNo),
                new User("程序猿", cipherEmail, cipherPassword, cipherIdCardNo));
        EntityTransferUtils.decrypt(users, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(AesUtils.decrypt(cipherEmail, key), users.get(0).getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), users.get(0).getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(AesUtils.decrypt(cipherEmail, key), users.get(1).getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), users.get(1).getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), users.get(1).getIdCardNo());

        Set<String> propertyNames = new HashSet<>(Arrays.asList("password", "idCardNo"));

        // 指定属性解密，单个对象
        user = new User("iBit程序猿", cipherEmail, cipherPassword, cipherIdCardNo);
        EntityTransferUtils.decrypt(user, propertyNames, strategy);

        assertEquals("iBit程序猿", user.getUsername());
        assertEquals(cipherEmail, user.getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), user.getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), user.getIdCardNo());


        // 指定属性解密，列表
        users = Arrays.asList(
                new User("iBit", cipherEmail, cipherPassword, cipherIdCardNo),
                new User("程序猿", cipherEmail, cipherPassword, cipherIdCardNo));
        EntityTransferUtils.decrypt(users, propertyNames, strategy);
        assertEquals("iBit", users.get(0).getUsername());
        assertEquals(cipherEmail, users.get(0).getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), users.get(0).getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), users.get(0).getIdCardNo());

        assertEquals("程序猿", users.get(1).getUsername());
        assertEquals(cipherEmail, users.get(1).getEmail());
        assertEquals(AesUtils.decrypt(cipherPassword, key), users.get(1).getPassword());
        assertEquals(AesUtils.decrypt(cipherIdCardNo, key), users.get(1).getIdCardNo());

    }

    @Test
    public void testNull() {
        Assert.assertNull(strategy.encrypt(null));
        Assert.assertNull(strategy.decrypt(null));
    }

    /**
     * 使用Aes算法的策略
     */
    public class AesStrategy implements TransferStrategy {


        @Override
        public Map<String, String> batchEncrypt(Set<String> plainTextSet) {
            if (CollectionUtils.isEmpty(plainTextSet)) {
                return Collections.emptyMap();
            }
            return plainTextSet.stream()
                    .collect(
                            HashMap::new,
                            (m, v) -> m.put(v, AesUtils.encrypt(v, key)),
                            HashMap::putAll);
        }

        @Override
        public Map<String, String> batchDecrypt(Set<String> cipherTextSet) {
            if (CollectionUtils.isEmpty(cipherTextSet)) {
                return Collections.emptyMap();
            }
            return cipherTextSet.stream().collect(
                    HashMap::new,
                    (m, v) -> m.put(v, AesUtils.decrypt(v, key)),
                    HashMap::putAll
            );
        }

        @Override
        public Consumer<Map<String, String>> getTextSaver() {
            return (t1) -> System.out.printf("It needs to save %s!%n", t1);
        }
    }

    @Encrypt
    static class User {

        private String username;

        @Encrypt
        private String email;

        @Encrypt
        private String password;

        @Encrypt
        private String idCardNo;

        public User(String username, String email, String password, String idCardNo) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.idCardNo = idCardNo;
        }

        /**
         * Gets the value of username
         *
         * @return the value of username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Sets the username
         * <p>You can use getUsername() to get the value of username</p>
         *
         * @param username username
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Gets the value of email
         *
         * @return the value of email
         */
        public String getEmail() {
            return email;
        }

        /**
         * Sets the email
         * <p>You can use getEmail() to get the value of email</p>
         *
         * @param email email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * Gets the value of password
         *
         * @return the value of password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Sets the password
         * <p>You can use getPassword() to get the value of password</p>
         *
         * @param password password
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * Gets the value of idCardNo
         *
         * @return the value of idCardNo
         */
        public String getIdCardNo() {
            return idCardNo;
        }

        /**
         * Sets the idCardNo
         * <p>You can use getIdCardNo() to get the value of idCardNo</p>
         *
         * @param idCardNo idCardNo
         */
        public void setIdCardNo(String idCardNo) {
            this.idCardNo = idCardNo;
        }
    }
}