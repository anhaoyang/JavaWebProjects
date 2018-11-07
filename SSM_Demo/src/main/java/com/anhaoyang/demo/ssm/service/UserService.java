package com.anhaoyang.demo.ssm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import com.anhaoyang.demo.ssm.entity.User;
import com.anhaoyang.demo.ssm.entity.UserExample;

public interface UserService {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int insert(User record);

    int insertSelective(User record);
    
    @Cacheable(cacheNames="Ehcache",key="'UserService_selectByExample?liveTime=3'")
    public List<User> selectByExample(UserExample example);
    
	@Cacheable(cacheNames="Ehcache",key="'UserService_selectUser_id_' + #id+'?liveTime=10'")
    public User selectUser(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);
    
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);
}
