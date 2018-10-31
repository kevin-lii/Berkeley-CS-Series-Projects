package com.java;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Autowired;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateSource.Validate.class)
public @interface ValidateSource {
	
	String message() default "{error.invalidSource}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	public class Validate implements ConstraintValidator <ValidateSource, String> {
		
		@Autowired
		private Message message;
		
	    public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		@Override
	    public void initialize(ValidateSource validSource) {
	    }
	    
	    @Override
	    public boolean isValid(String source, ConstraintValidatorContext constraintAnnotation) {
	    	return message.doesExist(source);
	    }
	 
	}

}
	