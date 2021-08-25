package br.com.zup.desafioproposta.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueDocumentValidator.class) //
@Target({ElementType.FIELD}) // aplicável em atributos
@Retention(RetentionPolicy.RUNTIME) // pode ser lida em runtime na aplicação
public @interface UniqueDocument {

    // Mensagem padrão aplicada quando a validação falhar
    String message() default "já está em uso";

    // Validação para validation groups. Pouco utilizada.
    Class<?>[] groups() default {};

    // Possibilidade de envio de informações extras. Pouco utilizada.
    Class<? extends Payload>[] payload() default{};

}
