package br.com.zup.desafioproposta.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class) //
@Target({ElementType.FIELD}) // aplicável em atributos
@Retention(RetentionPolicy.RUNTIME) // pode ser lida em runtime na aplicação
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "não corresponde a uma opção válida.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
