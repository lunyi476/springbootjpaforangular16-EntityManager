package com.springbootjpaangular2.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Offers entity/table
 * @author lyi
 *
 */
@Entity(name = "Offers")
@IdClass(OffersKeys.class) // composite key with auto-generation
public class Offers {
	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "OFFERS_NO_SEQ")
	@SequenceGenerator(name="OFFERS_NO_SEQ", sequenceName = "OFFERS_NO_SEQ")
    @Id
    private Integer offer_no;
    
    @Id
    private String owner ="BAMBOOROSE";
    
    @Id
    private Integer request_no;
     
    @NotNull //  for primary type validation
    @Column(nullable = false) // database schema
    private String item;
    
    private String description;
    
    //@JsonProperty("offer_date")
    @JsonFormat(pattern="yyyy-MM-dd") //"yyyy-MM-dd'T'HH:mm:ss.SSS"
    private Date offer_date;
    
    private String status;
    private String buyer;
    private String supplier;
    private String vendor_name;
    private String manufacturer;
    private String carrier;
    private String currency;
    private String payment_type;  
    private String payment_terms;
    private String delivery_terms;
    private String origin_cntry;
    private String lading_point;
    private Date first_ship_date;
    private Date last_ship_date;
    private String final_dest;
    
    public Offers () {};
    
    @ManyToOne (targetEntity=Quotes.class)
    /** 
     * JoinColumn declare side always owning side.
     * 
     * In case of unidirectional mapping, Without JoinColumn 
     * DB foreign key will not be used in mapping no matter foreign key existing or not.
     * 
     * JoinTable normally automatically created by inside based on mapping relation style.
     * But, can use annotation to force using it.
     * 
     * Unidirectional
     * OneToMany mapping not necessary means SQL foreign key existing.
     * Without foreign key or not specify JoinColumn, relation mapping will use JoinTable.
     * ManyToOne (or OneToOne) no need JoinTable even not specify JoinColumn.
     * OneToMany/ManyToOne mapping without "mappedBy", it is two unidirectional mapping
     * and JoinTable will be automatically used for mapping..
     * ManyToMany always us JoinTable mapping relation
     * 
     * Bidirectional: always specify "mappedBy"
     * OneToMany/ManyToOne should always have SQL foreign key existing
     * no matter specify JoinColumn or not. Foreign keys used in mapping. 
     * ManyToMany always use JoinTable mapping relation
     * OneToOne no need JoinTable if use SQL foreign key mapping (JoinColumn)
     * OneToOne still use JoinTable if not foreign key (JoinColumn) or @MapsId
     * 
     * 
     * parent/child is SQL relation meaning
     * Owning/no-owning side is Entity relation mapping meaning
     * target/source is other or myself meaning
     * 
     * Entity name may be different from table Column name, better use same.
     * Name is source (owning side, child side) Offers entity field (foreign key)
     * referencedColumnName is referring target(no-owning side, parent side) Quotes table column
     * 
     * Error: Repeated column in mapping for entity: Offers column: owner 
     * (should be mapped with insert="false" update="false").
     * 
     * @MapsId
     * If offers table primary key (as foreign key) mapping to primary key of quotes.
     * That will be much simple, no need to first create, then set owner and request_no
     * to offers, final persist offers. We can create quotes and offers, set relation
     * to offers, persist it at same time.
     * 
     * Foreign key mapping partial primary key is not allowed, for example, joinCoulum 
     * only request_no is not allowed..
     */
	@JoinColumns(  { @JoinColumn(name = "owner", referencedColumnName = "owner",insertable=false,  updatable=false),    
			@JoinColumn(name = "request_no", referencedColumnName = "request_no",insertable=false,  updatable=false)})  
    /** 
     * [org.springframework.http.converter.HttpMessageNotWritableException: Could not write JSON: Infinite recursion (StackOverflowError); 
     * nested exception is com.fasterxml.jackson.databind.JsonMappingException: Infinite recursion (StackOverflowError) 
     * (through reference chain: com.springbootjpaangular2.domain.Offers["quotes"]->com.springbootjpaangular2.domain.Quotes["offers"]->
     * org.hibernate.collection.internal.PersistentBag[0]->com.springbootjpaangular2.domain.Offers["quotes"]->
     * com.springbootjpaangular2.domain.Quotes["offers"]->org.hibernate.collection.internal.PersistentBag[0]->
     * com.springbootjpaangular2.domain.Offers["quotes"]->com.springbootjpaangular2.domain.Quotes["offers"]-> .........
     * 
     * To avoid infinite loading: 
     * https://www.appsdeveloperblog.com/infinite-recursion-in-objects-with-bidirectional-relationships/
     */
    @JsonIgnore
	private Quotes quotes; 
    
   
    public Quotes getQuotes() {
        return quotes;
    }
    
    public void setQuotes(Quotes quotes) {
        this.quotes = quotes;
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
        this.offer_no =offerno;
    }
    
    public Integer getRequest_no() {
        return request_no;
    }

    public void setRequest_no (Integer requestno) {
        this.request_no = requestno;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    public Date getOffer_date() {
        return offer_date;
    }

    public void setOffer_date(Date offer_date) {
        this.offer_date = offer_date;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
    
    public String getDelivery_terms() {
        return delivery_terms;
    }

    public void setDelivery_terms(String delivery_terms) {
        this.delivery_terms = delivery_terms;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
  
    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name){
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
 
    public String getOrigin_cntry() {
        return origin_cntry;
    }

    public void setOrigin_cntry(String origin_cntry) {
        this.origin_cntry = origin_cntry;
    }

    public String getLading_point() {
        return lading_point;
    }

    public void setLading_point(String lading_point) {
        this.lading_point = lading_point;
    }
    
   
    public Date getFirst_ship_date() {
        return first_ship_date;
    }

    public void setFirst_ship_date(Date first_ship_date) {
        this.first_ship_date = first_ship_date;
    }

    
    public String getFinal_dest() {
        return final_dest;
    }

    public void setFinal_dest(String final_dest) {
        this.final_dest = final_dest;
    }

    
    public Date getLast_ship_date() {
        return last_ship_date;
    }

    public void setLast_ship_date(Date last_ship_date) {
        this.last_ship_date = last_ship_date;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offers )) return false;
        return offer_no != null && offer_no.equals(((Offers) o).getOffer_no()) && 
        	   owner != null && owner.equals(((Offers) o).getOwner()) &&
        	   request_no != null && request_no.equals(((Offers) o).getRequest_no()) ;
    }
    
  
    @Override
    public int hashCode () {
    	final int prime = 98;
        int result = 1;
        result = prime * result+ ((offer_no == null) ? 0 : offer_no.hashCode());
        result = prime * result+ ((owner == null) ? 0 : owner.hashCode());
        result = prime * result+ ((request_no == null) ? 0 : request_no.hashCode());

    	return result;
    }
    
    public static void setAllFieldValue  (Offers source, Offers target) throws Exception { 	
    	Method[] ms = (target.getClass().getDeclaredMethods());
    	List<Method> msListget= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("get")).collect(Collectors.toList());
    	
    	List<Method> msListset= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("set")).collect(Collectors.toList());
    	// cannot cast object[] to Method[]
    	ms = msListget.toArray(new Method[msListget.size()]);

    	for (Method getMd: ms) {
    		if (getMd.getName().equals("getOffer_no") || getMd.getName().equals("getRequest_no") ||
    				getMd.getName().equals("getOwner") || getMd.getName().equals("getQuotes"))
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
