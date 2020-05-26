package cn.com.pan.proxy.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class GlobalService {

	protected final Logger log = LogManager.getLogger(getClass());

	protected static final Runtime runTime = Runtime.getRuntime();

	@Autowired
	protected WebClient webClient;

}
