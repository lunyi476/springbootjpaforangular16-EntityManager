package com.springbootjpaangular2.controllers;


import com.springbootjpaangular2.services.OrdersService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootjpaangular2.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** 
 * @author lyi
 *
 */
@Controller
@CrossOrigin(origins="*")
public class OrdersController {
	private static final Logger logger = LoggerFactory.getLogger(
			OrdersController.class);

    private OrdersService ordersService;

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
    
	public static String DEFAULT_OWNER = "BAMBOOROSE";
   
	
    @Autowired
    private MessageSource  messageResource;
   
    @Autowired
  	@Qualifier("todoValidator")
  	private org.springframework.validation.Validator validator2;
  	
  	@InitBinder
  	private void initBinder(WebDataBinder binder) { 
  		 binder.setValidator(validator2);
  	}
  	
 	
    @ResponseBody 
    @GetMapping(value = "/listorders", produces = MediaType.APPLICATION_JSON_VALUE)  
    public  List<Orders> listOrders(
    		HttpServletRequest request, HttpServletResponse response) { 
    	List<Orders> all = ordersService.listAllOrders();	
    	return all;
    }
    
    
    @ResponseBody 
    @GetMapping(value = "/getorder", produces = MediaType.APPLICATION_JSON_VALUE)  
    public  Orders getOrder ( 
    		HttpServletRequest request, HttpServletResponse response) 
    				throws JsonMappingException, JsonParseException, IOException {   	
    	Integer  orderNo = Integer.valueOf(request.getParameter("order_no"));
    	Orders all = ordersService.getOrderById(orderNo);
    
    	return all;
    }    

    
    @ResponseBody 
    @GetMapping(value = "/retrieveoffers", produces = MediaType.APPLICATION_JSON_VALUE)  
    public List<Offers> retrieveOffers(HttpServletRequest request, 
    		HttpServletResponse response) {
    	
        	List<Offers> all = ordersService.retrieveOffers();	
        	return all;
    }
    
    
    @DeleteMapping (value ="/deleteorder")
    public ResponseEntity<String>  delete(HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
    	Integer orderNo = Integer.valueOf(request.getParameter("order_no"));
    	ordersService.deleteOrder(orderNo);
        // Response body must be json type even empty
	    JSONObject resp = new JSONObject();  	
    	HttpHeaders headers = new HttpHeaders();

        headers.add("Access-Control-Expose-Headers", "*");
    	headers.add("message", messageResource.getMessage(
    			"order.delete.respmessage", null, Locale.ENGLISH));

    	return new ResponseEntity<String>(
    				  resp.toString(), headers,
    			      HttpStatus.OK);    	
    } 
    
    
    @PostMapping(value ="/saveorder", consumes= "application/json") 
    public  ResponseEntity<String>  createOrders(@RequestBody @Validated Orders order,   BindingResult result,
    		HttpServletRequest request, HttpServletResponse response) 
    				throws Exception { 	
    	
	    String action = request.getHeader("reqaction");
    	Orders ord = null;
    	
    	if (action != null && action.contentEquals("new"))  {
    		ord = ordersService.createOrder(order);
    	}
        else if (action != null && action.contentEquals("save")) {
        	ord = ordersService.updateWholeOrder(order);
        }
    	
    	ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString Response body
        String jsonString = mapper.writeValueAsString(ord);
    	HttpHeaders headers = new HttpHeaders();

        //For client side to add number into new Order
    	if (action != null && action.contentEquals("new") && ord != null)
    		headers.add("new_order_no", String.valueOf(ord.getOrder_no()));
        headers.add("Access-Control-Expose-Headers", "*");
    	headers.add("message", messageResource.getMessage(
    			"order.save.respmessage", null, Locale.ENGLISH) );

    	return new ResponseEntity<String>(
    			jsonString, headers,
    			      HttpStatus.OK);    	
    }
    
}
