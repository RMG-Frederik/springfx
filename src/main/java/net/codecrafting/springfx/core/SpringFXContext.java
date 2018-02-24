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
	 * Get a {@link ApplicationContext} instance. Only call this method after a successful {@link #run(String[])}
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
	 * Executes and initialize a {@link ApplicationContext}. This is the moment that Spring Boot is initialized.
	 * @param args arguments to pass to Spring Boot initialization.
	 */
    void run(String args[]);
	
	/**
	 * Stop SpringFXContext for preparing the application to exit.
	 */
    void stop();
}
