package com.liu.learnjava.web;

import com.liu.learnjava.entity.ResultResponse;
import com.liu.learnjava.entity.User;
import com.liu.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api")
/*
*允许跨域访问
*  方法一
* */
@CrossOrigin(origins = "http://local.liaoxuefeng.com:8080")
// @CrossOrigin(origins = {"a.com","b.com"})
// 允许任何域访问
// @CrossOrigin(origins = "*")
public class ApiController {
	@Autowired
	UserService userService;
	// @GetMapping("/users")
	// public List<User> users(){
	// 	return userService.getUsers();
	// }
	/*
	*async处理的方法一
	* callable
	* */
	@GetMapping("/users")
	public Callable<List<User>> users(){
		return ()->{
		// 	模拟3s耗时
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e){
			}
			return userService.getUsers();
		};
	}
	/*
	* async处理的方法二
	* 返回DeferredResult
	* */
	@GetMapping("/users/{id}")
	public DeferredResult<User> user(@PathVariable("id") long id){
		DeferredResult<User> result = new DeferredResult<>(3000L);//3s超时
		new Thread(()->{
		// 	等待1s
			try {
				Thread.sleep(1000);
			}catch (InterruptedException e){}
			try {
				User user = userService.getUserById(id);
			// 	设置正常结果并由spring mvc 写入response
				result.setResult(user);
			}catch (Exception e){
			// 	设置错误结果并由spring mvc写入response
				result.setErrorResult(Map.of("error",e.getClass().getSimpleName(),"message",e.getMessage()));
			}
		}).start();
		return result;
	}
	// @GetMapping("/users/{id}")
	// public ResultResponse user(@PathVariable("id") long id){
	// 	ResultResponse resultResponse = new ResultResponse();
	// 	ResultResponse.Record record = new ResultResponse.Record();
	// 	try {
	// 		User user = userService.getUserById(id);
	// 		record.setCode("200");
	// 		record.setData(user);
	// 		resultResponse.setRecord(record);
	//
	// 	}catch (Exception e){
	// 		record.setCode("500");
	// 		record.setData("not found");
	// 		resultResponse.setRecord(record);
	// 	}
	// 	return resultResponse;
	// }
	@PostMapping("/signin")
	public Map<String,Object> signin(@RequestBody SignInRequest signInRequest){
		try {
			User user = userService.signin(signInRequest.email,signInRequest.password);
			return Map.of("user",user);
		}catch (Exception e){
			return Map.of("error","登录失败","message",e.getMessage());
		}
	}
	public static class SignInRequest{
		public String email;
		public String password;
	}
	/*
	* 注解处理异常
	* */
	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleUnknowException(Exception e){
		return new ModelAndView("index.html",Map.of("error","登录失败","message",e.getMessage()));
	}
}
