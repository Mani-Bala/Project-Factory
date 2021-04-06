package com.revature.project.factory.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncryption {
  private StringEncryption() {}

  /**
   * This method used to get md5 algorthim.
   * 
   * @param input input
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   */
  public static String getEngryptedString(String input) throws NoSuchAlgorithmException {
    byte[] bytesOfMsg = (input).getBytes(StandardCharsets.UTF_8);
    MessageDigest md = null;
    md = MessageDigest.getInstance("MD5");
    byte[] theDigest = md.digest(bytesOfMsg);
    StringBuilder sb = new StringBuilder(2 * theDigest.length);
    for (byte b : theDigest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }
}
