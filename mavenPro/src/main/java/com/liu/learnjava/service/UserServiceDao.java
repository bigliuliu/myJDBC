package com.liu.learnjava.service;

import com.liu.learnjava.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserServiceDao {
	@Autowired
	UserDao userDao;
	public User fetchUserByEmail(String email){
		return  userDao.fetchUserByEmail(email);
	}
	public List<User> getUsers(int pageIndex){
		return userDao.getUsers(pageIndex);
	}
	public User registerUser(String email,String password, String name){
		if(userDao.fetchUserByEmail(email)!=null){
			throw new RuntimeException("Email is already exist");
		}
		return userDao.createUser(email,password,name);
	}
}
