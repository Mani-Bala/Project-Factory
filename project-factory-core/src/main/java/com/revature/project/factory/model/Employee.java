package com.revature.project.factory.model;

import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.project.factory.util.JsonLocalDateTimeDeserializer;
import com.revature.project.factory.util.JsonLocalDateTimeSerializer;
import com.revature.project.factory.util.TypeConversionUtils;

@Entity
@Table(name = "employees")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Employee implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "EMAIL_ID")
  private String emailId;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive = true;

  @JsonIgnore
  @Column(name = "PASSWORD_ENCRYPT")
  private byte[] passwordEncrypt;

  @JsonIgnore
  @Column(name = "PASSWORD_SALT")
  private byte[] passwordSalt;

  @JsonIgnore
  @Column(name = "CREATED_BY")
  private Long createdBy;

  @Column(name = "CREATED_ON")
  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  private LocalDateTime createdOn;

  @JsonIgnore
  @Column(name = "UPDATED_BY")
  private Long updatedBy;

  @Column(name = "UPDATED_ON")
  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  private LocalDateTime updatedOn;

  @Column(name = "PASSWORD_RESET_REQUEST")
  private Boolean passwordResetRequest = false;

  @Transient
  private String password;

  @Transient
  private String encryptedLoginToken;

  @Transient
  private Date tokenExpTime;

  @Transient
  private String loginToken;

  @Transient
  private String displayFullName;

  /**
   * Default no-arg constructor.
   */
  public Employee() {
    super();
  }

  /**
   * Construct employee with specified id.
   */
  public Employee(Long id) {
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
    this.id = toLong(id);
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

  public void setCreatedBy(Object createdBy) {
    this.createdBy = toLong(createdBy);
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

  public void setUpdatedBy(Object modifiedBy) {
    this.updatedBy = toLong(modifiedBy);
  }

  public LocalDateTime getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Object modifiedOn) {
    this.updatedOn = TypeConversionUtils.toLocalDateTime(modifiedOn);
  }

  public byte[] getPasswordEncrypt() {
    return passwordEncrypt;
  }

  public void setPasswordEncrypt(byte[] passwordEncrypt) {
    this.passwordEncrypt = passwordEncrypt;
  }

  public byte[] getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(byte[] passwordSalt) {
    this.passwordSalt = passwordSalt;
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

  public Boolean getPasswordResetRequest() {
    return passwordResetRequest;
  }

  public void setPasswordResetRequest(Boolean passwordResetRequest) {
    this.passwordResetRequest = passwordResetRequest;
  }

  public String getDisplayFullName() {
    return displayFullName;
  }

  public void setDisplayFullName(String displayFullName) {
    this.displayFullName = displayFullName;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Employee && hashCode() == obj.hashCode();
  }
}
