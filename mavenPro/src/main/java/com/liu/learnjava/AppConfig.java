package com.liu.learnjava;

import com.liu.learnjava.service.FileResourceService;
import com.liu.learnjava.service.MailSession;
import com.liu.learnjava.service.User;
import com.liu.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

//类启动器
@Configuration
@ComponentScan
@PropertySource("app.properties")
@PropertySource("smtp.properties")
@EnableAspectJAutoProxy
public class AppConfig {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		userService.register("hhhhh@example.com", "password", "23");
		context.getBean(MailSession.class);
//		source
		FileResourceService fileResourceService = context.getBean(FileResourceService.class);
		fileResourceService.printLogo();
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
}
