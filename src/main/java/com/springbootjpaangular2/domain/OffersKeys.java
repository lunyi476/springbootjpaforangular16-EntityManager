package com.springbootjpaangular2.domain;

import java.io.Serializable;

public final class OffersKeys implements Serializable {
    /**
	 * Generated UID
	 */
	private static final long serialVersionUID = -3997033011538284166L;
    
	public Integer offer_no;
    public String owner;
    public Integer request_no;

    public OffersKeys() {}

    public OffersKeys(Integer offer_no, String owner, Integer request_no) {
        this.request_no = request_no;
        this.owner = owner;
        this.offer_no = offer_no;
    }

    public Integer getOffer_no () {
    	return offer_no;
    }
    
    public Integer setOffer_no (Integer offer_no) {
    	return this.offer_no = offer_no;
    }
  
    public Integer getRequest_no () {
    	return request_no;
    }
    
    public Integer setRequest_no (Integer request_no) {
    	return this.request_no = request_no;
    }

   public String getOwner () {
    	return owner;
    }
    
   public void setOwner (String owner) {
    	this.owner= owner;
    }
   
    public boolean equals(Object otherOb) {
        if (this == otherOb) {
            return true;
        }
        if (!(otherOb instanceof OffersKeys)) {
            return false;
        }
        OffersKeys other = (OffersKeys) otherOb;
        return ((offer_no==null?other.offer_no==null:offer_no.equals(other.offer_no))
                 && (owner == other.owner));
    }

    public int hashCode() {
        return ((offer_no==null?0:offer_no.hashCode()) ^ (owner.hashCode()));
    }

    public String toString() {
        return "" + offer_no + "-" + owner;
    }
}