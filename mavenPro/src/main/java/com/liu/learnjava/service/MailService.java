package com.liu.learnjava.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

@Component
public class MailService {
	@Value("#{smtpConfig.host}")
	private String smtpHost;
	@Value("#{smtpConfig.port}")
	private int smtpPort;
	@Autowired
//	ZoneId zoneId = ZoneId.systemDefault();
	private ZoneId zoneId;
	@PostConstruct
	public void init() {
		System.out.println("init mail service with zoneId" + this.zoneId);
	}
	@PreDestroy
	public void shutdown(){
		System.out.println("Shut down mail service");

	}
//	private ZoneId zoneId = ZoneId.systemDefault();
//
//	public void setZoneId(ZoneId zoneId) {
//		this.zoneId = zoneId;
//	}

	public String getTime() {
		return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	public void sendLoginMail(User user) {
		System.err.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
	}

	public void sendRegistrationMail(User user) {
		System.err.println(String.format("Welcome, %s!", user.getName()));

	}
	public void sendWelcomeMail(User user){
		System.out.println("sent by smtp host :"+smtpHost);
		System.out.println("sent by smtp port"+smtpPort);
	}
}
