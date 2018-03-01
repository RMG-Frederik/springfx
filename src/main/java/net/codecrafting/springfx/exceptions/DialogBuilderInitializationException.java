package net.codecrafting.springfx.exceptions;

import net.codecrafting.springfx.utils.DialogBuilder;

/**
 * This exception is a {@link RuntimeException} that is used to indicated that 
 * the {@link DialogBuilder} has not initialized yet.
 * @author Lucas Marotta
 */
public class DialogBuilderInitializationException extends RuntimeException
{
	private static final long serialVersionUID = -7008188960265492561L;
	
	/**
	 * The default message to be thrown
	 */
	public static final String DEFAULT_MESSAGE = "DialogBuilder is not initialized. Check SpringFx startup";
	
	/**
	 * Create a new instance of {@link DialogBuilderInitializationException}. This constructor 
	 * uses the {@link #DEFAULT_MESSAGE} message value.
	 */
	public DialogBuilderInitializationException()
	{ 
		super(DEFAULT_MESSAGE); 
	}
	
	/**
	 * Create a new instance of {@link DialogBuilderInitializationException}. This constructor 
	 * uses a custom message.
	 * @param message to be thrown
	 */
	public DialogBuilderInitializationException(String message) 
	{ 
		super(message); 
	}
}
