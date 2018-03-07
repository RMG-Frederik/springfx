package net.codecrafting.springfx.exception;

import net.codecrafting.springfx.util.StageSystemTray;

/**
 * This exception used to indicate that the usage of {@link StageSystemTray} is not possible
 * due to unsupported AWT System. Normally this exception happens with the system was configured
 * with as a headless application, check {@link StageSystemTray} for more information.
 * @author Lucas Marotta
 */
public class StageSystemTrayNotSupported extends Exception
{
	private static final long serialVersionUID = 6100887719452159919L;
	
	/**
	 * The default message to be thrown
	 */
	public static final String DEFAULT_MESSAGE = "The current system does not support AWT System Tray";
	
	/**
	 * Create a new instance of {@link StageSystemTrayNotSupported}. This constructor 
	 * uses the {@link #DEFAULT_MESSAGE} message value.
	 */
	public StageSystemTrayNotSupported()
	{ 
		super(DEFAULT_MESSAGE); 
	}
	
	/**
	 * Create a new instance of {@link StageSystemTrayNotSupported}. This constructor 
	 * uses a custom message.
	 * @param message to be thrown
	 */
	public StageSystemTrayNotSupported(String message) 
	{ 
		super(message); 
	}
}
