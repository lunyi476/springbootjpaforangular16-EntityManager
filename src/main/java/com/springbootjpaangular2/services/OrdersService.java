package com.springbootjpaangular2.services;


import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.springbootjpaangular2.domain.Offers;
import com.springbootjpaangular2.domain.Orders;


public interface OrdersService {
	List<Orders> listAllOrders();
    // Update all fields as we don't know specific changed fields
    Orders updateWholeOrder(Orders order) throws Exception;
    
    Orders createOrder(Orders orders);

    void deleteOrder(Integer id);
    
    public EntityManagerFactory getEntityManagerFactory ();

	Orders getOrderById(Integer id);
	
	public List<Offers> retrieveOffers();

}
