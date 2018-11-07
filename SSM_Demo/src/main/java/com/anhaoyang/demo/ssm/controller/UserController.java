package com.anhaoyang.demo.ssm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anhaoyang.demo.ssm.entity.User;
import com.anhaoyang.demo.ssm.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/getUserInfo")
    public List<User> getUserInfo() {
		List<User> user = userService.selectByExample(null);
        System.out.println(user.toString());
        return user;
    }
	
	@ResponseBody
	@RequestMapping("/getUser")
    public String getUser() {
		User user = userService.selectUser(1L);
        System.out.println(user.toString());
        return user.toString();
    }
	
}