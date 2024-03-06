package com.liu.learnjava.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//这是一个javabean持有所有的配置
@Component
public class SmtpConfig {
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Value("${smtp.host}")
	private String host;
	@Value("${smtp.port:25}")
	private int port;

}
