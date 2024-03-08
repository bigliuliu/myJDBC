package com.liu.learnjava.service;

import com.liu.learnjava.metrics.MetricTime;
import com.liu.learnjava.validator.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

@Component
@Transactional
public class UserService {
	//	注入数据库
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired(required = false)
	MailService mailService;
	@Autowired(required = false)
	Mail mail;
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
//				if(mailService!= null){
//					mailService.sendWelcomeMail(user);
				if (mail != null) {
					mail.sendMail(user.getEmail(), "Login successfully", "Hi, you have logged in at ");
				} else {
					System.out.println("skip send email ...");
				}
				mailService.sendLoginMail(user);

				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}

	public User getUser(long id) {
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
	}

	//	监控register的性能
	@MetricTime("register")
	public User register(String email, String password, String name) {
		validators.validate(email, password, name);
		users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong() + 1, email, password, name);
		users.add(user);
//		mailService.sendRegistrationMail(user);
		return user;
	}

	//数据库连接测试
	public User getUserById(long id) {
//		注意传入的是ConnectionCallback
		return jdbcTemplate.execute((Connection conn) -> {
//		可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放
//		再内部手动创建的PrepareStatement,Resultset 必须用try(..)释放
			try (var ps = conn.prepareStatement("SELECT * FROM student WHERE id=?")) {
				ps.setObject(1, id);
				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						return new User(
//							new User objec
								rs.getLong("id"),
								rs.getString("email"),
								rs.getString("password"),
								rs.getString("name")
						);
					}
					throw new RuntimeException("user not found by id");
				}
			}
		});
	}

	//根据userName查询
	public User getUserByName(String name) {

		return jdbcTemplate.execute("SELECT * FROM student WHERE name=?", (PreparedStatement ps) -> {
//			preparedStatement 实例已经由jdbcTemplate创建，并在回调后自动释放
			ps.setObject(1, name);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getLong("id"),
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("name")
					);
				}
				throw new RuntimeException("user not found by name");
			}
		});
	}
//	根据email查询user
	public User getUserByEmail(String email){
//		传入sql ，参数，rowmapper实例
		return jdbcTemplate.queryForObject("SELECT * FROM student WHERE email = ?",(ResultSet rs,int rowNum)->{
//			将resultset的当前行映射为一个JavaBean
			return new User(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("name")
			);
		},new Object[] { email});
	}
	public User fetchUserByEmail(String email){
		List<User> users = jdbcTemplate.query("SELECT * FROM student WHERE email = ?",(ResultSet rs,int rowNum)->{
			return new User(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("name"));
//					  name

		},new Object[] {email});
		return users.isEmpty() ? null : users.get(0);
	}
	public User fetchUserByEmail3(String email) {
		List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", (ResultSet rs, int rowNum) -> {
			return new User( // new User object:
					rs.getLong("id"), // id
					rs.getString("email"), // email
					rs.getString("password"), // password
					rs.getString("name")); // name
		}, new Object[] { email });
		return users.isEmpty() ? null : users.get(0);
	}

//	返回一行记录
//	public long getUser(){
//		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student",(ResultSet rs,int rowNum)->{
////			select count* 查询只有一列，取第一列数据
//			return rs.getLong(1);
//		});
//	}
//返回多行记录
public List<User> getUsers(int pageIndex){
		int limit= 100;
		int offset = limit*(pageIndex -1);
		return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?",new BeanPropertyRowMapper<>(User.class),new Object[] {limit,offset});
}
//更新
	public void updateUser(User user){
//		传入SQL，Sql参数，返回更新的行数
		if(1!=jdbcTemplate.update("UPDATE users name = ? WHERE id = ?",user.getName(),user.getId())){
			throw new RuntimeException("User not found by id");
		}
	}
//	插入操作
//	需要事务支持的方法加入@transactional注解/也可以加到class上，表示整个class方法都需要事务

	public User registerUser(String email, String password,String name){
//		创建一个keyHolder
		KeyHolder holder = new GeneratedKeyHolder();
		if(1!=jdbcTemplate.update(
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
}
