package cn.com.pan.proxy.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

public class WebsocketHandlerMapping extends SimpleUrlHandlerMapping {

	@Nullable
	protected Object lookupHandler(PathContainer lookupPath, ServerWebExchange exchange) throws Exception {
		boolean f = false;
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders requestHeaders = request.getHeaders();

		String connection = requestHeaders.getFirst("Connection");
		String upgrade = requestHeaders.getFirst("Upgrade");

		if ("Upgrade".equals(connection) && "websocket".equals(upgrade)) {
			f = true;
		}

		if (!f) {
			return null;
		}

		List<PathPattern> matches = this.getHandlerMap().keySet().stream().filter(key -> key.matches(lookupPath))
				.collect(Collectors.toList());

		if (matches.isEmpty()) {
			return null;
		}

		if (matches.size() > 1) {
			matches.sort(PathPattern.SPECIFICITY_COMPARATOR);
			if (logger.isTraceEnabled()) {
				logger.debug(exchange.getLogPrefix() + "Matching patterns " + matches);
			}
		}

		PathPattern pattern = matches.get(0);
		PathContainer pathWithinMapping = pattern.extractPathWithinPattern(lookupPath);
		return handleMatch(this.getHandlerMap().get(pattern), pattern, pathWithinMapping, exchange);
	}

	private Object handleMatch(Object handler, PathPattern bestMatch, PathContainer pathWithinMapping,
			ServerWebExchange exchange) {

		// Bean name or resolved handler?
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = obtainApplicationContext().getBean(handlerName);
		}

		validateHandler(handler, exchange);

		exchange.getAttributes().put(BEST_MATCHING_HANDLER_ATTRIBUTE, handler);
		exchange.getAttributes().put(BEST_MATCHING_PATTERN_ATTRIBUTE, bestMatch);
		exchange.getAttributes().put(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);

		return handler;
	}

}
