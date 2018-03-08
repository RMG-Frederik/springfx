package net.codecrafting.springfx.exception;

import net.codecrafting.springfx.core.SpringFXContext;
import net.codecrafting.springfx.core.SpringFXLauncher;

/**
 * This exception used to indicate that a {@link SpringFXLauncher} launch failed
 * for a empty {@link SpringFXContext}, improper use of {@link SpringFXLauncher#setRelaunchable(boolean)},
 * or any other failure during the launch.
 * @author Lucas Marotta
 */
public class SpringFXLaunchException extends Exception
{
	private static final long serialVersionUID = -3077934869176123746L;
			
	/**
	 * The default message to be thrown
	 */
	public static final String DEFAULT_MESSAGE = "SpringFX failed to launch";
	
	/**
	 * Create a new instance of {@link SpringFXLaunchException}. This constructor 
	 * uses the {@link #DEFAULT_MESSAGE} message value.
	 */
	public SpringFXLaunchException()
	{ 
		super(DEFAULT_MESSAGE);
	}
	
	/**
	 * Create a new instance of {@link SpringFXLaunchException}. This constructor 
	 * uses a custom message.
	 * @param message to be thrown
	 */
	public SpringFXLaunchException(String message) 
	{ 
		super(message); 
	}
}
