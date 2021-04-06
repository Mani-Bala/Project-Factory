package com.revature.project.factory.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.revature.project.factory.model")
@EnableTransactionManagement
public class SessionFactoryBean {

  private Environment environment;
  private DataSource dataSource;

  @Autowired
  public SessionFactoryBean(Environment environment, DataSource dataSource) {
    this.environment = environment;
    this.dataSource = dataSource;
  }

  @Bean(name = "sessionFactory")
  public SessionFactory getSessionFactory() {
    LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
    sessionBuilder.scanPackages("com.revature.project.factory.model");
    sessionBuilder.addProperties(getHibernateProperties());
    return sessionBuilder.buildSessionFactory();
  }

  @Autowired
  @Bean(name = "transactionManager")
  public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
    return new HibernateTransactionManager(sessionFactory);
  }

  private Properties getHibernateProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.dialect",
        environment.getProperty("spring.jpa.properties.hibernate.dialect"));
    properties.put("hibernate.default_schema",
        environment.getProperty("spring.jpa.properties.hibernate.default_schema"));
    properties.put("hibernate.show_sql",
        environment.getProperty("spring.jpa.properties.hibernate.show_sql"));
    properties.put("hibernate.format_sql",
        environment.getProperty("spring.jpa.properties.hibernate.format_sql"));
    properties.put("hibernate.use_sql_comments",
        environment.getProperty("spring.jpa.properties.hibernate.use_sql_comments"));
    properties.put("hibernate.generate_statistics",
        environment.getProperty("spring.jpa.properties.hibernate.generate_statistics"));
    properties.put("hibernate.connection.autocommit",
        environment.getProperty("spring.jpa.properties.hibernate.autocommit"));
    return properties;
  }
}
