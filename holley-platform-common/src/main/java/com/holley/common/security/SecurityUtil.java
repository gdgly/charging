package com.holley.common.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.holley.common.util.StringUtil;

/**
 * 安全的工具
 */
public class SecurityUtil {

    public static final int    AES_BIT            = 256;                   // 支持128位，192位，256位
    public static final String ALGORITHM          = "AES/ECB/PKCS7Padding"; // 算法名称
    public static boolean      initialized        = false;                 // 初始化标志
    static final int           PASSWORD_SALT_SIZE = 16;

    /**
     * 对密码进行非对称加密,用户密码生成
     * 
     * @param passwd 输入的口令
     * @return 返回密文
     */
    public static String passwordEncrypt(String passwd) {
        try {
            byte[] bsPasswd = passwd.getBytes("GBK");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bsMD5 = md5.digest(bsPasswd);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] bsSHa = sha.digest(bsPasswd);
            Key key = loadKey(bsMD5);
            return StringUtil.encodeBase64(encrypt(bsSHa, key));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从二进制数组调入密钥
     * 
     * @param rawkey 存密钥的数组
     * @return 密钥
     * @exception Exception Description of Exception
     */
    private static Key loadKey(byte[] rawkey) throws Exception {
        byte[] desKey = new byte[24];
        int keylen = rawkey.length > 24 ? 24 : rawkey.length;
        System.arraycopy(rawkey, 0, desKey, 0, keylen);
        DESedeKeySpec dks = new DESedeKeySpec(desKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        Key key = keyFactory.generateSecret(dks);
        return key;
    }

    /**
     * 用密钥对信息加密 DES
     * 
     * @param msg 要加密的信息
     * @param key 密钥
     * @return 密文
     * @exception Exception Description of Exception
     */
    private static byte[] encrypt(byte[] msg, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(msg);
    }

    /**
     * 对密文解密
     * 
     * @param msg 要解密的密文
     * @param key 密钥
     * @return 明文
     * @exception Exception Description of Exception
     */
    private static byte[] decrypt(byte[] msg, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(msg);
    }

    /**
     * 根据secretKey解密,DES
     * 
     * @param secretString 密文
     * @param secretKey 密钥
     * @return
     */
    public static String decrypt(String secretString, String secretKey) {
        if (StringUtils.isEmpty(secretString) || StringUtils.isEmpty(secretKey)) {
            return null;
        }
        try {
            byte[] encryptedData = Base64.decode(secretString);
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            SecureRandom sr = new SecureRandom();
            byte[] rawKeyData = (new String(secretKey)).getBytes();

            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);

            byte[] decryptedData = cipher.doFinal(encryptedData);

            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据secretKey加密
     * 
     * @param source 明文
     * @param secretKey 密钥
     * @return
     */
    public static String encrypt(String source, String secretKey) {
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(secretKey)) {
            return null;
        }
        try {
            byte[] decryptData = source.getBytes();
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            SecureRandom sr = new SecureRandom();
            byte[] rawKeyData = (new String(secretKey)).getBytes();

            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);

            byte[] encryptData = cipher.doFinal(decryptData);

            return Base64.encode(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param source 密文支持128/192/256 bits.
     * @param secretKey 秘钥
     * @return base64加密内容
     */
    public static String aesEncode(String source, String secretKey) {
        initialize();
        String result = "";
        try {
            SecretKeySpec key = getAesKey(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedText = cipher.doFinal(source.getBytes("UTF-8"));
            result = Base64.encode(encryptedText);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String aesDecode(String source, String secretKey) {
        initialize();
        String result = "";
        try {
            byte[] deSource = Base64.decode(source);
            SecretKeySpec key = getAesKey(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedText = cipher.doFinal(deSource);
            result = new String(decryptedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void initialize() {
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    private static SecretKeySpec getAesKey(String secretKey) throws UnsupportedEncodingException {
        int keyLength = AES_BIT;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = secretKey.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, ALGORITHM);
        return key;
    }

}
