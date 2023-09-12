package com.springbootjpaangular2.services;

import com.springbootjpaangular2.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;


/** 
 * @author lyi
 * 08/2020
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {
    // Application managed
	private final EntityManagerFactory entityManagerFactory;	
	
	@Autowired // singleton
	OrdersServiceImpl (EntityManagerFactory factory) {
		this.entityManagerFactory = factory;
	}
	
	
	@Override  
	public EntityManagerFactory getEntityManagerFactory () {
		return this.entityManagerFactory;
	}
		
		
    @Override
    public List<Orders> listAllOrders() {
    	EntityManager em = entityManagerFactory.createEntityManager(); 
    	List<Orders> ords = em.createQuery("SELECT p from Orders p", 
    			Orders.class).getResultList();
    	em.close();        
        
    	return ords;
    }
   

    @Override
    public Orders getOrderById(Integer id) {
    	// different class Id, different Instance of EntityManger, thread safe
    	EntityManager em = entityManagerFactory.createEntityManager();   	
    	Orders pro = em.find(Orders.class, id);
	    em.close();
        return pro;
    }

    
    @Override
    public Orders createOrder(Orders order) { 
    	EntityManager em = entityManagerFactory.createEntityManager();
    	EntityTransaction trns = em.getTransaction();
    	
    	 trns.begin();
         em.persist(order);         
         trns.commit(); 
         em.close(); 
        
        return order;  
    }
    
    
    @Override
    public List<Offers> retrieveOffers() {
    	EntityManager em = entityManagerFactory.createEntityManager(); 
    	List<Offers> ofs = em.createQuery("SELECT p from Offers p", 
    			Offers.class).getResultList();
    	em.close();        
        
    	return ofs;
    }
    
    
    @Override
    public Orders updateWholeOrder(Orders order)  throws Exception {
    	EntityManager em = entityManagerFactory.createEntityManager();
    	Integer orderNo = order.getOrder_no();
    	String owner =  order.getOwner();
    	Integer offerNo =  order.getOffer_no();
    	
    	EntityTransaction trns = em.getTransaction();
    	
    	trns.begin();      	
    	Orders ords = em.find(Orders.class, orderNo);  
		Orders.setAllFieldValue(order, ords); 
		
		order.setOrder_no(orderNo);
		order.setOffer_no(offerNo);
		order.setOwner(owner);
		
		trns.commit();
		
        em.close();          
        return ords;  
    }

  
    @Override
    public void deleteOrder(Integer id) {
    	EntityManager em = entityManagerFactory.createEntityManager(); 
    	EntityTransaction trns = em.getTransaction();
    	
    	trns.begin();
    	Orders ord = em.find(Orders.class, id);
    	
    	em.remove(ord);// in remove queue but not detached
    	em.flush(); 
    	em.clear(); 
        trns.commit(); 
        em.close(); 
    }
}
