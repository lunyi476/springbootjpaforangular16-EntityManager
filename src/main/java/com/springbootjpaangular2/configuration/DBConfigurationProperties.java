package com.springbootjpaangular2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.h2.jdbc.JdbcStatement;
import org.h2.jdbc.JdbcConnection;

import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.sql.DataSource;

/**
 * @author lyi 08/2020
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")  // default
/**
 * (1) multiple properties: value = {"classpath:example1.properties",
 * "classpath:example2.properties"}
 * or @PropertySource("classpath:example1.properties") @PropertySource("classpath:example2.properties")
 * 
 * (2) change default property to load: If we want to change which file Spring
 * Boot reads by default then we can use the spring.config.name property. export
 * SPRING_CONFIG_NAME=foo Now when we run the spring boot application, it will
 * load all the properties from foo.properties file.
 * 
 * (3) Bind Fields to Property Values with @ConfigurationProperties: prefix are
 * valid to bind to this object in property
 * 
 * @ConfigurationProperties(prefix="spring.datasource", ignoreUnknownFields =false)
 * 
 * (4) Include Additional Configuration Files we can have the following import statement in
 *  application.properties file. spring.config.import=classpath:datasource.properties,
    classpath:mysql-properties.yml, optional:file:./cloud-deployment.properties, classpath:test-properties/
 * 
 * (5) Initial db data: spring.sql.init.platform= Platform to use in the  default schema or data  script locations,
 * schema-${platform}.sql and data-${platform}.sql. Locations of the data(DML) scripts to apply to the database: defualt
   is in classpath such as:  
   spring.sql.init.data-locations=classpath: db/spring.sql.init.schema-locations=
 **/
public class DBConfigurationProperties {

	/**
	 * without provide self Bean Factory, environment will provide default :
	 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.
	 Bean is a singleton as default, native EntityManager which is not
	 this method name must be entityManagerFactory (cannot be entityManagerFactoryBean)
	 otherwise Jakarta.Persistence loaded RootBeanDefinition and try to load entityManagerFactory but "Not found"
	 **/
	/**  @Bean 
	  public EntityManagerFactory entityManagerFactory () 
	  {
	  
		  final EntityManagerFactory entityManagerFactory;  // jpaSharedEM_AWC_entityManagerFactoryBean
		  
		  try { 
			  entityManagerFactory = Persistence.createEntityManagerFactory("SpringBootWebOracle"); 
		  } catch (Throwable ex) 
		  {
			  System.err.println("Initial EntityManagerFactory creation failed."+ ex);
			  throw new ExceptionInInitializerError(ex); 
		  }
		  
		  return entityManagerFactory; 
	 }
	**/
	
	  /** Issues: (1) with data.sql and schema.sql, run schema.sql syntax errors  
	   * resolved (2) if only data.sql in classpath (main/resources), it runs before Entity DDL schema generated**/
	  @Bean 
	  public LocalContainerEntityManagerFactoryBean entityManagerFactory() 
	  {
		  HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		  vendorAdapter.setGenerateDdl(true);
		  vendorAdapter.setShowSql(true);
		  vendorAdapter.setDatabase(Database.H2);
		  
		  LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		  factory.setPackagesToScan("com.springbootjpaangular2.domain");
		  factory.setJpaVendorAdapter(vendorAdapter);
		  factory.setDataSource(dataSource());

		  factory.setJpaProperties(additionalProperties()); 
	  
		  return factory;
	  }
	  
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:testdb;MODE=Oracle;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");  
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		return dataSource;
	}
	
	/** to put property in first loaded application.properties better
	 *  put data.sql in classpath:  src/main/resources  or specify another folder in classpath (src/main/resources)
	 *  Or spring.sql.init.data-locations=classpath:META-INF/data.sql  in application.properties
	 *  set jakarta.persistence.sql-load-script-source=true in application.properties, not in this method
	 *  Then, data.sql will run after DDL (Entity) generated to void "Table not found"  error.
	 *  Log file (console screen) does not show the insert/data.sql scripts, but ddl run after DDL generated.
	 *  It seems data.sql is run at first query database
	 **/
	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("spring.jpa.generate-ddl", "true");
		hibernateProperties.setProperty("spring.datasource.initialization-mode", "always");
		hibernateProperties.setProperty("spring.jpa.hibernate.hbm2ddl.auto", "create-drop");	
		hibernateProperties.setProperty("spring.sql.init.mode", "always");
		hibernateProperties.setProperty("spring.jpa.hibernate.dialect", "org.hibernate.dialect.H2Dialect"); 
		hibernateProperties.setProperty( "spring.jpa.hibernate.cache.use_second_level_cache", "false");
	    hibernateProperties.setProperty("spring.jpa.hibernate.show_sql", "true");
		hibernateProperties.setProperty( "spring.jpa.hibernate.enable_lazy_load_no_trans", "true");
		//hibernateProperties.setProperty( "spring.sql.init.data-locations", "META-INF/data.sql"); // OK
		hibernateProperties.setProperty("jakarta.persistence.sql-load-script-source", "META-INF/data.sql");

		return hibernateProperties;
	}

    
	/**
	 * It is not JTA transaction which (JTA) spans multiple persistence units (or
	 * other databases), JTA support both of local transaction vs global transaction
	 * (Distributed Transactions). JPA only support local transaction.
	 * 
	 * For Spring support distribute transaction, user have to use
	 * JtaTransactionManager and JNDI setting (normally in Application Server such
	 * as JBoss container environment). But the returned interface can be
	 * PlatformTransactionManager
	 **/
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
