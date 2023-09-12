package com.springbootjpaangular2.domain;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;  // switch to junit-5
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.mockito.Mockito.*;
//import static org.mockito.BDDMockito.*;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.springbootjpaangular2.controllers.QuoteOfferController;

 

/** 
 * @author lyi
 * 08/2020
 * 
 * For angular-springboot-jpa, testing focus on service (business logic),
 * persistence, request mapping, and response. In this project, persistence, 
 * mapping and response are concerned.
 * 
 * org.springframework.mock package more used for Unit(isolated) testing.
 * org.springframework.test package more used for integration(real context, JPA) testing.
 * org.springframework.boot.test package used for auto-configuration of testing context.
 *
 *
 * 	(1) 
 *  for using spring boot based test context 
 *  @SpringBootTest
 * 	Automatically searches for a @SpringBootConfiguration 
 *  when nested @Configuration is not used, 
 * 	and no explicit classes are specified. Then,
 * 	@SpringBootConfiguration will cause auto-search @configuration and componentScan
 * 	based on its location as base package, finally, all @configuration and component 
 *  classes found.
 * 	So, it is using REAL application configurations.
 *  
 *	(2) 
 *	for using regular Spring TestContext Framework
 *	@ExtendWith(SpringExtension.class)  // JUNIT-5, @Order(n) and WebApplicationContext Autowired to work
 *	@WebAppConfiguration  // used together with @ContextConfiguration
 *	In test, use same configuration as REAL application, web and db
 *	@ContextConfiguration(classes = {WebConfiguration.class, DBConfigurationProperties.class, SpringBootWebApplication.class}) 
 *
 *  (3)
 *  for both test context/framework
 *  @AutoConfigureMockMvc  // this waiver of building mockMvc from WebApplicationContext but less control by developer
 *	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 *	
 *	Real application configuration/context used In either case of @SpringBootTest or @ContextConfiguration
 */
@SpringBootTest   
@AutoConfigureMockMvc  
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpringBootIntegrationTest {
   
    @Autowired
    private  WebApplicationContext webApplicationContext;
    
    @Autowired
	private MockMvc mockMvc; 
    /** 
     * Three kinds of Mock Object: Stub, Mock, Proxy/Spy (inside wired to real object)
     * 
     * Mocks can be registered by type or by bean name. Any existing single 
     * bean of the same type defined in the context will be replaced by the mock.
     * 
     * Any Mock Object, we have to provide implementation (fake function or data)
     * or wire it to real object. Otherwise it does nothing. Normally, we have
     * to create Mock Object by Subclass/create new Object/others to replace real Object.
     * 
     * for example:  Mockito's @Mock 
     * @Mock
     * UserRepository mockRepository;
     *
	 * @Test
	 * public void givenCountMethodMocked_WhenCountInvoked_ThenMockValueReturned() {
	 *      // mock count() function
	 *      Mockito.when(mockRepository.count()).thenReturn(123L);
	 *      // call count() test
	 *      long userCount = mockRepository.count();
     *
	 *      Assert.assertEquals(123L, userCount);
	 *      Mockito.verify(mockRepository).count();
	 * }
	 * 
	 * for example, Spring Boot's @MockBean
	 * @MockBean
	 * EmployeeRepository repository;
	 * 
	 * @Autowired
	 * private WebTestClient webClient;
	 *
	 * @Test
	 * void testCreateEmployee() {
	 *   Employee employee = new Employee();
	 *   employee.setId(1);
	 *   employee.setName("Test");
	 *   employee.setSalary(1000);
	 *   // Implement the repository behavior of MockBean 
	 *   Mockito.when(repository.save(employee)).thenReturn(Mono.just(employee));
	 *   
	 *   // Bellow will call MockBean repository instead of real repository
	 *   webClient.post()
	 *           .uri("/create")
	 *           .contentType(MediaType.APPLICATION_JSON)
	 *           .body(BodyInserters.fromObject(employee))
	 *           .exchange()
	 *           .expectStatus().isCreated();
	 * 
	 *    Mockito.verify(repository, times(1)).save(employee);
	 *  }
	 * }
	 * 
     */
    //@MockBean
    //private QuoteOfferServiceImpl service;
    
    @BeforeEach
    public void initTests() { 	
    	 mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    	// fine control:  MockMvcBuilders.standaloneSetup(QuoteOfferController);	
    }
    
    
    @Test
    @Order(1)
    public void testCreateJsonObject() throws Exception { 	   		
    	Quotes qt = mockQuotes("shouldCreateQuotes");
  
        byte[] r1Json = toJson(qt);

        MvcResult result =  this.mockMvc.perform(post("/savequote")
                .content(r1Json)
                .header("reqaction", "new")
                .contentType(MediaType.APPLICATION_JSON)  //MediaType.APPLICATION_JSON
                .accept(MediaType.APPLICATION_JSON))  // going to server
                .andExpect(status().isOk())  // coming back from server response, 200 status code
                .andReturn();
        
        Integer req_no = Integer.valueOf(result.getResponse().getHeader("new_request_no"));
    	assertTrue(result.getResponse().getContentAsString() != null && req_no != null);
        
    	qt.setRequest_no(req_no);
        Offers ofs = mockOffers(req_no, 1);
        List<Offers> detail = new ArrayList<Offers>();
        
        detail.add(ofs);
        ofs = mockOffers(req_no, 0);
        detail.add(ofs);
        qt.setOffers(detail);
        
        r1Json = toJson(qt);

        result =  mockMvc.perform(post("/savequote")
                .content(r1Json)
                .header("reqaction", "save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    	
    	assertTrue(result.getResponse().getHeader("message").indexOf("Save quote succeed") != -1);  
        assertTrue(qt.getOffers().size() ==2);
        
    }
    
    
    @Test
    @Order(2)
    public void testUpdateWholeQuote() throws Exception {    	
    	MvcResult result = mockMvc.perform(get("/listquotes")
                .accept(MediaType.APPLICATION_JSON))  //MediaType.APPLICATION_JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	List<Quotes> qts = mapper.readValue(result.getResponse().getContentAsString(), 
    			new TypeReference<List<Quotes>>(){});
    	Quotes qt = qts.get(0);
        
        qt.setRequested_by("Leonard Yi");
        qt.setAllocated_qty(new BigDecimal(1400.00));
        qt.setRequest_price(new BigDecimal(2100.00));
        qt.setDescription("update whole fields blindly");
        byte[] r1Json = toJson(qt);
        
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/savequote")
    			.content(r1Json)
    			.header("reqaction", "save")
    			.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
                                           
        this.mockMvc.perform(builder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().bytes(r1Json))
                    .andDo(MockMvcResultHandlers.print());	       
    }
   
   
    
    @Test
    @Order(3)
    public void testRetrieveQuote () throws Exception {	
    	Quotes r1 = mockQuotes("shouldCreateQuote");  	  
        byte[] r1Json = toJson(r1);

        MvcResult result =  this.mockMvc.perform(post("/savequote")
                .content(r1Json)
                .header("reqaction", "new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))  
                .andExpect(status().isOk()) 
                .andReturn();
        
        Integer req_no = Integer.valueOf(result.getResponse().getHeader("new_request_no"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/getquote")
        		.queryParam("request_no", req_no.toString())
                .accept(MediaType.APPLICATION_JSON);          
        ObjectMapper map = new ObjectMapper();
        
        result = this.mockMvc.perform(builder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();	 
        
        assertTrue(map.readValue(result.getResponse().getContentAsString(), 
        		Quotes.class).getRequest_no() != null);
        
    }
    
    
    private Offers mockOffers (Integer rfqNo, int a) {
    	Offers ofs = new Offers();
    	ofs.setOwner("BAMBOOROSE");
    	ofs.setRequest_no(rfqNo);  	 	
    	ofs.setOffer_date(new java.sql.Date(new Date().getTime()));
    	ofs.setSupplier("Ling");
    	ofs.setDescription("for testing");
    	ofs.setStatus("Open");
    	ofs.setCurrency("USD");
    	ofs.setPayment_type("COL");
    	ofs.setBuyer("Ling");
    	ofs.setVendor_name("Peter");
    	if (a ==1) {
    		ofs.setItem("fish");  
	     	
    	} else {
    		ofs.setItem("Pork");  
    	}
    			
    	return ofs;	
    	
    }
    
    
    private Quotes mockQuotes(String prefix) {
     	Quotes qts = new Quotes();
     	//qts.setRequest_No(91);
    	qts.setOwner("BAMBOOROSE");// non-auto generated key, must set value
    	qts.setItem("Beef");   	
    	qts.setDescription("Food Ingredient");
    	qts.setRequested_by("Lun Yi");
    	qts.setCategory("Food");
    	qts.setItem_class("First");
    	qts.setDivision("Food");
    	qts.setBrand("Stop-Shop");
    	qts.setModify_user("Lun Yi");
    	qts.setModify_ts(new Timestamp(new Date().getTime()));
    	
        return qts;
    }

    
    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}