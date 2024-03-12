package com.liu.learnjava.mapperService;

import com.liu.learnjava.entity.User;
import com.liu.learnjava.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserServiceMap {
	//	注入usermapper
	@Autowired
	UserMapper userMapper;

	public User getUserById(long id) {
//		调用mapper的方法
		User user = userMapper.getById(id);
		if (user == null) {
			throw new RuntimeException("user not found by id");
		}
		return user;
	}

	public User fetchUserByEmail(String email) {
		return userMapper.getByEmail(email);
	}

	public User getUserByEmail(String email) {
		User user = fetchUserByEmail(email);
		if (user == null){
			throw  new RuntimeException("user not found by email");
		}
		return user;
	}
	public List<User> getUsers(int pageIndex){
		int pageSize = 100;
		return userMapper.getAll((pageIndex -1)*pageSize,pageSize);
	}
	public User login(String email,String password){
		User user = userMapper.getByEmail(email);
		if(user != null && password.equals(user.getPassword())){
			return user;
		}
		throw new RuntimeException("login failed");
	}
	public User register(String email, String password,String name){
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		userMapper.insert(user);
		return user;
	}
	public void updateUser(Long id, String name){
		User user = getUserById(id);
		user.setName(name);
		user.setCreatedAt(System.currentTimeMillis());
		userMapper.update(user);
	}
	public void deleteUser(Long id){
		userMapper.deleteById(id);
	}
}
