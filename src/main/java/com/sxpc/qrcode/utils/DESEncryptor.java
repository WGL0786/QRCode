package com.sxpc.qrcode.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 该类为 DES 加解密工具类
 */
@Component
public class DESEncryptor {
    private static final String KEY = "12345678";   // 密钥，长度为8字节
    private static final String IV = "12345678";    // 初始化向量，长度为8字节
    private static final String CHARSET = "UTF-8";  // 字符集
    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";     // 加解密算法和工作模式
    private static final String ALGORITHM = "DES";  // 加解密算法

    /**
     * 加密明文并返回密文
     *
     * @param plaintext 明文字符串
     * @return 密文字符串
     * @throws Exception 加密过程中可能抛出异常，由调用方处理
     */
    public String encrypt(String plaintext) throws Exception {
        byte[] keyBytes = KEY.getBytes(CHARSET);
        byte[] ivBytes = IV.getBytes(CHARSET);
        byte[] plaintextBytes = plaintext.getBytes(CHARSET);

        // 生成密钥
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(desKeySpec.getKey(), ALGORITHM);

        // 加密
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // Base64 编码
        String ciphertext = Base64.encodeBase64String(encryptedBytes);

        return ciphertext;
    }

    /**
     * 解密密文并返回明文
     *
     * @param ciphertext 密文字符串
     * @return 明文字符串
     * @throws Exception 解密过程中可能抛出异常，由调用方处理
     */
    public String decrypt(String ciphertext) throws Exception {
        byte[] keyBytes = KEY.getBytes(CHARSET);
        byte[] ivBytes = IV.getBytes(CHARSET);
        byte[] ciphertextBytes = Base64.decodeBase64(ciphertext);

        // 生成密钥
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(desKeySpec.getKey(), ALGORITHM);

        // 解密
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

        // 解密密文并去除 padding
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);
        String plaintext = new String(decryptedBytes, CHARSET);

        return plaintext;
    }
}