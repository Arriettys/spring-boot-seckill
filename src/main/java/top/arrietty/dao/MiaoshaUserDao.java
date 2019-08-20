package top.arrietty.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import top.arrietty.domain.MiaoShaUser;

@Mapper
public interface MiaoshaUserDao
{
	@Select("select * from miaosha_user where id=#{id}")
	public MiaoShaUser getById(@Param("id") long id);

	@Update("update miaosha_user set password=#{password} where id=#{id}")
	public void update(MiaoShaUser toBeUpdate);
	
}
