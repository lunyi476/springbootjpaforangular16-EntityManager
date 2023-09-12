package com.springbootjpaangular2.domain;

import java.beans.PropertyEditorSupport;
import org.apache.commons.lang3.StringUtils;

public class QuotesEditor extends PropertyEditorSupport {
	
	public QuotesEditor () {};
	// Testing only
	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		if (!StringUtils.isNoneBlank(text)) {
            setValue(null);
        } else { // source is QuotesEditor, value is Quotes 
        	// argument of method is Quotes
        	//Quotes o = new Quotes();
        	//o.setRequest_No(Integer.valueOf(text));
            //setValue (o);
        	// argument of method is Integer 
        	setValue (Integer.valueOf(text));
        }
	}	
}
