package top.arrietty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.arrietty.dao.UserDao;
import top.arrietty.domain.User;
import top.arrietty.result.CodeMsg;
import top.arrietty.vo.LoginVo;
@Service
public class UserService
{
	@Autowired
	UserDao userDao;
	
	public User getById(int id)
	{
		return userDao.get(id);
	}
	
	public boolean tx()
	{
		User user = new User();
		user.setId(3);
		user.setName("joe");
		userDao.insert(user);
		return true;
	}
}
