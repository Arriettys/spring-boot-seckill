package top.arrietty.redis;

public class BasePrefix implements KeyPrefix
{
	private int expireSeconds;	
	private String prefix;
	
	//0������������
	public BasePrefix(String prefix) 
	{
		this(0, prefix);
	}
	public BasePrefix(int expireSeconds, String prefix)
	{
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	//Ĭ��0������������
	public int expireSeconds()
	{
		return expireSeconds;
	}

	public String getPrefix()
	{
		String className = getClass().getSimpleName();
		return className+":" + prefix;
	}

}
