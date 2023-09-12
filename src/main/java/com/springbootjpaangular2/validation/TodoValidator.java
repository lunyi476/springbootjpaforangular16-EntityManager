package com.springbootjpaangular2.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Service;

import com.springbootjpaangular2.domain.Offers;
import com.springbootjpaangular2.domain.Orders;
import com.springbootjpaangular2.domain.Quotes;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To put custom validation 
 * 
 * @author lyi
 * 08/2020
 */
@Service("todoValidator")
public class TodoValidator implements Validator {
	private static Logger logger = LoggerFactory.getLogger(TodoValidator.class);
	
	/** 
	 * Object in session will be always in Models 
	 */
	@Override
	public boolean supports(Class<?> paramClass) { 
		// return Orders.class.equals(paramClass);
		return true; // to avoid throw error if unknown object in Model
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// client side validated
		if (obj instanceof Orders )  { 
			Orders emp = (Orders) obj;			
			if(StringUtils.isBlank(emp.getBuyer()) && emp.getOffer_no() == null) {		
				errors.rejectValue("offer_no", "orders.missing.value"); 	
				errors.reject("orders.missing.value", new Object[]{"'offer_no'", "'buyer'"}, "");
			}
			logger.info("validate error counts :  " + errors.getFieldErrorCount());
		}
		else if (obj instanceof Quotes   ) { 
			System.out.println(obj.toString());
		}
		else if (obj instanceof Offers   ) { 
			System.out.println(obj.toString());
		}
	}
}
