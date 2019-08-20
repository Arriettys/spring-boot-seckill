package top.arrietty.MD5;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util
{
	public static String md5(String str)
	{
		return DigestUtils.md5Hex(str);
	}
	
	public static final String salt = "1a2b3c4d";
	
	public static String inputPassToFormPass(String inputPass)
	{
		String str = ""+salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass, String salt)
	{
		String str = ""+salt.charAt(0) + salt.charAt(1) + formPass + salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDBPass(String inputPass, String salt)
	{
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, salt);
		return dbPass;
	}
	
	public static void main(String[] args) 
	{
		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "qwertyui"));
		
	}
}
