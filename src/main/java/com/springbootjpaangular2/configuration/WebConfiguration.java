package com.springbootjpaangular2.configuration;


import org.h2.server.web.WebServlet;
//import org.apache.catalina.connector.Connector;
//import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
//import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/** 
 * @author lyi
 * 08/2020
 * 
 * Configuration Documents:  Bean or WebMvcConfiguration/WebMvcConfigurationSupport
 * Bean:      https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/special-bean-types.html
 * WebConfig: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config.html
 * 
 * spring-framework/spring-webmvc/src/main/resources/org/springframework/web/servlet/DispatcherServlet.properties
 * 
 * 
 * Auto-configuration could be overridden by manual configuration.
 * If no manual configuration and auto-configuration class 
 * existing in classpath, auto-config will happen.
 * 
 * @EnableConfigurationProperties annotation is strictly connected 
 * to @ConfiguratonProperties
 */
@Configuration(proxyBeanMethods = false)
//@EnableWebMvc
public class WebConfiguration { // extends WebMvcConfigurationSupport  or Implements WebMvcConfigurer
	
    @Bean
    public TerminatBean getTerminateBean() {
        return new TerminatBean();
    }
    
	@Bean  
	public MessageSource messageSource() { 
	    ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	     
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	// Tomcat 8.5 does not allow [] in encoding, to allow it
	/**@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setProperty("relaxedQueryChars", "|{}[]");
	        }
	    });
	    return factory;
	}**/
}
