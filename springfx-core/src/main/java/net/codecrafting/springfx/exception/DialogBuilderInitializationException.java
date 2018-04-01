/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2018 The SpringFX Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.exception;

import net.codecrafting.springfx.util.DialogBuilder;

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
