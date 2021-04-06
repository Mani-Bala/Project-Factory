package com.revature.project.factory.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.revature.project.factory.util.TypeConversionUtils;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SystemUserDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  private String emailId;
  private String firstName;
  private String lastName;
  private Boolean isActive = true;
  private Long createdBy;
  private LocalDateTime createdOn;
  private Long updatedBy;
  private LocalDateTime updatedOn;
  private String password;
  private String encryptedLoginToken;
  private Date tokenExpTime;
  private String loginToken;
  private String displayFullName;
  private Boolean passwordResetRequest;

  /**
   * Default no-arg constructor.
   */
  public SystemUserDTO() {
    super();
  }

  /**
   * Construct employee with specified id.
   */
  public SystemUserDTO(Long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return id != null ? id.intValue() : -1;
  }

  public Long getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = TypeConversionUtils.toLong(id);
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean active) {
    isActive = active;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Object createdOn) {
    this.createdOn = TypeConversionUtils.toLocalDateTime(createdOn);
  }

  public Long getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Long modifiedBy) {
    this.updatedBy = modifiedBy;
  }

  public LocalDateTime getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Object modifiedOn) {
    this.updatedOn = TypeConversionUtils.toLocalDateTime(modifiedOn);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEncryptedLoginToken() {
    return encryptedLoginToken;
  }

  public void setEncryptedLoginToken(String encryptedLoginToken) {
    this.encryptedLoginToken = encryptedLoginToken;
  }

  public Date getTokenExpTime() {
    return tokenExpTime;
  }

  public void setTokenExpTime(Date tokenExpTime) {
    this.tokenExpTime = tokenExpTime;
  }

  public String getLoginToken() {
    return loginToken;
  }

  public void setLoginToken(String loginToken) {
    this.loginToken = loginToken;
  }

  public String getDisplayFullName() {
    return displayFullName;
  }

  public void setDisplayFullName(String displayFullName) {
    this.displayFullName = displayFullName;
  }

  public Boolean getPasswordResetRequest() {
    return passwordResetRequest;
  }

  public void setPasswordResetRequest(Boolean passwordResetRequest) {
    this.passwordResetRequest = passwordResetRequest;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SystemUserDTO && hashCode() == obj.hashCode();
  }
}
