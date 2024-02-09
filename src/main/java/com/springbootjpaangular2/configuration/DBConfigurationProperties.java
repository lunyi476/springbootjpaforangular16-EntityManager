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
/**
 *  (1) multiple properties:
		value = {"classpath:example1.properties", "classpath:example2.properties"}
		or 
		@PropertySource("classpath:example1.properties")
		@PropertySource("classpath:example2.properties")

    (2) change default property to looad:
		 If we want to change which file Spring Boot reads by default then we can use the spring.config.name property.
		 export SPRING_CONFIG_NAME=foo
		 Now when we run the spring boot application, it will load all the properties from foo.properties file.
		 
    (3)  Bind Fields to Property Values with @ConfigurationProperties:
		 prefix are valid to bind to this object in property
		 @ConfigurationProperties(prefix="spring.datasource", ignoreUnknownFields = false)
		 
	(4)  Include Additional Configuration Files
		 we can have the following import statement in application.properties file.
		 spring.config.import=classpath:datasource.properties,
		  classpath:mysql-properties.yml,
		  optional:file:./cloud-deployment.properties,
		  classpath:test-properties/
	
		  
     (5) Initial db data:   
		   spring.sql.init.platform=
		   Platform to use in the default schema or data script locations, schema-${platform}.sql and data-${platform}.sql.
		 Locations of the data (DML) scripts to apply to the database:  defualt is in classpath such as: src/main/resources/db
		 spring.sql.init.data-locations=classpath: db/
		 spring.sql.init.schema-locations=


**/
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
