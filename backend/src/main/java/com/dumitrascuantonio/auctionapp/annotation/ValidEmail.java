package com.dumitrascuantonio.auctionapp.annotation;

import com.dumitrascuantonio.auctionapp.validator.EmailValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "Invalid email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
