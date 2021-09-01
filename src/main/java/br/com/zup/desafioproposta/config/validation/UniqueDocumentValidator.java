package br.com.zup.desafioproposta.config.validation;

import br.com.zup.desafioproposta.config.exception.EntidadeImprocessavelException;
import br.com.zup.desafioproposta.config.security.data.JasyptConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueDocumentValidator implements ConstraintValidator<UniqueDocument, String> {

    private final Logger logger = LoggerFactory.getLogger(UniqueDocumentValidator.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isValid(String documento, ConstraintValidatorContext context) {
        // query para buscar a proposta referente ao documento
        Query query = entityManager.createQuery("select 1 from Proposta where documentoHash = :doc");

        // configuração do parâmetro da query
        query.setParameter("doc", new JasyptConfig().gerarHash(documento));

        // criação de uma lista com os resultados da query
        List<?> list = query.getResultList();

        // mensagem de exceção para a condição de a lista ter mais de um registro
        Assert.state(list.size() <= 1, "Foi encontrado mais de um registro para o documento = :value");

        if (list.isEmpty()) {
            logger.info("Documento válido");
            return true;
        }
        else {
            logger.warn("Documento já cadastrado");
            throw new EntidadeImprocessavelException("Já existe uma proposta para este documento");
        }
    }

}
