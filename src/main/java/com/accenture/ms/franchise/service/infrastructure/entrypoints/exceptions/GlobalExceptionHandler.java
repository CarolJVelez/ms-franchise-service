package com.accenture.ms.franchise.service.infrastructure.entrypoints.exceptions;

import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnicalException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnologyClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex) {

        var message = ex.getTechnicalMessage();
        HttpStatus status = HttpStatus.resolve(Integer.parseInt(message.getCode()));
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        var body = Map.of(
                "code", message.getCode(),
                "param", message.getParam(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<Object> handleTechnical(TechnicalException ex) {

        var body = Map.of(
                "code", ex.getTechnicalMessage().getCode(),
                "message", ex.getTechnicalMessage().getMessage()
        );

        return ResponseEntity.status(500).body(body);
    }

    @ExceptionHandler(TechnologyClientException.class)
    public ResponseEntity<Object> handleTechnologyClient(TechnologyClientException ex) {
        String body = ex.getBody();
        int status = ex.getStatus();

        Map<String, Object> map;
        try {
            map = new ObjectMapper().readValue(body, Map.class);
        } catch (Exception e) {
            map = Map.of(
                    "message", body
            );
        }
        return ResponseEntity.status(status).body(map);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleValidation(WebExchangeBindException ex) {

        var error = ex.getFieldErrors().stream().findFirst();

        String message = error
                .map(e -> e.getDefaultMessage())
                .orElse("Error de validación");

        var body = Map.of(
                "code", "400",
                "message", message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
