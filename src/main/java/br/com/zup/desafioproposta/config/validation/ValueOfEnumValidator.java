package br.com.zup.desafioproposta.config.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {

    private List<String> acceptedValues;

    private final Logger logger = LoggerFactory.getLogger(ValueOfEnumValidator.class);

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

            logger.info("Input compatível com opções do Enum");
            return acceptedValues.contains(inputTratado);
        }

        logger.warn("Input incompatível com opções do Enum");
        return false;

    }
}
