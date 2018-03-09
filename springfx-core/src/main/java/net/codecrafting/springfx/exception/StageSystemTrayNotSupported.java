/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
