package com.anhaoyang.demo.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhaoyang.demo.ssm.entity.User;
import com.anhaoyang.demo.ssm.entity.UserExample;
import com.anhaoyang.demo.ssm.mapper.UserMapper;
import com.anhaoyang.demo.ssm.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public int countByExample(UserExample example) {
		// TODO Auto-generated method stub
		return userMapper.countByExample(example);
	}

	@Override
	public int deleteByExample(UserExample example) {
		// TODO Auto-generated method stub
		return userMapper.deleteByExample(example);
	}

	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(User record) {
		// TODO Auto-generated method stub
		return userMapper.insertSelective(record);
	}

	@Override
	public List<User> selectByExample(UserExample example) {
		// TODO Auto-generated method stub
		System.err.println("访问了数据库");
		return userMapper.selectByExample(example);
	}

	@Override
	public int updateByExampleSelective(User record, UserExample example) {
		// TODO Auto-generated method stub
		return userMapper.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(User record, UserExample example) {
		// TODO Auto-generated method stub
		return userMapper.updateByExample(record, example);
	}
	
	@Override
	public User selectUser(Long id) {
		// TODO Auto-generated method stub
		System.err.println("访问了数据库");
		return userMapper.selectUser(id);
	}

}
