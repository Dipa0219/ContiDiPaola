package it.polimi.SE2.CK.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 *
 */
public class EncryptorTripleDES {

    private final String transformation = "DESede/CBC/PKCS5Padding";
    private final String keyString = "ContiDiPaola";
    private final String algorithm = "DESede";

    /**
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String encrypt(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(keyString.getBytes("UTF-8"));

        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] plainTextBytes = input.getBytes("UTF-8");
        byte[] cipherText = cipher.doFinal(plainTextBytes);

        StringBuilder outputString = new StringBuilder();
        for (byte a : cipherText) {
            outputString.append(a).append(" ");
        }

        return outputString.toString();
    }

    /**
     *
     * @param ciphertext
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String decrypt(String ciphertext) throws InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {

        byte[] cipherTextByteArray = stringToByteArray(ciphertext);

        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(keyString.getBytes("UTF-8"));

        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);

        Cipher decryptor = Cipher.getInstance(transformation);
        decryptor.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] decrypted = decryptor.doFinal(cipherTextByteArray);

        return new String(decrypted, "UTF-8");
    }

    /**
     *
     * @param string
     * @return
     */
    private byte[] stringToByteArray(String string) {
        String[] stringFragments = string.split(" ");

        byte[] byteArray = new byte[stringFragments.length];
        for (int i = 0; i < stringFragments.length; i++) {
            byteArray[i] = Byte.parseByte(stringFragments[i]);
        }
        return byteArray;
    }
}
