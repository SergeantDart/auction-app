package com.dumitrascuantonio.auctionapp.validator;

import com.dumitrascuantonio.auctionapp.annotation.MatchingPassword;
import com.dumitrascuantonio.auctionapp.dto.SignUpRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        SignUpRequest signUpRequest = (SignUpRequest) obj;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }

}
