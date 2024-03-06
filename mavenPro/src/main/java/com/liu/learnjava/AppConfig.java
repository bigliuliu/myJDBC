package com.liu.learnjava;

import com.liu.learnjava.service.User;
import com.liu.learnjava.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//类启动器
@Configuration
@ComponentScan
public class AppConfig {
	public static void main(String[] args){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		userService.register("hhhhh@example.com", "password", "hhhhh333");
		System.out.println(user.getName());
	}
}
