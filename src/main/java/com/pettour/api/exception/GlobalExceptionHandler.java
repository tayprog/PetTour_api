package com.pettour.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ... (o método handleValidationExceptions continua igual) ...

    // MÉTODO PARA TRATAR CONFLITO DE AGENDAMENTO ---
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // Verifica se a mensagem é a nossa mensagem de conflito
        if (ex.getMessage().contains("Conflito de agendamento")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
        // Para outras RuntimeExceptions, mantém o comportamento padrão
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
    }
}