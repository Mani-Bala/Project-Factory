package com.revature.project.factory.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.mail.EmailWrapper;
import com.revature.project.factory.mail.Emailer;
import com.revature.project.factory.mail.MailConfiguration;

@Component
public class EmailUtils {

  private static MailConfiguration mailConfiguration;

  @Autowired
  public EmailUtils(MailConfiguration mailConfiguration) {
    this.mailConfiguration = mailConfiguration;
  }

  public EmailUtils() {
    // Do nothing because of X and Y.
  }

  public static void sendEmail(EmailWrapper emailWrapper) {
    if (isBlank(emailWrapper.getFromName())) {
      emailWrapper.setFromName(AppConstants.EMAIL_FROM_NAME);
    }
    new Thread(new Emailer(mailConfiguration, emailWrapper)).start();
  }
}
