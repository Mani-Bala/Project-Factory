package com.revature.project.factory.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.revature.project.factory.util.EmailValidationUtil;
import com.revature.project.factory.util.FileDetails;

public class EmailWrapper {

  private String fromAddress;
  private String fromName;
  private String toName;
  private String ccName;
  private List<String> toAddress;
  private List<String> ccAddress;
  private List<String> bccAddress;
  private String subject;
  private String content;
  private String templateName;
  private Map<String, Object> templateVars;
  private List<FileDetails> attachments;
  private Long referralId;
  private String phone;
  private Map<String, String> ccEmailNameMap;
  private String notificationType;

  public EmailWrapper() {
    /*
     * This is a default constructor. I need to declare explicitly an empty public constructor with
     * no argument because I also need the default one, and if I don't declare it , it won't be
     * available.
     */
  }

  /**
   * Parameterized constructor with 6 arguments.
   * 
   * @author Avinash
   * 
   * @param fromName
   * @param toAddress
   * @param subject
   * @param bccAddress
   * @param templateName
   * @param templateVars
   */
  public EmailWrapper(List<String> toAddress, List<String> bccAddress, String fromName,
      String templateName, String subject, Map<String, Object> templateVars) {
    this.toAddress = toAddress;
    this.bccAddress = bccAddress;
    this.fromName = fromName;
    this.templateName = templateName;
    this.subject = subject;
    this.templateVars = templateVars;
  }

  public EmailWrapper(List<String> toAddress, List<String> bccAddress, String fromName,
      String templateName, String subject, Map<String, Object> templateVars,
      String notificationType) {
    this.toAddress = toAddress;
    this.bccAddress = bccAddress;
    this.fromName = fromName;
    this.templateName = templateName;
    this.subject = subject;
    this.templateVars = templateVars;
    this.notificationType = notificationType;
  }

  /**
   * Parameterized constructor with 6 arguments.
   * 
   * @param fromName
   * @param toAddress
   * @param toName
   * @param subject
   * @param templateName
   * @param templateVars
   */
  public EmailWrapper(String fromName, List<String> toAddress, String toName, String subject,
      String templateName, Map<String, Object> templateVars) {
    this.fromName = fromName;
    this.toAddress = toAddress;
    this.toName = toName;
    this.subject = subject;
    this.templateName = templateName;
    this.templateVars = templateVars;
  }

  /**
   * Parameterized constructor with 6 arguments.
   * 
   * @param fromName
   * @param toAddress
   * @param bccAddress
   * @param subject
   * @param templateName
   * @param templateVars
   */
  public EmailWrapper(String fromName, List<String> toAddress, List<String> bccAddress,
      String subject, String templateName, Map<String, Object> templateVars) {
    this.fromName = fromName;
    this.toAddress = toAddress;
    this.bccAddress = bccAddress;
    this.subject = subject;
    this.templateName = templateName;
    this.templateVars = templateVars;
  }

  /**
   * Parameterized constructor with 7 arguments.
   * 
   * @param fromName
   * @param toName
   * @param toAddress
   * @param bccAddress
   * @param subject
   * @param templateName
   * @param templateVars
   */
  public EmailWrapper(String fromName, String toName, List<String> toAddress,
      List<String> bccAddress, String subject, String templateName,
      Map<String, Object> templateVars) {
    this.fromName = fromName;
    this.toName = toName;
    this.toAddress = toAddress;
    this.bccAddress = bccAddress;
    this.subject = subject;
    this.templateName = templateName;
    this.templateVars = templateVars;
  }

  /**
   * Convenience constructor.
   * 
   * @param fromName Display name for the sender.
   * @param toAddress The target e-mail address.
   * @param toName The name associated with the toAddress.
   * @param subject The subject of the email.
   * @param templateName The name of the email template to use.
   * @param templateVars Properties to insert into the template.
   */
  public EmailWrapper(String fromName, String toAddress, String toName, String subject,
      String templateName, Map<String, Object> templateVars) {
    this.fromName = fromName;
    addIntoToAddress(toAddress);
    this.toName = toName;
    this.subject = subject;
    this.templateName = templateName;
    this.templateVars = templateVars;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFromAddress() {
    return fromAddress;
  }

  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  public String getFromName() {
    return fromName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public List<String> getToAddress() {
    return toAddress;
  }

  public void setToAddress(List<String> toAddress) {
    this.toAddress = toAddress;
  }

  public List<String> getCcAddress() {
    return ccAddress;
  }

  public void setCcAddress(List<String> ccAddress) {
    this.ccAddress = ccAddress;
  }

  public List<String> getBccAddress() {
    return bccAddress;
  }

  public void setBccAddress(List<String> bccAddress) {
    this.bccAddress = bccAddress;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public Map<String, Object> getTemplateVars() {
    return templateVars;
  }

  public void setTemplateVars(Map<String, Object> templateVars) {
    this.templateVars = templateVars;
  }

  public Long getReferralId() {
    return referralId;
  }

  public void setReferralId(Long referralId) {
    this.referralId = referralId;
  }

  public String getToName() {
    return toName;
  }

  public void setToName(String toName) {
    this.toName = toName;
  }

  public String getCcName() {
    return ccName;
  }

  public void setCcName(String ccName) {
    this.ccName = ccName;
  }

  public Map<String, String> getCcEmailNameMap() {
    return ccEmailNameMap;
  }

  public void setCcEmailNameMap(Map<String, String> ccEmailNameMap) {
    this.ccEmailNameMap = ccEmailNameMap;
  }

  public boolean addIntoToAddress(String email) {

    if (toAddress == null) {
      toAddress = new ArrayList<>();
    }

    if (!EmailValidationUtil.isValidEmail(email)) {
      return false;
    }

    return toAddress.add(email);
  }

  public boolean addIntoToCcAddress(String email) {

    if (ccAddress == null) {
      ccAddress = new ArrayList<>();
    }

    if (!EmailValidationUtil.isValidEmail(email)) {
      return false;
    }
    return ccAddress.add(email);
  }

  public boolean addIntoBccAddress(String email) {

    if (bccAddress == null) {
      bccAddress = new ArrayList<>();
    }

    if (!EmailValidationUtil.isValidEmail(email)) {
      return false;
    }
    return bccAddress.add(email);
  }

  // Remove from list
  public boolean removeFromToAddress(String item) {
    return removeFromList(toAddress, item);
  }

  public boolean removeFromCcAddress(String item) {
    return removeFromList(ccAddress, item);
  }

  public boolean removeFromBccAddress(String item) {
    return removeFromList(bccAddress, item);
  }

  // Remove all from list
  public void removeAllFromToAddress() {
    toAddress = removeAllFromList();
  }

  public void removeAllFromCcAddress() {
    ccAddress = removeAllFromList();
  }

  public void removeAllFromBccAddress() {
    bccAddress = removeAllFromList();
  }

  // ------------------------------------------------------------ Local often use
  // methods

  private static boolean removeFromList(List<String> list, String email) {
    if (list == null) {
      return false;
    }

    return list.remove(email);
  }

  private static List<String> removeAllFromList() {
    return new ArrayList<>();
  }

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public List<FileDetails> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<FileDetails> attachments) {
    this.attachments = attachments;
  }

}
