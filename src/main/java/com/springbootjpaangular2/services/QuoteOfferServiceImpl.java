package com.springbootjpaangular2.services;

import com.springbootjpaangular2.domain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Session;

import io.reactivex.Observable;

/**
 * 
 * @author lyi
 * 08/2023
 * 
 */
@Service
public class QuoteOfferServiceImpl implements QuoteOfferService {
	private static final Logger logger = LoggerFactory.getLogger(
			QuoteOfferServiceImpl.class);
	  
	   /** two ways to get EntityManager, EntityManager is actually DAO (not a necessary layer), 
	    *  Quotes object is actually DTO 
	    **/
	  /** (1)
	   Application managed, thread safe, can create thread safe EntityManager too (but injected EntityManager is not thread safe)
	  
	   private final EntityManagerFactory entityManagerFactory;	
	  	
	  @Autowired // singleton QuoteOfferServiceImpl (EntityManagerFactory factory)
	  { this.entityManagerFactory = factory; }
	 
	  @Override public EntityManagerFactory getEntityManagerFactory () { return
	  this.entityManagerFactory; }
	  
	  With an application-managed entity manager, on the other hand, the persistence context 
	  is not propagated to application components, and the lifecycle of EntityManager instances 
	  is managed by the application.
	  We have to use below to managing every transaction.
	  entityManagerFactory.createEntityManager().begin() ..............  
	  entityManagerFactory.createEntityManager().close().
	
	
	  (2)
	   Container managed and Thread safe (Proxy wrapped EntityManager)
	   With a container-managed entity manager, an EntityManager instance’s persistence context 
	   is automatically propagated by the container to all application components that use the 
	   EntityManager instance within a single Java Transaction API (JTA OR JPA if not need cross database) transaction.
	   JTA transactions usually involve calls across application components. To complete a JTA transaction, 
	   these components usually need access to a single persistence context.
	   This occurs when an EntityManager is injected into the application components by means of 
	   the javax.persistence.PersistenceContext annotation. The persistence context is automatically propagated 
	   with the current JTA transaction, and EntityManager references that are mapped 
	   to the same persistence unit provide access to the persistence context within that transaction. 
	   By automatically propagating the persistence context, application components don’t need
	   to pass references to EntityManager instances to each other in order to make changes 
	   within a single transaction. The Java EE container manages the lifecycle of container-managed entity managers.
	   
	   (3)  However, the container (JakartaEE or Spring) injects a special proxy instead of a simple EntityManager here. 
	   Spring, for example, injects a proxy of type SharedEntityManagerCreator. 
	   Every time we use the injected EntityManager, this proxy will either reuse the existing EntityManager or create a new one. 
	   Reuse usually occurs when we enable something like Open Session/EntityManager in View. 
       Either way, the container ensures that each EntityManager is confined to one thread.
	   **/
	  @PersistenceContext  
	  @Autowired
	  public EntityManager em;
	  

		
		/** get, find, load are auto-committed/transaction **/
	    @Override
	    public List<Quotes> listAllQuotes() {
	    	List<Quotes> qts = em.createQuery("SELECT p from Quotes p", 
	    			Quotes.class).getResultList();  
	    	em.close();
	    	
	    	return qts;
	    }
	   
	
	    @Override
	    public Quotes getQuoteById(QuotesKeys id) {	
	    	//  EntityManager em = entityManagerFactory.createEntityManager(); 
	    	Quotes qts = em.find(Quotes.class, id);	  
	    	em.close();
	        return qts;
	    }
	
	    /**
	     *  this @Transaction will be the same as created that of 
	     *  @PersistenceContext 
	     *  EntityManager em (container managed and created)
	     *  
	     *  if use EntityManager em = entityManagerFactory.createEntityManager();
	     *  this transaction from container managed EntityManager 
	     *  will be different from em.getTransaction();
	     *  
	     *  So, not use EntityManager em = entityManagerFactory.createEntityManager();
	     *  with @Transactional together
	     */
	    @Override
	    @Transactional 
	    public Quotes createQuote(Quotes quote) { 

	    	List<Offers> offersNew = new ArrayList<Offers>();
	    	
	    	
	    	if (quote.getRequest_no() == null) { // should be null
		        /**
		         *  In case user add new quotes and offers at the same time due to simplified screen logic.
		         *  This should not happen at real production. 
		         *  
		         *  Quote and Offer cannot be created at same transaction because 
		         *  request_no/owner unknown yet. Persist(quote), then set request_no
		         *  to offer, finally flush and committed transaction.
		         *  
		         *  new ArrayList<Offers>(quote.getOffers()) for
		         *  avoiding java.util.ConcurrentModificationException.
		         *  
		         *  for ( Offers o : offers) {
		      	 *	  offers.remove(o);
		      	 *  }  	
		      	 * 	
		         *  cause error: 
		         *  at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:909)
		         *  java.util.ConcurrentModificationException: null: exception because 
		         *  you are modifying the list while iterating, which means if the Collection 
		         *  will be changed while some thread is traversing over it using iterator, 
		         *  the iterator.next() 
		         *         
		         **/
	    		for (Offers o: new ArrayList<Offers>(quote.getOffers())) {
	    			offersNew.add(o);
	    			quote.removeOffer(o);
	    		}
	    	} 
	    	
	         em.persist(quote); // In managed context      
	         for (Offers o: offersNew) {
	        	 quote.addOffer(o); // in managed context
	         }	 
	        
	        return quote;  
	    }
	    /** 
	     * Update strategy: 
	     * Copy all values (except generated keys) of offers from posted source into
	     * origin target offers in context.
	     * 
	     * Another way is post changed fields only and reset it. 
	     */
	    @Override
	    @Transactional // without this we must manually set to begin/commit transaction
	    public Quotes updateWholeQuote(Quotes quote) throws Exception {
	    	Integer reqNo = quote.getRequest_no();
	    	String owner =  quote.getOwner();

	    	Quotes qts = em.find(Quotes.class, new QuotesKeys(reqNo, owner));   
	        /**  
	         *  Simply remove all offers of target, add/copy all field value from source.
	         *  Otherwise either passing specific updated fields and offers(deleted/added)
	         *  from client side or check changed fields and offers one by one
	         *  
	         **/ 
	    	qts.removeAllOffers(); 
			Quotes.setAllFieldValue(quote, qts);    

	        return qts;  
	    }
	    
	    
	    @Override
	    @Transactional
	    public void deleteQuote(QuotesKeys id) { 
	    	Quotes qts = em.find(Quotes.class, id);	    	
	    	em.remove(qts);// in remove queue but not detached	
	    }
	        
	   
	    @Override
	    public List<Offers> listAllOffers() {
	    	// EntityManager em = entityManagerFactory.createEntityManager(); 
	    	List<Offers> ofs = em.createQuery("SELECT p from Offers p", 
	    			Offers.class).getResultList();  
	    	em.close();
	    	return ofs;
	    }
	   
	
	    @Override
	    public Offers getOfferById(OffersKeys id) {  	
	    	Offers ofs = em.find(Offers.class, id);	 
	    	em.close();
	        return ofs;
	    }
  
}
