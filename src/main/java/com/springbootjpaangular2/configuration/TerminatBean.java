package com.springbootjpaangular2.configuration;

import jakarta.annotation.PreDestroy;

public class TerminatBean {
	 
    @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed!");
    }
}