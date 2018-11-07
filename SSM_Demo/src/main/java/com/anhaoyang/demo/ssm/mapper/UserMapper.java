package com.anhaoyang.demo.ssm.mapper;

import com.anhaoyang.demo.ssm.entity.User;
import com.anhaoyang.demo.ssm.entity.UserExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int insert(User record);

    int insertSelective(User record);
    
    List<User> selectByExample(UserExample example);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);
    
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    @Select("select * from user where id=#{id}")
    User selectUser(Long id);
}