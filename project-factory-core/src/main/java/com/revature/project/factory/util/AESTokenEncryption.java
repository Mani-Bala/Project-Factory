package com.revature.project.factory.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.revature.project.factory.service.exception.EncryptionFailsException;

/**
 * Helper methods for encryption and decryption of data using AES algorithm
 * 
 * @author Revature
 *
 */
public class AESTokenEncryption {

  private AESTokenEncryption() {}

  private static final String SECRET_KEY =
      PropertiesFileUtils.getValue("security.token.encryption");
  private static final String ALGORITHM = "AES/ECB/PKCS5PADDING";
  private static SecretKeySpec secretKey;

  private static void setKey() throws NoSuchAlgorithmException {
    MessageDigest sha = null;
    String myKey = SECRET_KEY;
    byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
    sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    secretKey = new SecretKeySpec(key, "AES");
  }

  public static String encrypt(String strToEncrypt) throws EncryptionFailsException {
    try {
      setKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new EncryptionFailsException(e.getMessage(), e);
    }
  }

  public static String decrypt(String strToDecrypt) throws EncryptionFailsException {
    try {
      setKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      throw new EncryptionFailsException(e.getMessage(), e);
    }
  }
}
