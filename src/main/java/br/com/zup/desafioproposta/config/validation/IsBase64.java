package br.com.zup.desafioproposta.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsBase64Validator.class) //
@Target({ElementType.FIELD}) // aplicável em atributos
@Retention(RetentionPolicy.RUNTIME) // pode ser lida em runtime na aplicação
public @interface IsBase64 {

    // Mensagem padrão aplicada quando a validação falhar
    String message() default "atributo não está codificado em base64";

    // Validação para validation groups. Pouco utilizada.
    Class<?>[] groups() default {};

    // Possibilidade de envio de informações extras. Pouco utilizada.
    Class<? extends Payload>[] payload() default{};

}
