package br.com.zup.desafioproposta.config.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;

public class IsFutureOffsetDateTimeValidator implements ConstraintValidator<IsFutureOffsetDateTime, String> {

    OffsetDateTime dateTime;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            dateTime = OffsetDateTime.parse(value);

        }
        catch (Exception e) {
            return false;
        }

        return dateTime.isAfter(OffsetDateTime.now());
    }
}
