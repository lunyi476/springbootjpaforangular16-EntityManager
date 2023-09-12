package com.springbootjpaangular2.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Quote entity/table
 * @author lyi
 *
 */
@Entity(name = "Quotes")
@IdClass(QuotesKeys.class) // composite key with auto-generation
//@DynamicUpdate
public class Quotes {
	//for postgresql,  nextval('REQUESTS_NO_SEQ'); for h2, NEXT VALUE FOR REQUESTS_NO_SEQ;  in insert statement
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "REQUESTS_NO_SEQ")
    @SequenceGenerator(name="REQUESTS_NO_SEQ", sequenceName = "REQUESTS_NO_SEQ") 
    @Id
    private Integer request_no;
    
    @Id
    private String owner ="BAMBOOROSE";
  
    @NotNull //  for primary type validation
    @Column(nullable = false) // database schema
    private String item;
    private String description;
    private String requested_by;
    private String category;
    private String item_class;
    private String division;
    private String brand;  
    private String dept;
    private String buyer;
    private String hts_no;
    private BigDecimal request_price;
    private String currency;
    private String delivery_terms;
    private String import_cntry;
    private BigDecimal allocated_qty;
    //@Temporal( TemporalType.TIMESTAMP )
	//@CreationTimestamp
    //@RevisionEntity( CustomTrackingRevisionListener.class )
    //@Audited
    private Timestamp modify_ts;
    private String modify_user;
    
    public Quotes () {};
    /**  
     *  https://medium.com/@rajibrath20/the-best-way-to-map-a-onetomany-relationship-with-jpa-and-hibernate-dbbf6dba00d3
     *  
     *  The main difference between a OneToMany and a ManyToMany relationship in JPA is 
     *  that a ManyToMany always makes use of a intermediate relational join table to 
     *  store the relationship, where as a OneToMany can either use 
     *  (1) a join table, 
     *  (2) or a foreign key in target object's table referencing the source object table's primary key.
     *  
     *  (2) is preferred way.
	 *   
	 *  Better use Set (if no duplicated object allowed) instead of List:
	 *  https://lkumarjain.blogspot.com/2013/07/why-hibernate-does-delete-all-entries.html
     */
    /** bidirectional, target/source is referring to other/self**/
    @OneToMany(targetEntity=Offers.class, mappedBy = "quotes", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) 
   	private List<Offers> offers = new ArrayList<Offers>();
       
    /**
     * The bidirectional associations should always be updated on both sides, 
     * therefore the Parent side should contain the addChild and removeChild
     */
    public void addOffer(Offers offer) {
        this.offers.add(offer);  
        offer.setOwner(this.getOwner());
        offer.setRequest_no(this.getRequest_no());
        offer.setQuotes(this);
    }
    
    public void addAllOffer ( List<Offers> offers) throws Exception { 	
    	for ( Offers o : offers) {
    	    boolean find = false;
			for (Offers p: this.offers) {
				if (o.getOffer_no()!= null && o.getOffer_no().equals(p.getOffer_no())) {
				  Offers.setAllFieldValue(o, p);
				  find = true;
				  break;
				}
			}
			
			if (!find) { // add new			
				this.addOffer(o);
			}
    	}
    }
    
    public void removeOffer(Offers offer) {
    	offers.removeIf(i -> offer.equals(i));
    	offer.setQuotes(null); 
    }
    
    public void removeAllOffers() {
    	 offers.removeIf(i -> i != null );
    	 
    	 for ( Offers o : offers) {
       		  o.setQuotes(null);
       	 } 
      }
   
    public List<Offers> getOffers() {
    	return offers;
    }
    
	/**
	 *  HibernateException - A collection with cascade="all-delete-orphan" 
	 *  was no longer referenced by the owning entity instance
		
		What is happening is that Hibernate requires complete ownership 
		of the children collection in the parent object. 
		If you set it to a new object, Hibernate is unable to track changes 
		to that collection and thus has no idea how to apply the cascading 
		persistence to your objects.
		
		So the code like:		
		this.children = someNewSetOfChildren;
		or like:	
		parent.setChildren(someNewSetOfChildren);
		results in a HibernateException.
		
		To avoid this problem, any time we want to add or delete something 
		to the list, we have to modify the contents of the collection instead 
		of assigning a new one with code like:

		parent.getChildren().clear();
		parent.getChildren().addAll(someNewSetOfChildren);
	 **/
    public void setOffers(List<Offers> set) {
    	this.offers.clear();
    	this.offers =set;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requestedBy) {
        this.requested_by = requestedBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String cat) {
        this.category = cat;
    }
    
    public String getItem_class() {
        return item_class;
    }

    public void setItem_class(String itemClass) {
        this.item_class = itemClass;
    }
    
    public String getDivision() {
        return division;
    }

    public void setDivision(String divi) {
        this.division = divi;
    }
    
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
    
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    
    public String getHts_no() {
        return hts_no;
    }

    public void setHts_no(String htsNo) {
        this.hts_no = htsNo;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDelivery_terms() {
        return delivery_terms;
    }

    public void setDelivery_terms(String delivery_terms) {
        this.delivery_terms = delivery_terms;
    }
    
    public String getImport_cntry() {
        return import_cntry;
    }

    public void setImport_cntry(String import_cntry) {
        this.import_cntry = import_cntry;
    }
       
    public BigDecimal getAllocated_qty() {
        return allocated_qty;
    }

    public void setAllocated_qty(BigDecimal allocated_qty) {
        this.allocated_qty = allocated_qty;
    }
    
    public BigDecimal getRequest_price() {
        return request_price;
    }

    public void setRequest_price(BigDecimal price) {
        this.request_price = price;
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
        if (!(o instanceof Quotes )) return false;
        return request_no != null && request_no.equals(((Quotes) o).getRequest_no());
    }
    
    @Override
    public int hashCode () {
    	final int prime = 31;
        int result = 1;
        result = prime * result+ ((request_no == null) ? 0 : request_no.hashCode());
        result = prime * result+ ((owner == null) ? 0 : owner.hashCode());

    	return result;
    }
    
    /** 
     * Reflection to set Bean values
     * 
     * @param source
     * @param target
     * @throws Exception
     */
    public static void setAllFieldValue  (Quotes source, Quotes target) throws Exception { 
    	Method[] ms = (target.getClass().getDeclaredMethods());
    	List<Method> msListget= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("get")).collect(Collectors.toList());
    	
    	List<Method> msListset= Arrays.stream(ms).filter(
    			m -> m.getName().startsWith("set")).collect(Collectors.toList());
    	// cannot cast object[] to Method[]
    	ms = msListget.toArray(new Method[msListget.size()]);

    	for (Method getMd: ms) {
    		if (getMd.getName().equals("getRequest_no"))
    			continue;
    		
    		if (getMd.getName().equals("getOffers")) {
    			// any target's offers were already removed
    			if (source.getOffers().size() > 0)
    				target.addAllOffer(source.getOffers());
    			continue;
    		}
    		
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
