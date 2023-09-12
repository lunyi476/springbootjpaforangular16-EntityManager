package com.springbootjpaangular2.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.springbootjpaangular2.services.QuoteOfferService;
import com.springbootjpaangular2.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
//import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
//org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder
import org.springframework.http.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * For handling request from Angular 8
 * return body (Object), ResponseEntity by json type and not string 
 * (that will trigger view mapping which we don't want) 
 * 
 * @author lyi
 * 08/2020
 * 
 * With Spring Web you have two choices to write your app:
 *	(1) Write a server-side rendered application with JSPs, Thymeleaf templates, 
 *      Freemaker templates -> Spring MVC pattern
 *	(2) Write a RESTful backend for e.g. a Single Page Application (like ReactJs or Angular) 
 *      which gets its data from your RESTful backend
 *      
 *  Here, we use (2) and the environment is boot (although inside is embedded web environment) 
 *  instead of tradition server deploying web environment.
 */
@Controller  
@CrossOrigin(origins="*")
public class QuoteOfferController {
	private static final Logger logger = LoggerFactory.getLogger(
			QuoteOfferController.class);
	
	public static String DEFAULT_OWNER = "BAMBOOROSE";
    private QuoteOfferService quoteOfferService;

    @Autowired
    public void setQuoteOfferService(QuoteOfferService quoteOfferService) 
    		throws ParseException {
        this.quoteOfferService = quoteOfferService;    
    } 
    
    
    /**
     * Used to get message from code, or use context to get MessageResource: 
     * WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext())
     */
    @Autowired
    private MessageSource  messageResource;
    
    
    /**
     * Validator and DataBinder make up the validation. Inside BeanWrapper, 
     * PropertyEditorSupport
     */
    @Autowired
	@Qualifier("todoValidator")
	private org.springframework.validation.Validator validator2;
    
    
	
    /**
     *  For dynamic Bean validation, Data Binding(user input to Bean)
     *  and event Bean field type conversion (besides annotation field
     *  type validation and conversion).
     */
    @InitBinder
	private void initBinder(WebDataBinder binder) { //ServletRequestDataBinder
		/** 
		 *  To trigger validation and Custom Editor, have to put @Validated
		 *  and @Pathvariable Or @RequestParam("request_no") in mapping
		 *  method.
		 */
		 binder.setValidator(validator2);
		 binder.registerCustomEditor(Integer.class, "request_no", new QuotesEditor());
	}
	
    
    @GetMapping(value = "/enroll", produces = MediaType.APPLICATION_JSON_VALUE)  
    public ResponseEntity<String>  login (HttpServletRequest request, 
    		HttpServletResponse response)   {   
    	String user = request.getParameter("userName");
    	String pass = request.getParameter("password");
    	JSONObject resp = new JSONObject();
    	logger.info("user----"+user +"     "+"pass----"+pass);
    	HttpHeaders headers = new HttpHeaders();

        headers.add("Access-Control-Expose-Headers", "*");
        // Fake implementation 
    	if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(pass)) {  	 
    		 headers.add("loggedin", "1");
    		 return new ResponseEntity<String>(
    				  resp.toString(), headers,
    			      HttpStatus.OK);
    	}
    	else  {
    		 headers.add("loggedin", "0");
    		 return new ResponseEntity<String>(
   				  resp.toString(),
   			      HttpStatus.OK);		
    	}
    }
    
    
    /**
     * @Pathvariable Or @RequestParam("request_no") Quotes reqNo, 
     * can trigger Custom Editor which set request_no
     */
    @ResponseBody 
    @GetMapping(value = "/listquotes", produces = MediaType.APPLICATION_JSON_VALUE)  
    public  List<Quotes> listQuotes(
    		HttpServletRequest request, HttpServletResponse response) { 
    	List<Quotes> all = quoteOfferService.listAllQuotes();
    		
    	return all;
    }
    
    
    /**
     * @Pathvariable Or @RequestParam("request_no") Quotes reqNo, 
     * can trigger Custom Editor which set request_no
     */
    @ResponseBody 
    @GetMapping(value = "/listquotesxml", produces = MediaType.APPLICATION_XML_VALUE)  
    public  List<Quotes> listQuotesXml(
    		HttpServletRequest request, HttpServletResponse response) { 
    	List<Quotes> all = quoteOfferService.listAllQuotes();
    		
    	return all;
    }
    

    @GetMapping(value = "/listquotesstring", produces = MediaType.APPLICATION_XML_VALUE)  
    public  ResponseEntity<String> listQuotesString (
    		HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException  { 
    	List<Quotes> all = quoteOfferService.listAllQuotes();
    	
    		
    	
    	/**JSONObject outputJson = new JSONObject();
		outputJson.put(CHNG_FIELDS_ARRAY, changeFields);
		outputJson.put(REPEAT_COUNTS, repeatingCount);
		outputJson.put(DOC_VIEW_ID, docViewId);
		outputJson.put(PAGE_NO, pageNo);
    	return all;**/
    	// JSON format to check : class HierarchyMassUpdateResource
    	ObjectMapper Obj = new ObjectMapper();
    	String jsonStr = Obj.writeValueAsString(all);
    	jsonStr = "{ quotes: " + jsonStr +"}";
        // Displaying JSON String
        System.out.println(jsonStr);	
    	//String json = "{employee : { age:30, name : Raja, technology:Java}}";
    	String xml = XmlConverterHelper.convert(jsonStr, "root");

    	return new ResponseEntity<String>(
				  xml, new HttpHeaders(),
			      HttpStatus.OK);   
    }
    
    
    @ResponseBody 
    @GetMapping(value = "/listoffers", produces = MediaType.APPLICATION_JSON_VALUE)  
    public  List<Offers> listOffers(
    		HttpServletRequest request, HttpServletResponse response) { 
    	List<Offers> all = quoteOfferService.listAllOffers();
    	
    	return all;
    }
 
    
    @ResponseBody 
    @GetMapping(value = "/getquote", produces = MediaType.APPLICATION_JSON_VALUE)  
    public  Quotes getQuote ( 
    		HttpServletRequest request, HttpServletResponse response) 
    				throws JsonMappingException, JsonParseException, IOException {   	
    	String owner = request.getParameter("owner") == null? 
    			DEFAULT_OWNER : request.getParameter("owner");
    	Integer  reqNo = Integer.valueOf(request.getParameter("request_no"));
    	Quotes all = quoteOfferService.getQuoteById(new QuotesKeys(reqNo, owner));
    	List<Quotes> alls = new ArrayList<Quotes>();
    	alls.add(all);
 
    	return alls.get(0);
    }    

    
    @DeleteMapping (value ="/deletequote")
    public ResponseEntity<String>  delete(HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
    	Integer reqNo = Integer.valueOf(request.getParameter("request_no"));
    	String owner = request.getParameter("owner") == null? 
    			DEFAULT_OWNER : request.getParameter("owner");	
    
        quoteOfferService.deleteQuote(new QuotesKeys(reqNo, owner));
        // Response body must be json type even empty
	    JSONObject resp = new JSONObject();  	
    	HttpHeaders headers = new HttpHeaders();

        headers.add("Access-Control-Expose-Headers", "*");
    	headers.add("message", messageResource.getMessage(
    			"quote.delete.respmessage", null, Locale.ENGLISH));

    	return new ResponseEntity<String>(
    				  resp.toString(), headers,
    			      HttpStatus.OK);    	
    } 
    
    
    // Save posted either old(update) or new(created) quote
    @PostMapping(value ="/savequote", consumes= "application/json", produces =MediaType.APPLICATION_JSON_VALUE) 
    public  ResponseEntity<String>  createQuotes(@RequestBody  @Validated  Quotes quotes, BindingResult result,
    		HttpServletRequest request, HttpServletResponse response) throws Exception { 
	    String action = request.getHeader("reqaction");
    	Quotes qts = null;
    	/**
    	 * 1. new/save is based on Quote. Old quote may have new Offers.
    	 * 
    	 * 2. For error thrown, let server to return it as 
    	 * HttpErrorResponse to Angular
    	 * 
    	 * 3. When testing without real DB, requet_no and owner must be manually added
    	 */
    	if (action != null && action.contentEquals("new"))  {
    		qts = quoteOfferService.createQuote(quotes);
    	}
        else if (action != null && action.contentEquals("save")) {
        	qts = quoteOfferService.updateWholeQuote(quotes);
        }
    	
    	List<Quotes> alls = new ArrayList<Quotes>();
    	alls.add(qts);
    	
    	ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString Response body
        String jsonString = mapper.writeValueAsString(qts);
    	HttpHeaders headers = new HttpHeaders();

        //For client side to add number into new quote
    	if (action != null && action.contentEquals("new") && qts != null)
    		headers.add("new_request_no", String.valueOf(qts.getRequest_no()));
        headers.add("Access-Control-Expose-Headers", "*");
    	headers.add("message", messageResource.getMessage(
    			"quote.save.respmessage", null, Locale.ENGLISH) );
   
    	return new ResponseEntity<String>(
    			jsonString, headers, //jsonString
    			      HttpStatus.OK);    	
    }
    
    public static class XmlConverterHelper implements Serializable
    {
		private static final long serialVersionUID = 1L;

		public static String CONDITION_PLACEHOLDER = "#condition#";
    	
    	public static String EXTRACONDITION_PLACEHOLDER = "#extra_condition#";
    	
    	public static final String DEFAULT_ENCODING_TYPE = "UTF-8";
    	
    	public static String XML_DECLARATION_TAG = "<?xml version=\"1.0\" encoding=\"" + DEFAULT_ENCODING_TYPE + "\"?>\n";
    	
    	 public  static final  String SUCCESS_STATUS = "SUCCESS";
    	 public  static final  String FAILED_STATUS = "FAILED";
    	 public  static final  String WARNING_STATUS = "WARNING";
    	 private static final  String FAILED_HTTP_STATUS_CODE = "400";
    	 private static final  String WARNING_HTTP_STATUS_CODE = "200";
    	 private static final  String SUCCESS_HTTP_STATUS_CODE = "200";
    	 
    	 
    	 //String json = "{employee : { age:30, name : Raja, technology:Java}}";
    	 //String xml = convert(json, "root");
    	 public static String convert(String json, String root) throws JSONException {
    		 JSONObject jsonObject = new JSONObject(json);
    		 // must have <root></root>
    		 String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<"+root+">" + XML.toString(jsonObject) + "</"+root+">";
    		 return xml;
    	 }
    	/**
    	 * This method builds the message element 
    	 * with status, message_id, message_desc, and token as attributes
    	 * @param status
    	 * @param id
    	 * @param msg
    	 * @param token
    	 * @return
    	 */
    	public static String buildMessageTag(String status, String id, String msg, String token)
    	{
    		StringBuilder outputXml = new StringBuilder();
    		outputXml.append("<message status=\""+status+"\" message_id=\""+id+"\" message_desc=\""+
    				msg+"\"" + (token==null?"":" token=\""+token+"\"")+">");
    		
    		outputXml.append("</message>");
    		return outputXml.toString();
    	}

    	/**
    	 * This method builds the message element 
    	 * with status, message_id and message_desc  as attributes
    	 * @param status
    	 * @param id
    	 * @param msg
    	 * @return
    	 */
    	public static String buildMessageTag(String status, String id, String msg)
    	{
    		 return buildMessageTag(status, id, msg, null);
    	}
    	
    	
    	/**
    	 * This method builds xml start tag of the document.
    	 * @return
    	 */
    	public static String buildDocumentStartTag()
    	{
    		StringBuilder outputXml = new StringBuilder();
    		outputXml.append(XML_DECLARATION_TAG);
    		outputXml.append("<root>");
    		return outputXml.toString();
    	}
    	
    	/**
    	 * @return document end tag String
    	 */
    	public static String buildDocumentEndTag()
    	{
    		return "</root>";
    	}
    	
	    public static String getResponseXML(String strSuccessMsg, 
	    		String strWarningMsg, String strErrorMsg)
	    {
	    	StringBuilder outputXml = new StringBuilder(
	    			XmlConverterHelper.buildDocumentStartTag());
	    	
	    	if (strSuccessMsg != null) {
	    		outputXml.append(XmlConverterHelper.buildMessageTag(
	    				SUCCESS_STATUS, SUCCESS_HTTP_STATUS_CODE, strSuccessMsg));
	        	
	    	}
	    	if (strWarningMsg != null) {
	    		outputXml.append(XmlConverterHelper.buildMessageTag(
	    				WARNING_STATUS, WARNING_HTTP_STATUS_CODE, strWarningMsg));
	        	
	    	}
	    	if (strErrorMsg != null) {
	    		outputXml.append(XmlConverterHelper.buildMessageTag(
	    				FAILED_STATUS, FAILED_HTTP_STATUS_CODE, strErrorMsg));
	        	
	    	}
	    	outputXml.append(XmlConverterHelper.buildDocumentEndTag());
	    	return outputXml.toString();
	    }
    }
}
