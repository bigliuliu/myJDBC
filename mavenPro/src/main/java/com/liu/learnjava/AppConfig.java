package com.liu.learnjava;

import com.liu.learnjava.entity.User;
import com.liu.learnjava.mapperService.UserServiceMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.ZoneId;
import java.util.Properties;

//类启动器
@Configuration
@ComponentScan
@PropertySource("app.properties")
@PropertySource("smtp.properties")
//@EnableAspectJAutoProxy
@PropertySource("jdbc.properties")
@MapperScan("com.liu.learnjava.mapper")
//启用声明式
@EnableTransactionManagement
//mybatis 自动创建mapper的实现类
public class AppConfig {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserServiceMap userServiceMap = context.getBean(UserServiceMap.class);
//		插入bob
		if (userServiceMap.fetchUserByEmail("jpa1@example.com") == null) {
			User bob = userServiceMap.register("jpa1@example.com", "password1", "Bob");
			System.out.println("注册成功：" + bob);
		}
		// 插入Alice:
		if (userServiceMap.fetchUserByEmail("jpa2@example.com") == null) {
			User alice = userServiceMap.register("jpa2@example.com", "password2", "Ali656ce");
			System.out.println("注册成功：" + alice);
		}
//		查询所有用户
		for (User u : userServiceMap.getUsers(1)) {
			System.out.println(u);
		}
		User bob = userServiceMap.login("jpa1@example.com", "password1");
		System.out.println(bob);
//		关闭ioc容器
		((ConfigurableApplicationContext) context).close();
	}

	//	创建数据库连接必须的bean
	@Value("${jdbc.url}")
	String jdbcUrl;
	@Value("${jdbc.username}")
	String jdbcUsername;
	@Value("${jdbc.password}")
	String jdbcPassword;

	@Bean
	DataSource createDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.addDataSourceProperty("autoCommit", "true");
		config.addDataSourceProperty("connectionTimeout", "5");
		config.addDataSourceProperty("idleTimeout", "60");
		return new HikariDataSource(config);
	}

	//	启用hibernate 需要创建的localsessionfactorybean
//	@Bean
//	LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource){
//		var props = new Properties();
//		props.setProperty("hibernate.hbm2ddl.auto","update");//生产环境不要使用
////		props.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect");
//		props.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
//		props.setProperty("hibernate.show_sql","true");
//		var sessionFactoryBean = new LocalSessionFactoryBean();
//		sessionFactoryBean.setDataSource(dataSource);
////		扫描指定的package获取所有entity class；
//		sessionFactoryBean.setPackagesToScan(AbstractEntity.class.getPackageName());
//		sessionFactoryBean.setHibernateProperties(props);
//		return sessionFactoryBean;
//	}
//	使用JPA接口
//	@Bean
//	public LocalContainerEntityManagerFactoryBean createEntityManagerFactory(@Autowired DataSource dataSource) {
//		var emFactory = new LocalContainerEntityManagerFactoryBean();
//		// 注入DataSource:
//		emFactory.setDataSource(dataSource);
//		// 扫描指定的package获取所有entity class:
//		emFactory.setPackagesToScan(AbstractEntity.class.getPackageName());
//		// 使用Hibernate作为JPA实现:
//		emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//		// 其他配置项:
//		var props = new Properties();
//		props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
//		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//		props.setProperty("hibernate.show_sql", "true");
//		emFactory.setJpaProperties(props);
//		return emFactory;
//	}

//	j使用mybatis
	@Bean
	SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource){
		var sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean;
	}
//	事务管理器和jdbc一致
	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}

	//	创建hibernate事务管理器
//	@Bean
//	PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
//		return new HibernateTransactionManager(sessionFactory);
//	}
//	实例化jpa 事务管理器
//	@Bean
//	PlatformTransactionManager createTxManager(@Autowired EntityManagerFactory entityManagerFactory) {
//		return new JpaTransactionManager(entityManagerFactory);
//	}
}
