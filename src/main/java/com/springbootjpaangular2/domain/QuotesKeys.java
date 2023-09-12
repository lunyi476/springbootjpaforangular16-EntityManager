package com.springbootjpaangular2.domain;

import java.io.Serializable;

public final class QuotesKeys implements Serializable {
    /**
	 * Generated UID
	 */
	private static final long serialVersionUID = -3997033011538284166L;
    
	public Integer request_no;
    public String owner;

    public QuotesKeys() {}

    public QuotesKeys(Integer requestNo, String owner) {
        this.request_no = requestNo;
        this.owner = owner;
    }

    public Integer getRequest_no () {
    	return request_no;
    }
    
    public Integer setRequest_no (Integer requestNo) {
    	return this.request_no = requestNo;
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
        if (!(otherOb instanceof QuotesKeys)) {
            return false;
        }
        QuotesKeys other = (QuotesKeys) otherOb;
        return ((request_no==null?other.request_no==null:request_no.equals(other.request_no))
                 && (owner == other.owner));
    }

    public int hashCode() {
        return ((request_no==null?0:request_no.hashCode()) ^ (owner.hashCode()));
    }

    public String toString() {
        return "" + request_no + "-" + owner;
    }
}