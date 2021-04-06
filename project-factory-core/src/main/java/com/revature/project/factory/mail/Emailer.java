package com.revature.project.factory.mail;

import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

/**
 * This utility is used for mail operations.
 *
 * @author Selva Kumaran
 *
 */
@Component
public class Emailer implements Runnable {

  private MailDTO maildto;

  private EmailWrapper email;

  private Logger logger = LogManager.getLogger(Emailer.class);

  public Emailer() {
    super();
  }

  public Emailer(MailConfiguration maildto, EmailWrapper email) {
    this.maildto = maildto.getMaildto();
    this.email = email;
  }

  @Override
  public void run() {
    sendEmail();

  }

  public void sendEmail() {
    try {
      JavaMailSenderImpl sender = new JavaMailSenderImpl();
      if (maildto.getMailHost() != null) {
        sender.setHost(maildto.getMailHost());
      }
      if (maildto.getMailPort() != 0) {
        sender.setPort(maildto.getMailPort());
      }
      if (maildto.getUsername() != null) {
        sender.setUsername(maildto.getUsername());
      }
      if (maildto.getPassword() != null) {
        sender.setPassword(maildto.getPassword());
      }
      if (maildto.isSmtpSsl()) {
        sender.setProtocol("smtps");
      }
      Properties javaMailProperties = null;
      if (maildto.isSmtpStartTls()) {
        if (javaMailProperties == null) {
          javaMailProperties = new Properties();
        }
        javaMailProperties.put("mail.smtp.starttls.enable", true);
      }
      if (maildto.isSmtpAuth()) {
        if (javaMailProperties == null) {
          javaMailProperties = new Properties();
        }
        javaMailProperties.put("mail.smtp.auth", true);
      }
      if (maildto.isQuitWait()) {
        if (javaMailProperties == null) {
          javaMailProperties = new Properties();
        }
        javaMailProperties.put("mail.smtp.quitwait", true);
      } else {
        if (javaMailProperties == null) {
          javaMailProperties = new Properties();
        }
        javaMailProperties.put("mail.smtp.quitwait", false);
      }
      if (javaMailProperties != null) {
        sender.setJavaMailProperties(javaMailProperties);
      }
      MimeMessage msg = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

      if (StringUtils.isBlank(email.getContent())) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        configuration.setClassForTemplateLoading(this.getClass(), "/template/");
        Template template = configuration.getTemplate(email.getTemplateName());
        StringWriter out = new StringWriter();
        template.process(email.getTemplateVars(), out);
        helper.setText(out.toString(), true);
      } else {
        helper.setText(email.getContent(), true);
      }
      String mailFrom;
      if (maildto.getMailFrom() != null) {
        mailFrom = maildto.getMailFrom();
      } else {
        mailFrom = maildto.getUsername();
      }
      if (email.getFromAddress() != null) {
        helper.setFrom(email.getFromAddress());
      }

      if (email.getFromName() != null) {
        helper.setFrom(mailFrom, email.getFromName());
      } else {
        helper.setFrom(mailFrom);
      }

      if (CollectionUtils.isNotEmpty(email.getToAddress())) {
        String[] toAddress = new String[email.getToAddress().size()];
        int iterator = 0;
        for (String addr : email.getToAddress()) {
          toAddress[iterator] = addr;
          iterator++;
        }
        helper.setTo(toAddress);
        if (email.getToName() != null && toAddress.length == 1) {
          String to = toAddress[0];
          helper.setTo(new InternetAddress(to, email.getToName()));
        } else {
          helper.setTo(toAddress);
        }
      }
      if (CollectionUtils.isNotEmpty(email.getCcAddress())) {
        String[] ccAddress = new String[email.getCcAddress().size()];
        int iterator = 0;
        for (String addr : email.getCcAddress()) {
          ccAddress[iterator] = addr;
          iterator++;
        }
        helper.setCc(ccAddress);
        if (email.getCcName() != null && ccAddress.length == 1) {
          String cc = ccAddress[0];
          helper.setCc(new InternetAddress(cc, email.getCcName()));
        } else {
          helper.setCc(ccAddress);

        }
      }

      if (email.getCcEmailNameMap() != null) {
        InternetAddress[] ccAddress = new InternetAddress[email.getCcEmailNameMap().size()];
        int iterator = 0;
        for (Map.Entry<String, String> entry : email.getCcEmailNameMap().entrySet()) {
          ccAddress[iterator] = new InternetAddress(entry.getKey(), entry.getValue());
          iterator++;
        }
        helper.setCc(ccAddress);
      }

      if (CollectionUtils.isNotEmpty(email.getBccAddress())) {
        String[] bccAddress = new String[email.getBccAddress().size()];
        int iterator = 0;
        for (String addr : email.getBccAddress()) {
          bccAddress[iterator] = addr;
          iterator++;
        }
        helper.setBcc(bccAddress);
      }
      helper.setSubject(email.getSubject());
      if (Objects.nonNull(email)) {
        if (CollectionUtils.isNotEmpty(email.getToAddress())) {
          logger.info("Mails sent TO: " + email.getToAddress().toString());
        }
        if (CollectionUtils.isNotEmpty(email.getCcAddress())) {
          logger.info("Mails sent CC: " + email.getCcAddress().toString());
        }
        if (CollectionUtils.isNotEmpty(email.getBccAddress())) {
          logger.info("Mails sent BCC: " + email.getBccAddress().toString());
        }
      }
      sender.send(msg);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
}
