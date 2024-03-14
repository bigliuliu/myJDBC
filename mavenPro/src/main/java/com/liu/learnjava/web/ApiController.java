package com.liu.learnjava.web;

import com.liu.learnjava.entity.ResultResponse;
import com.liu.learnjava.entity.User;
import com.liu.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	UserService userService;
	@GetMapping("/users")
	public List<User> users(){
		return userService.getUsers();
	}
	@GetMapping("/users/{id}")
	public ResultResponse user(@PathVariable("id") long id){
		ResultResponse resultResponse = new ResultResponse();
		ResultResponse.Record record = new ResultResponse.Record();
		try {
			User user = userService.getUserById(id);
			record.setCode("200");
			record.setData(user);
			resultResponse.setRecord(record);

		}catch (Exception e){
			record.setCode("500");
			record.setData("not found");
			resultResponse.setRecord(record);
		}
		return resultResponse;
	}
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
}
