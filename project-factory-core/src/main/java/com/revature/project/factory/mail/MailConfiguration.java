package com.revature.project.factory.mail;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.util.PropertiesFileUtils;

import freemarker.template.Configuration;

/**
 * This Singleton Class for the Dynamic Configuration
 * 
 * @author MUTHU G.K
 *
 */

@SuppressWarnings("serial")
@Component
public class MailConfiguration implements Serializable {

  private static final long serialVersionUID = 1905122041950251207L;

  private static final Logger LOGGER = LogManager.getLogger(MailConfiguration.class);

  private transient Configuration freeMarkerConfiguration;

  private MailDTO maildto;

  public Configuration getFreeMarkerConfiguration() {
    return freeMarkerConfiguration;
  }

  public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
    this.freeMarkerConfiguration = freeMarkerConfiguration;
  }



  @SuppressWarnings("static-access")
  public MailDTO getMaildto() {
    maildto = MailDTO.getInstanceOf();
    if (maildto.firstTimeInstanciat < 100) {
      loadValueOnce(maildto);
    }
    return maildto;
  }

  public void setMaildto(MailDTO maildto) {
    this.maildto = maildto;
  }

  @SuppressWarnings("static-access")
  public void loadValueOnce(MailDTO mailcfg) {

    if (mailcfg.firstTimeInstanciat < 100) {
      mailcfg.firstTimeInstanciat = 100;
      try {
        if (getFreeMarkerConfiguration() != null) {
          mailcfg.setFreeMarkerConfiguration(getFreeMarkerConfiguration());
        }
        mailcfg.setMailHost(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_HOST));
        mailcfg.setMailPort(
            Integer.parseInt(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_PORT)));
        mailcfg.setMailFrom(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_FROM));
        mailcfg.setUsername(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_USERNAME));
        mailcfg.setPassword(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_PASWORD));
        mailcfg.setSmtpStartTls(Boolean
            .valueOf(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_SMTP_STARTTLS)));
        mailcfg.setSmtpAuth(
            Boolean.valueOf(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_SMTP_AUTH)));
        mailcfg.setQuitWait(Boolean
            .valueOf(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_SMTP_QUIT_WAIT)));
        mailcfg.setSmtpSsl(Boolean
            .valueOf(PropertiesFileUtils.getValue(AppConstants.APP_CONFIG_MAIL_MAIL_SMTP_SSL)));
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
  }
}
