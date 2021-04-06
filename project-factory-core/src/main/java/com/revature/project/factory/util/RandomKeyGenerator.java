package com.revature.project.factory.util;

import java.util.Random;

public final class RandomKeyGenerator {

  private RandomKeyGenerator() {}

  private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
  private static final String NUM = "0123456789";
  private static final String SPL_CHARS = "!@#$%^&*_=+-/";

  private static Random random = new Random();

  private static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits,
      int noOfSplChars) {
    if (minLen > maxLen) {
      throw new IllegalArgumentException("Min. Length > Max. Length!");
    }
    if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen) {
      throw new IllegalArgumentException(
          "Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
    }
    int value = random.nextInt(maxLen - minLen + 1);
    int len = value + minLen;
    char[] pswd = new char[len];
    int index = 0;
    for (int i = 0; i < noOfCAPSAlpha; i++) {
      index = getNextIndex(random, len);
      pswd[index] = ALPHA_CAPS.charAt(random.nextInt(ALPHA_CAPS.length()));
    }
    for (int i = 0; i < noOfDigits; i++) {
      index = getNextIndex(random, len);
      pswd[index] = NUM.charAt(random.nextInt(NUM.length()));
    }
    for (int i = 0; i < noOfSplChars; i++) {
      index = getNextIndex(random, len);
      pswd[index] = SPL_CHARS.charAt(random.nextInt(SPL_CHARS.length()));
    }
    for (int i = 0; i < len; i++) {
      if (pswd[i] == 0) {
        pswd[i] = ALPHA.charAt(random.nextInt(ALPHA.length()));
      }
    }
    return pswd;
  }

  private static int getNextIndex(Random rnd, int len) {
    return rnd.nextInt(len);
  }

  public static String getRandomPassword() {
    int len = 12;
    char[] pwd = new char[len];
    int cq = 'A';
    int rand = 0;
    for (int i = 0; i < len; i++) {
      rand = random.nextInt(3);
      switch (rand) {
        case 0:
          cq = '0' + random.nextInt(10);
          break;
        case 1:
          cq = 'a' + random.nextInt(26);
          break;
        case 2:
          cq = 'A' + random.nextInt(26);
          break;
        default:
          break;
      }
      pwd[i] = (char) cq;
    }
    return new String(pwd);
  }

  public static String getRandomVerificationCode() {
    int noOfCAPSAlpha = 2;
    int noOfDigits = 2;
    int noOfSplChars = 0;
    int minLen = 8;
    int maxLen = 12;
    String result = null;
    char[] pswd = generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars);
    result = new String(pswd);
    return result;
  }

  public static String getRandomNumber() {
    int noOfCAPSAlpha = 0;
    int noOfDigits = 3;
    int noOfSplChars = 0;
    int minLen = 3;
    int maxLen = 3;
    String result = null;
    char[] pswd = generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars);
    result = new String(pswd);
    return result;
  }

  public static String generateReferralCode() {
    String randomChars = "ABCDEFGHJKLMNOPQRSTUVWXYZ0123456789";
    int length = 6;
    StringBuilder token = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomChar = random.nextInt(randomChars.length());
      token.append(randomChars.charAt(randomChar));
    }

    return token.toString();
  }

}
