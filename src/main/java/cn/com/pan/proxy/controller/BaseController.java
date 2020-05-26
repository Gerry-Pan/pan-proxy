package cn.com.pan.proxy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

public abstract class BaseController {

	protected final Logger log = LogManager.getLogger(getClass());

	protected final Mono<JSONObject> onErrorResume(Throwable e) {
		log.error(e.getMessage(), e);

		JSONObject result = new JSONObject();
		result.put("code", -1);
		result.put("message", "system error");
		return Mono.just(result);
	}

}
