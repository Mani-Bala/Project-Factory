package com.revature.project.factory.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordEncryptionUtils {
  private static PasswordEncryptionUtils passwordEncryptionInstance;

  private static final Logger LOGGER = LogManager.getLogger(PasswordEncryptionUtils.class);

  /**
   * getInstance.
   *
   */
  public static PasswordEncryptionUtils getInstance() {
    if (passwordEncryptionInstance == null) {
      passwordEncryptionInstance = new PasswordEncryptionUtils();
    }
    return passwordEncryptionInstance;
  }

  /**
   * authenticate.
   *
   */
  public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) {
    boolean result = false;
    /*
     * Encrypt the clear-text password using the same salt that was used to encrypt the original
     * password
     */
    byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
    /*
     * Authentication succeeds if encrypted password that the user entered is equal to the stored
     * hash
     */
    result = Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    return result;
  }

  /**
   * getEncryptedPassword.
   *
   */
  public byte[] getEncryptedPassword(String password, byte[] salt) {
    byte[] result = null;
    try {
      /*
       * PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST specifically names SHA-1 as
       * an acceptable hashing algorithm for PBKDF2
       */
      String algorithm = "PBKDF2WithHmacSHA1";
      /*
       * SHA-1 generates 160 bit hashes, so that's what makes sense here
       */
      int derivedKeyLength = 160;
      /*
       * Pick an iteration count that works for you. The NIST recommends atleast 1,000 iterations:
       */
      int iterations = 20000;
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
      SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
      result = skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      LOGGER.error(noSuchAlgorithmException.getMessage());
    } catch (InvalidKeySpecException invalidKeySpecException) {
      LOGGER.error(invalidKeySpecException.getMessage());
    }
    return result;
  }

  /**
   * generateSalt.
   *
   */
  public byte[] generateSalt() {
    byte[] result = null;
    try {
      /* VERY important to use SecureRandom instead of just Random */
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      /* Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5 */
      result = new byte[8];
      random.nextBytes(result);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      LOGGER.error(noSuchAlgorithmException.getMessage());
    }
    return result;
  }

}
