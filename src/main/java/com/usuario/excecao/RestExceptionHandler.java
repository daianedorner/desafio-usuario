package com.usuario.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErroSistema> handlerMethodThrowable(Throwable exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(exception.getMessage()));
	}

	private ErroSistema buildError(String message) {
		return new ErroSistema(message);
	}

}
