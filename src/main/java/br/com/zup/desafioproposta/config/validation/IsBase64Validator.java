package br.com.zup.desafioproposta.config.validation;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsBase64Validator implements ConstraintValidator<IsBase64, String> {

    private final Logger logger = LoggerFactory.getLogger(IsBase64Validator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Base64.isBase64(value)) {
            logger.info("Input compatível com Base64");
            return true;
        }
        else {
            logger.warn("Input incompatível com Base64");
            return false;
        }
    }
}
