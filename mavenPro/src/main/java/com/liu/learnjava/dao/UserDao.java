package com.liu.learnjava.dao;

import com.liu.learnjava.service.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.util.List;
@Component
@Transactional
public class UserDao extends AbstractDao<User> {
	public User fetchUserByEmail(String email) {
		List<User> users = getJdbcTemplate().query("SELECT * FROM users WHERE email = ?", (ResultSet rs, int rowNum) -> {
			return new User( // new User object:
					rs.getLong("id"), // id
					rs.getString("email"), // email
					rs.getString("password"), // password
					rs.getString("name")); // name
		}, new Object[] { email });
		return users.isEmpty() ? null : users.get(0);
	}
	public List<User> getUsers(int pageIndex){
		int limit= 100;
		int offset = limit*(pageIndex -1);
		return  getJdbcTemplate().query("SELECT * FROM users LIMIT ? OFFSET ?",new BeanPropertyRowMapper<>(User.class),new Object[] {limit,offset});
	}
	public User registerUser(String email, String password,String name){
//		创建一个keyHolder
		KeyHolder holder = new GeneratedKeyHolder();
		if(1!=getJdbcTemplate().update(
//				参数1：preparedStatementCreator
				(conn)->{
//					传教preparedStatement时，必须指定RETURN_GENERATED_KEYS;
					var ps= conn.prepareStatement("INSERT INTO users(email,password,name) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
					ps.setObject(1,email);
					ps.setObject(2,password);
					ps.setObject(3,name);
					return ps;
				},
//				参数2：KeyHolder
				holder
		)){
			throw new RuntimeException("Insert failed");
		}
		if("root".equalsIgnoreCase(name)){
			throw new RuntimeException("Invalid name, will rollback....");
		}
//		从KeyHolder中获取返回的自增值
		return  new User(holder.getKey().longValue(),email,password,name);
	}
	public User createUser(String email, String password, String name) {
		KeyHolder holder = new GeneratedKeyHolder();
		if (1 != getJdbcTemplate().update((conn) -> {
			var ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setObject(1, email);
			ps.setObject(2, password);
			ps.setObject(3, name);
			return ps;
		}, holder)) {
			throw new RuntimeException("Insert failed.");
		}
		if ("root".equalsIgnoreCase(name)) {
			throw new RuntimeException("Invalid name, will rollback...");
		}
		return new User(holder.getKey().longValue(), email, password, name);
	}
}
