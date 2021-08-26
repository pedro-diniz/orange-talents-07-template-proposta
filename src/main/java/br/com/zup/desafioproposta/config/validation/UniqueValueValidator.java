package br.com.zup.desafioproposta.config.validation;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

// UniqueValue é a Annotation com quem vamos trabalhar
// Object é o tipo do parâmetro que esperamos receber
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    private String domainAttribute;
    private Class<?> klass;

    // não conseguimos utilizar Repository aqui, pois a injeção seria dinâmica
        // acabaríamos precisando passar o Repository por parâmetro também, e
        // queremos simplificar o código
    @PersistenceContext
    private EntityManager entityManager;


    @Override // instancia o objeto de validação
    public void initialize(UniqueValue toValidate) {
        domainAttribute = toValidate.fieldName();
        klass = toValidate.domainClass();
    }

    @Override // faz uma query a partir dos parâmetros recebidos
    public boolean isValid(Object value, ConstraintValidatorContext validatorContext) {

        // query para buscar o domainAttribute na entidade klass.
        Query query = entityManager.createQuery("select 1 from " + klass.getName() +
                " where " + domainAttribute + " = :value ");

        // configuração do parâmetro da query
        query.setParameter("value", value);

        // criação de uma lista com os resultados da query
        List<?> list = query.getResultList();

        // mensagem de exceção para a condição de a lista ter mais de um registro
        Assert.state(list.size() <= 1, "Foi encontrado mais de um " + klass.getName() +
                " com o atributo " + domainAttribute + " = :value");

        // retorno do booleano se a lista está vazia ou não, validando a requisição
        return list.isEmpty();
    }

}
