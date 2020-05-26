package cn.com.pan.proxy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class ThrowableControllerAdvice extends BaseController {

	protected final Logger log = LogManager.getLogger(getClass());

	@ExceptionHandler({ Throwable.class })
	public Mono<Void> process(Throwable e, ServerWebExchange exchange) {
		log.error(e.getMessage(), e);

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		return Mono.empty();
	}

}
