package br.com.zup.desafioproposta.config.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

@Override // Retorna HTTP 400 e cobre todas as chamadas no MethodArgumentNotValidException, usado no @Valid
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        // Obtém listas dos erros globais e de campo referentes à exceção.
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Constrói o objeto ValidationErrorOutputDto e o retorna na exceção.
        ValidationErrorOutputDto errors = buildValidationErrors(globalErrors, fieldErrors);

        Problema problema = new Problema(
                status.value(),
                OffsetDateTime.now(),
                "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
                errors
        );

        return handleExceptionInternal(ex, problema, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Problema problema = new Problema(
                status.value(),
                OffsetDateTime.now(),
                ex.getMessage()
        );

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }

    // Constrói o ValidationErrorOutputDto a partir das listas de erros
    public ValidationErrorOutputDto buildValidationErrors(
            List<ObjectError> globalErrors, List<FieldError> fieldErrors) {

        // Instancia o objeto vazio da classe ValidationErrorOutputDto (VEOD)
        ValidationErrorOutputDto validationErrors = new ValidationErrorOutputDto();

        // Adiciona os erros globais à lista de erros globais do objeto VEOD.
        globalErrors.forEach(error -> validationErrors.addError(getErrorMessage(error)));

        // Adiciona os erros de campo à lista de erros de campo do objeto VEOD
        fieldErrors.forEach(error -> {
            // Obtém os fieldErrors
            String errorMessage = getErrorMessage(error);

            // Cria os objetos da classe FieldErrorOutputDto e os adiciona na lista de erros de campo
            // do VEOD
            validationErrors.addFieldError(error.getField(), errorMessage);
        });
        return validationErrors;
    }

    // Obtém as mensagens de erro a partir da entidade MessageSource
    public String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }

}
