package br.com.prevent.logmanager.service.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = LogValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LogValid {
	String message() default "Erro de validacao";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}