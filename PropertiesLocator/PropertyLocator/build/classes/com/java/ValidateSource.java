package com.java;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateSource.Validate.class)
public @interface ValidateSource {
	
	String message() default "{error.invalidSource}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	public class Validate implements ConstraintValidator <ValidateSource, String> {

		@Inject
		private Message message;
		
	    @Override
	    public void initialize(ValidateSource validSource) {
	    }
	    
	    @Override
	    public boolean isValid(String source, ConstraintValidatorContext constraintAnnotation) {
	    	return message.doesExist(source);
	    }
	 
	}

}
	