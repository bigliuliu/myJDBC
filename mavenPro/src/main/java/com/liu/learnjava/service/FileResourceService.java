package com.liu.learnjava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class FileResourceService {
	@Value("1")
	private int version;
	@Value("classpath:/logo.txt")
	private Resource resource;
	private String logo;
	@PostConstruct
	public void init() throws IOException{
		try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
			this.logo = reader.readLine();
		}
	}
	public void printLogo(){
		System.out.println(logo);
		System.out.println("注入基本类型测试：\n"+version);
	}
}
