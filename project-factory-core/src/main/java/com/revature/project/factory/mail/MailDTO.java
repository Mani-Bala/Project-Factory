package com.revature.project.factory.mail;

import freemarker.template.Configuration;

public class MailDTO {

  private static MailDTO instance;
  public static int firstTimeInstanciat;

  private String mailHost;
  private int mailPort;
  private String username;
  private String password;
  private Configuration freeMarkerConfiguration;
  private String mailFrom;
  private boolean smtpStartTls;
  private boolean smtpAuth;
  private boolean quitWait;
  private boolean smtpSsl;

  public MailDTO() {
    this.firstTimeInstanciat = 0;
  }

  public static MailDTO getInstanceOf() {
    if (instance == null) {
      instance = new MailDTO();
    } else if (firstTimeInstanciat < 100) {
      firstTimeInstanciat++;
    }
    return instance;
  }

  public String getMailHost() {
    return mailHost;
  }

  public void setMailHost(String mailHost) {
    this.mailHost = mailHost;
  }

  public int getMailPort() {
    return mailPort;
  }

  public void setMailPort(int mailPort) {
    this.mailPort = mailPort;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Configuration getFreeMarkerConfiguration() {
    return freeMarkerConfiguration;
  }

  public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
    this.freeMarkerConfiguration = freeMarkerConfiguration;
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public boolean isSmtpStartTls() {
    return smtpStartTls;
  }

  public void setSmtpStartTls(boolean smtpStartTls) {
    this.smtpStartTls = smtpStartTls;
  }

  public boolean isSmtpAuth() {
    return smtpAuth;
  }

  public void setSmtpAuth(boolean smtpAuth) {
    this.smtpAuth = smtpAuth;
  }

  public boolean isQuitWait() {
    return quitWait;
  }

  public void setQuitWait(boolean quitWait) {
    this.quitWait = quitWait;
  }

  public boolean isSmtpSsl() {
    return smtpSsl;
  }

  public void setSmtpSsl(boolean smtpSsl) {
    this.smtpSsl = smtpSsl;
  }

}
