package com.revature.project.factory.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;


public class PropertiesFileUtils implements BeanFactoryPostProcessor {

  private static Logger logger = LogManager.getLogger(PropertiesFileUtils.class);
  private static Environment applicationProperties;

  private PropertiesFileUtils() {}

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    applicationProperties = beanFactory.getBean(Environment.class);

  }

  public static String getValue(String propertyName) {
    return getValue("application", propertyName);
  }

  /**
   * getValue method.
   */
  public static String getValue(String fileName, String propertyName) {
    String value = null;
    if ("application".equals(fileName)) {
      value = applicationProperties.getProperty(propertyName);
    }
    if (StringUtils.isBlank(value)) {
      value = propertyName;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Return state: fileName= {} propertyName= {} value= {}", fileName, propertyName,
          value);
    }
    return value;
  }
}
