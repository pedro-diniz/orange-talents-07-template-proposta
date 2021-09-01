package br.com.zup.desafioproposta.config.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;

public class IsFutureOffsetDateTimeValidator implements ConstraintValidator<IsFutureOffsetDateTime, String> {

    OffsetDateTime dateTime;

    private final Logger logger = LoggerFactory.getLogger(IsFutureOffsetDateTimeValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            dateTime = OffsetDateTime.parse(value);
        }
        catch (Exception e) {
            logger.warn("Input incompatível com formato OffsetDateTime");
            return false;
        }
        logger.info("Input compatível com formato OffsetDateTime");

        if (dateTime.isAfter(OffsetDateTime.now())) {
            logger.info("Input é data futura");
            return true;
        }
        else {
            logger.warn("Input é data passada/presente");
            return false;
        }
    }
}
