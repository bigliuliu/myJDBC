package com.liu.learnjava.service;

import com.liu.learnjava.validator.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.time.*;

@Component
public class UserService {
	@Autowired
	MailService mailService;
	@Autowired
	Validators validators;
	public UserService(@Autowired MailService mailService) {
		this.mailService = mailService;
	}
	private List<User> users = new ArrayList<>(List.of( // users:
			new User(1, "bob@example.com", "password", "Bob"), // bob
			new User(2, "alice@example.com", "password", "Alice"), // alice
			new User(3, "tom@example.com", "password", "Tom"))); // tom

	public User login(String email, String password) {
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
//				mailService.sendLoginMail(user);
				mailService.sendWelcomeMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}

	public User getUser(long id) {
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
	}

	public User register(String email, String password, String name) {
		validators.validate(email,password,name);
		users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong() + 1, email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;
	}
}
