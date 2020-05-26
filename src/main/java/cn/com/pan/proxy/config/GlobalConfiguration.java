package cn.com.pan.proxy.config;

import java.util.List;

import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientCodecCustomizer;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class GlobalConfiguration {

	@Bean
	public WebClient webClient(WebClient.Builder webClientBuilder) {
		return webClientBuilder.build();
	}

	@Bean
	@Order(-1)
	@Primary
	public WebClientCodecCustomizer exchangeStrategiesCustomizer(List<CodecCustomizer> codecCustomizers) {
		return new WebClientCodecCustomizer(codecCustomizers);
	}

	@Bean
	@Order(1)
	@Primary
	public CodecCustomizer ecloudJacksonCodecCustomizer(ObjectMapper objectMapper) {
		return (configurer) -> {
			CodecConfigurer.DefaultCodecs defaults = configurer.defaultCodecs();
			defaults.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.ALL));
			defaults.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.ALL));
		};
	}

}
