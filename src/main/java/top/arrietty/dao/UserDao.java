package top.arrietty.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import top.arrietty.domain.User;

@Mapper
public interface UserDao
{
	@Select("select *  from user where id=#{id}")
	public User get(@Param("id") int id);
	
	@Transactional
	@Insert("insert into user (id, name) values (#{id}, #{name})")
	public int insert(User user);
}
