package com.liu.learnjava;

import com.liu.learnjava.service.User;
import com.liu.learnjava.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args){
//		创建ioc容器实例，加载配置文件，
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//		获取bean
		 UserService userService = context.getBean(UserService.class);
//		 正常调用login
		User user = userService.login("bob@example.com", "password");
//		调用register
		userService.register("hhhhh@example.com", "password", "hhhhh333");
		System.out.println(user.getName());
	}
}
