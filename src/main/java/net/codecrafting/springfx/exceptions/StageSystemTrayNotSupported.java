package net.codecrafting.springfx.exceptions;

public class StageSystemTrayNotSupported extends Exception
{
	private static final long serialVersionUID = 6100887719452159919L;
	public static final String DEFAULT_MESSAGE = "The current system does not support AWT System Tray";
	
	public StageSystemTrayNotSupported()
	{ 
		super(DEFAULT_MESSAGE); 
	}
	
	public StageSystemTrayNotSupported(String message) 
	{ 
		super(message); 
	}
	
	public StageSystemTrayNotSupported(String message, Throwable cause) 
	{ 
		super(message, cause); 
	}
	
	public StageSystemTrayNotSupported(Throwable cause) 
	{ 
		super(cause); 
	}
}
