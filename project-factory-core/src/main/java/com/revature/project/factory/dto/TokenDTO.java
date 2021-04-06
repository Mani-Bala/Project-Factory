package com.revature.project.factory.dto;

import java.util.Date;

import com.revature.project.factory.util.TypeConversionUtils;

public class TokenDTO {
  private String token;
  private String encryptedToken;
  private String refreshedToken;
  private String refreshedEncryptedToken;
  private Date expiresAt;
  private Long empId;
  private String userName;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getEncryptedToken() {
    return encryptedToken;
  }

  public void setEncryptedToken(String encryptedToken) {
    this.encryptedToken = encryptedToken;
  }

  public String getRefreshedToken() {
    return refreshedToken;
  }

  public void setRefreshedToken(String refreshedToken) {
    this.refreshedToken = refreshedToken;
  }

  public String getRefreshedEncryptedToken() {
    return refreshedEncryptedToken;
  }

  public void setRefreshedEncryptedToken(String refreshedEncryptedToken) {
    this.refreshedEncryptedToken = refreshedEncryptedToken;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Long getEmpId() {
    return empId;
  }

  public void setEmpId(Object empId) {
    this.empId = TypeConversionUtils.toLong(empId);
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
