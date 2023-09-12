package com.springbootjpaangular2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;



/** 
 * @author lyi
 * 08/2020
 * 
 * Configuration, Initializer, Context and ContextLoader 
 * are important elements for launching application.
 * 
 * Algorithm to determine WebApplicationType:
 * If Spring MVC is present, an AnnotationConfigServletWebServerApplicationContext is used
 * If Spring MVC is not present and Spring WebFlux is present, 
 * an AnnotationConfigReactiveWebServerApplicationContext is used.
 * 
 * Otherwise, AnnotationConfigApplicationContext is used
 */
@SpringBootApplication
public class SpringBootWebApplication {
	private static ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
    	ctx = SpringApplication.run(SpringBootWebApplication.class, args);
    }
}