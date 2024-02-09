package com.springbootjpaangular2.controlleradvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springbootjpaangular2.controllers.QuoteOfferController;

/** 
 * Any exception will come to this central advice
 * 
 *@ExceptionHandler, @InitBinder, and @ModelAttribute methods apply only to the @Controller class, or class hierarchy,
 *in which they are declared. If, instead, they are declared in an @ControllerAdvice or @RestControllerAdvice class, 
 *then they apply to any controller. Moreover, as of 5.3, @ExceptionHandler methods in @ControllerAdvice can be used 
 *to handle exceptions from any @Controller or any other handler.
 *
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(
			ExceptionControllerAdvice.class);
	
	@ExceptionHandler(value=Exception.class)
	public void  exceptionHandler(HttpServletRequest req, HttpServletResponse res, Exception exc) { 
		logger.debug(exc.getMessage()+" at "+req.getRequestURL(), exc);		
		res.setStatus(500);
	
		try {
		 	String xmlSt =  QuoteOfferController.XmlConverterHelper.getResponseXML(
		 				null, null, "Error :  " + exc.getMessage()+"  at "+req.getRequestURL());
			//Lazy error handling at server side, let browser to handle and display		
			res.getWriter().print(xmlSt);
			
		} catch (Exception er) {}
	}		
}
