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
package net.codecrafting.springfx.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Interface indicating a class that contains and exposes basic references to a Spring Boot context initialization.
 * This interface its used to give a full control of your own {@link ApplicationContext} and common references to 
 * {@link SpringFXLauncher}.
 * 
 * @author Lucas Marotta
 * @see #getSpringContext()
 * @see #getApplication()
 * @see #getEnvironment()
 * @see #run(String[])
 * @see #stop()
 */
public interface SpringFXContext
{
	/**
	 * Get a {@link ConfigurableApplicationContext} instance. Only call this method after a successful {@link #run(String[])}
	 * @return {@link ConfigurableApplicationContext} instance. Can return null.
	 */
    ConfigurableApplicationContext getSpringContext();

	/**
	 * Get a {@link SpringFXApplication} instance. This is the user application, can be casted to that class.
	 * Only call this method after a successful {@link #run(String[])}.
	 * @return {@link SpringFXApplication} instance. Can return null.
	 */
    SpringFXApplication getApplication();

	/**
	 * Get a {@link Environment} instance. Spring context Environment.
	 * Only call this method after a successful {@link #run(String[])}.
	 * @return {@link Environment} instance. Can return null.
	 */
    Environment getEnvironment();
	
	/**
	 * Executes and initialize a {@link ConfigurableApplicationContext}. This is the moment that Spring Boot is initialized.
	 * @param args arguments to pass to Spring Boot initialization.
	 */
    void run(String args[]);
	
	/**
	 * Stop SpringFXContext for preparing the application to exit.
	 */
    void stop();
}
