package br.com.zup.desafioproposta.config.validation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@CPF @CNPJ // validações que farão parte da composição
@ConstraintComposition(CompositionType.OR) // CPF ou CNPJ
@ReportAsSingleViolation // ignora os erros das anotações originais e os sobreescreve com o erro deste arquivo
@Documented
@Constraint(validatedBy = {}) // já é validado pelos Validators das anotações originais
@Target({ElementType.FIELD}) // aplicável em atributos
@Retention(RetentionPolicy.RUNTIME) // pode ser lida em runtime na aplicação
public @interface CpfOuCnpj {

    // Mensagem padrão aplicada quando a validação falhar
    String message() default "CPF/CNPJ inválido. Insira um valor válido";

    // Validação para validation groups. Pouco utilizada.
    Class<?>[] groups() default {};

    // Possibilidade de envio de informações extras. Pouco utilizada.
    Class<? extends Payload>[] payload() default{};

}
