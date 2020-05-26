package cn.com.pan.proxy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import cn.com.pan.proxy.service.HttpProxyService;
import reactor.core.publisher.Mono;

@RestController
public class HttpProxyController extends BaseController {

	@Autowired
	private HttpProxyService httpProxyService;

	@RequestMapping(path = "/**")
	public Mono<ResponseEntity<byte[]>> dispatch(@RequestBody(required = false) byte[] requestBody,
			ServerWebExchange exchange) {
		return httpProxyService.dispatch(requestBody, exchange).onErrorResume(e -> {
			log.error(e.getMessage(), e);

			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			return Mono.empty();
		});
	}

}
