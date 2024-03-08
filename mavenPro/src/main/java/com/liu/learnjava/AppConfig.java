package com.liu.learnjava;

import com.liu.learnjava.service.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.ZoneId;
import java.util.List;

//类启动器
@Configuration
@ComponentScan
@PropertySource("app.properties")
@PropertySource("smtp.properties")
//@EnableAspectJAutoProxy
@PropertySource("jdbc.properties")
//启用声明式
@EnableTransactionManagement
public class AppConfig {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		context.getBean(MailSession.class);
//		source
		FileResourceService fileResourceService = context.getBean(FileResourceService.class);
		fileResourceService.printLogo();
		UserServiceDao userService = context.getBean(UserServiceDao.class);
		System.out.println(userService.getClass()+"-------");
//		User user = userService.login("bob@example.com", "password");
//		userService.register("hhhhh@example.com", "password", "23");
//		User userResult = userService.getUserById(6);
//		User userResult = userService.getUserByEmail("4");
//		插入bob
		if(userService.fetchUserByEmail("bo4343b@example.com")==null){
			userService.registerUser("bo4343b@example.com", "password1", "Bob");
		}
		// 插入Alice:
		if (userService.fetchUserByEmail("ali99@example.com") == null) {
			userService.registerUser("ali99@example.com", "password2", "Ali656ce");
		}
		try {
			userService.registerUser("root@example.com", "password3", "root");
		}catch (RuntimeException e){
			System.out.println(e.getMessage());
		}
//		查询所有用户
		for(User u: userService.getUsers(1)){
			System.out.println(u);
		}
//		System.out.println(user.getName());

//		关闭ioc容器
		((ConfigurableApplicationContext) context).close();
	}

//	@Bean
//	@Primary
//	@Qualifier("z")
//	ZoneId createZoneOfZ(@Value("${app.zone:Z}") String zoneId) {
//		return ZoneId.of(zoneId);
//	}

//	@Bean
//	@Qualifier("utc8")
//	ZoneId createZoneOfUTC8() {
//		return ZoneId.of("UTC+08:00");
//	}
	@Bean
	@Profile("!test")
	ZoneId createZoneId(){
		return ZoneId.systemDefault();
	}
	@Bean
	@Profile("test")
	ZoneId createZoneIdForTest(){
		return ZoneId.of("America/New_York");
	}
//	创建数据库连接必须的bean
	@Value("${jdbc.url}")
	String jdbcUrl;
	@Value("${jdbc.username}")
	String jdbcUsername;
	@Value("${jdbc.password}")
	String jdbcPassword;
	@Bean
	DataSource createDataSource(){
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.addDataSourceProperty("autoCommit","true");
		config.addDataSourceProperty("connectionTimeout","5");
		config.addDataSourceProperty("idleTimeout","60");
		return new HikariDataSource(config);
	}
	@Bean
	JdbcTemplate createJdbcTemplate (@Autowired DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}
	@Bean
//	事务管理器
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource){
		return  new DataSourceTransactionManager(dataSource);
	}
}
