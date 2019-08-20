package top.arrietty.redis;

public interface KeyPrefix
{
	public int expireSeconds();	
	public String getPrefix();
}
