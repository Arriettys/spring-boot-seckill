package top.arrietty.exception;

import top.arrietty.result.CodeMsg;

public class GlobalException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7492253100734021496L;
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) 
	{
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() 
	{
		return cm;
	}
}
