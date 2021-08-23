package br.com.zup.desafioproposta.config.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum toValidate) {
        acceptedValues = Stream.of(toValidate.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value != null) {

            String inputString = value.toString();
            String inputAparado = inputString.trim();
            String inputTratado = inputAparado.toUpperCase();

            return acceptedValues.contains(inputTratado);
        }

        return false;

    }
}
