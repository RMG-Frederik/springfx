package net.codecrafting.springfx.exceptions;

public class DialogBuilderInitializationException extends RuntimeException
{
	private static final long serialVersionUID = -7008188960265492561L;
	
	public static final String DEFAULT_MESSAGE = "DialogBuilder is not initialized. Check SpringFx startup";
	
	public DialogBuilderInitializationException()
	{ 
		super(DEFAULT_MESSAGE); 
	}
	
	public DialogBuilderInitializationException(String message) 
	{ 
		super(message); 
	}
	
	public DialogBuilderInitializationException(String message, Throwable cause) 
	{ 
		super(message, cause); 
	}
	
	public DialogBuilderInitializationException(Throwable cause) 
	{ 
		super(cause); 
	}
}
