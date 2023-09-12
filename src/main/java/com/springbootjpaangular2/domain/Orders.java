package com.springbootjpaangular2.domain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.sql.Date;


/**
 * Orders entity/table
 * @author lyi
 *
 */
@Entity(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDERS_NO_SEQ")
    @SequenceGenerator(name="ORDERS_NO_SEQ", sequenceName = "ORDERS_NO_SEQ") 
    private Integer order_no;
    
    @NotNull 
    @Column(nullable = false)
    private String owner ="BAMBOOROSE";
    
    @NotNull 
    @Column(nullable = false) 
    private Integer offer_no;

    @NotNull 
    @Column(nullable = false)
    private String item;
  
    private String description;
    private Date   order_date;
    private String buyer;
    private String status;
    private String supplier;;
    private String vendor_name;
    private String manufacturer;
    private String carrier;
    private String delivery_terms;
    private String payment_type;
    private String payment_terms;
    private String currency;    
    private String origin_cntry;
    private Date first_ship_date;
    private Date last_ship_date;
    private String lading_point;
    private String final_dest;
 

    @Digits(integer=5, fraction=2)
    private BigDecimal tot_grs_value;

    @Digits(integer=5, fraction=2)
    private BigDecimal tot_calc_cost;

    @Digits(integer=5, fraction=2)
    private BigDecimal tot_qty;  

    @Digits(integer=5, fraction=2)
    private BigDecimal tot_grs_wgt;
    
    private Timestamp modify_ts;
    private String modify_user;
  
    public Orders () {};
   
    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no (Integer orderno) {
        this.order_no = orderno;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getOffer_no() {
        return offer_no;
    }

    public void setOffer_no (Integer offerno) {
        this.offer_no = offerno;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date orderDate) {
        this.order_date = orderDate;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDelivery_terms() {
        return delivery_terms;
    }

    public void setDelivery_terms(String delivery_terms) {
        this.delivery_terms = delivery_terms;
    }
    
    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
    
    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getOrigin_cntry() {
        return origin_cntry;
    }

    public void setOrigin_cntry(String origin_cntry) {
        this.origin_cntry = origin_cntry;
    }
    
    public Date getFirst_ship_date() {
        return first_ship_date;
    }

    public void setFirst_ship_date(Date first_ship_date) {
        this.first_ship_date = first_ship_date;
    }
    
    public Date getLast_ship_date() {
        return last_ship_date;
    }

    public void setLast_ship_date(Date last_ship_date) {
        this.last_ship_date = last_ship_date;
    }
    

    public String getLading_point() {
        return lading_point;
    }

    public void setLading_point(String lading_point) {
        this.lading_point = lading_point;
    }
    
    public String getFinal_dest() {
        return final_dest;
    }

    public void setFinal_dest(String final_dest) {
        this.final_dest = final_dest;
    }

    public BigDecimal getTot_grs_value() {
        return tot_grs_value;
    }

    public void setTot_grs_value(BigDecimal tot_grs_value) {
        this.tot_grs_value = tot_grs_value;
    }
    
    public BigDecimal getTot_calc_cost() {
        return tot_calc_cost;
    }

    public void setTot_calc_cost(BigDecimal tot_calc_cost) {
        this.tot_calc_cost = tot_calc_cost;
    }
    
    public BigDecimal getTot_qty() {
        return tot_qty;
    }

    public void setTot_qty(BigDecimal tot_qty) {
        this.tot_qty = tot_qty;
    }
    
    public BigDecimal getTot_grs_wgt() {
        return tot_grs_wgt;
    }

    public void setTot_grs_wgt(BigDecimal tot_grs_wgt) {
        this.tot_grs_wgt = tot_grs_wgt;
    }
     
    public Timestamp getModify_ts() {
        return modify_ts;
    }

    public void setModify_ts(Timestamp modify_ts) {
        this.modify_ts = modify_ts;
    }

    public String getModify_user() {
        return modify_user;
    }

    public void setModify_user(String modify_user) {
        this.modify_user = modify_user;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orders )) return false;
        return order_no != null && order_no.equals(((Orders) o).getOrder_no()) && 
        	   owner != null && owner.equals(((Orders) o).getOwner()) &&
        	   offer_no != null && offer_no.equals(((Orders) o).getOffer_no()) ;
    }
    
  
    @Override
    public int hashCode () {
    	final int prime = 98;
        int result = 1;
        result = prime * result+ ((offer_no == null) ? 0 : offer_no.hashCode());
        result = prime * result+ ((owner == null) ? 0 : owner.hashCode());
        result = prime * result+ ((order_no == null) ? 0 : order_no.hashCode());

    	return result;
    }
    
    public static void setAllFieldValue  (Orders source, Orders target) throws Exception { 	
    	Method[] ms = (target.getClass().getDeclaredMethods());
    	List<Method> msListget= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("get")).collect(Collectors.toList());
    	
    	List<Method> msListset= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("set")).collect(Collectors.toList());
    	// cannot cast object[] to Method[]
    	ms = msListget.toArray(new Method[msListget.size()]);

    	for (Method getMd: ms) {
    		if (getMd.getName().equals("getOrder_no") || getMd.getName().equals("getOrder_no") ||
    				getMd.getName().equals("getOwner") || getMd.getName().equals("getQuotes") ||
					getMd.getName().equals("getOffer_no") || getMd.getName().equals("getOffer_no"))
    			continue;
    		
    		Object obj = getMd.invoke(source);
    		String setMethodName = "s"+ getMd.getName().substring(1);
    		
    		Optional<Method> opMd = msListset.stream().filter( 
    				k-> k.getName().equals(setMethodName)).findFirst();
    		
    		Class<?>[] paramTypes = opMd.get().getParameterTypes();
    		
    		Method setMd  = target.getClass().getMethod(setMethodName, paramTypes);	   		
    		setMd.invoke(target, obj);
    	}
    }
}
