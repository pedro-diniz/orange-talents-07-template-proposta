package br.com.zup.desafioproposta.config.validation;

import org.apache.commons.codec.binary.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsBase64Validator implements ConstraintValidator<IsBase64, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Base64.isBase64(value);
    }
}
