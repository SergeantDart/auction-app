package com.dumitrascuantonio.auctionapp.annotation;

import com.dumitrascuantonio.auctionapp.validator.MatchingPasswordValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingPasswordValidator.class)
public @interface MatchingPassword {

    String message() default "Passwords do not match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
