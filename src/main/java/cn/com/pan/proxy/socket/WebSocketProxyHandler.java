package cn.com.pan.proxy.socket;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Mono;

public class WebSocketProxyHandler implements WebSocketHandler {

	protected final Logger log = LogManager.getLogger(getClass());

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		log.info("Handshake---" + session);

		URI absolute = session.getHandshakeInfo().getUri();

		String q = absolute.getRawQuery();
		String uri = absolute.getRawPath();

		String path = uri + (!StringUtils.hasText(q) ? "" : ("?" + q));

		String url = "http://ip:port" + path;// 转发规则自定义

		WebSocketClient client = new ReactorNettyWebSocketClient();

		return client.execute(URI.create(url), clinetSession -> {
			Mono<Void> mono1 = session
					.send(clinetSession.receive().map(message -> this.generateMessage(session, message)));

			Mono<Void> mono2 = clinetSession
					.send(session.receive().map(message -> this.generateMessage(clinetSession, message)));

			return Mono.zip(mono1, mono2).then();
		});
	}

	protected WebSocketMessage generateMessage(WebSocketSession session, WebSocketMessage message) {
		DataBuffer payload = message.getPayload();

		byte[] bytes = new byte[payload.readableByteCount()];
		payload.read(bytes);

		DataBuffer buffer = session.bufferFactory().wrap(bytes);

		return new WebSocketMessage(message.getType(), buffer);
	}

}
