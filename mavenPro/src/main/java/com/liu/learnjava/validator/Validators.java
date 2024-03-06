package com.liu.learnjava.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Component
public class Validators {
	@Autowired
	List<Validator> validators;
	public void validate(String email,String password,String name){
		for (var validator :validators){
			validator.validate(email,password,name);
		}
	}
}
