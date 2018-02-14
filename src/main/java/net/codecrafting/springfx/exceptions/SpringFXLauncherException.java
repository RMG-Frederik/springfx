package net.codecrafting.springfx.exceptions;

public class SpringFXLauncherException extends Exception
{
	private static final long serialVersionUID = -3077934869176123746L;
	public static final String DEFAULT_MESSAGE = "SpringFX failed to launch";
	
	public SpringFXLauncherException()
	{ 
		super(DEFAULT_MESSAGE); 
	}
	
	public SpringFXLauncherException(String message) 
	{ 
		super(message); 
	}
	
	public SpringFXLauncherException(String message, Throwable cause) 
	{ 
		super(message, cause); 
	}
	
	public SpringFXLauncherException(Throwable cause) 
	{ 
		super(cause); 
	}
}
