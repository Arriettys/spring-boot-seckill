package top.arrietty.MD5;

import java.util.UUID;

public class UUIDUtil
{
	public static String uuid() 
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
