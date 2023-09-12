package com.springbootjpaangular2.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/** 
 * @author lyi
 * 08/2020
 */
@Configuration  
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
//property in *.properties file: spring.datasource.url, prefix are valid to bind to this object
//@ConfigurationProperties(prefix="spring.datasource", ignoreUnknownFields = false)
public class DBConfigurationProperties {
  
 	/**
 	 * without provide self Bean Factory, environment will provide default : 
 	 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.
 	 * 
 	 */
	
	// Bean is a singleton as default
	@Bean  
    public EntityManagerFactory entityManagerFactoryBean () {  
    	final EntityManagerFactory entityManagerFactory;
		try {
			entityManagerFactory = Persistence.
					createEntityManagerFactory("SpringBootWebOracle"); 
			
		} catch (Throwable ex) {
			System.err.println("Initial EntityManagerFactory creation failed."+ ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		return entityManagerFactory;
	}
	
	
	  @Bean 
	  public PlatformTransactionManager
	  transactionManager(EntityManagerFactory emf) { 
		  JpaTransactionManager
		  transactionManager = new JpaTransactionManager();
		  transactionManager.setEntityManagerFactory(emf);
	  
		  return transactionManager; 
	  }
	  
	  @Bean 
	  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() { 
		  return new PersistenceExceptionTranslationPostProcessor(); 
	  }
}
