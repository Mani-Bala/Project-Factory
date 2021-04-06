package com.revature.project.factory.util;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:version.properties")
public class VersionPropertiesFileUtils implements BeanFactoryPostProcessor {
  private Environment versionProperties;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    versionProperties = beanFactory.getBean(Environment.class);
  }

  public String getValue(String propertyName) {
    return versionProperties.getProperty(propertyName);
  }
}
