package cn.com.pan.proxy.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

@Service
public class HttpProxyService extends GlobalService {

	public Mono<ResponseEntity<byte[]>> dispatch(final byte[] requestBody, ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders requestHeaders = request.getHeaders();

		URI absolute = request.getURI();
		String q = absolute.getRawQuery();
		String uri = absolute.getRawPath();

		String path = uri + (!StringUtils.hasText(q) ? "" : ("?" + q));

		String url = "http://ip:port" + path;// 转发规则自定义

		return webClient.method(request.getMethod()).uri(url, new JSONObject())
				.headers(headers -> headers.addAll(requestHeaders))
				.body(Mono.just(requestBody == null ? new byte[0] : requestBody), byte[].class).exchange()
				.flatMap(clientResponse -> clientResponse.toEntity(byte[].class));
	}

}
