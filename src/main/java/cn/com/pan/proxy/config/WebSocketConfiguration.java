package cn.com.pan.proxy.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import cn.com.pan.proxy.handler.WebsocketHandlerMapping;
import cn.com.pan.proxy.socket.WebSocketProxyHandler;

@Configuration
public class WebSocketConfiguration {

	protected final Logger log = LogManager.getLogger(getClass());

	@Bean
	public WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	public WebSocketHandler webSocketHandler() {
		return new WebSocketProxyHandler();
	}

	@Bean
	public HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
		Map<String, WebSocketHandler> map = new HashMap<String, WebSocketHandler>();
		map.put("/**", webSocketHandler);

		SimpleUrlHandlerMapping mapping = new WebsocketHandlerMapping();
		mapping.setUrlMap(map);
		mapping.setOrder(0);

		return mapping;
	}

}
