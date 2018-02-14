package net.codecrafting.springfx.core;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.application.Platform;
import net.codecrafting.springfx.exceptions.SpringFXLauncherException;
import net.codecrafting.springfx.utils.DialogBuilder;


/**
 * Class that can be used to bootstrap and launch a Spring application from a Java main
 * method as a example. By default class will perform the following steps to bootstrap your
 * application:
 * 
 * <ul>
 * <li>Register a {@link SpringFXContext} to create a {@link ApplicationContext}</li>
 * <li>Run the {@link SpringFXContext} with optional arguments</li>
 * <li>Create a internal JAVAFX Bootstrap Application ({@link BootstrapApplication} to call your {@link SpringFXApplication#start(net.codecrafting.springfx.controls.ViewStage)} implementation)</li>
 * </ul>
 * 
 * The default behavior of {@link #launch(Class)} is to use the internal {@link SpringFXContextImpl},
 * which define a non web application context with optional Headless option @see {@link SpringFXContextImpl#isHeadless()}.
 * You can call SpringFX like this:
 * 
 * <pre class="code">
 * &#064;SpringBootApplication
 * public class MyApplication extends SpringFXApplication 
 * {
 *
 *   // ... Bean definitions
 *
 *   public static void main(String[] args) throws Exception 
 *   {
 *   	SpringFXContext context = SpringFXLauncher.launch(MyApplication.class, args);
 *   }
 *   
 *   &#064;Override
 *   public void start(ViewStage viewStage) throws Exception 
 *   {
 *   	// ... code here
 *   }
 * }
 * </pre>
 * 
 * <b>OBS:</b> Note that your application must extends {@link SpringFXApplication}
 * 
 * However if you intent to use your own custom {@link ApplicationContext}, create a implementation of
 * {@link SpringFXContext} which you can define your own context. 
 * To launch your custom {@link SpringFXContext} @see {@link #launch(SpringFXContext, String[])}.
 * 
 * @author Lucas Marotta
 * @see #launch(String[])
 * @see #launch(Class, Properties)
 * @see #launch(SpringFXContext, String[])
 */
@SuppressWarnings("restriction")
public class SpringFXLauncher 
{
	/**
	 * The context of this launcher which contains the {@link ApplicationContext} and a {@link SpringFXApplication}
	 */
	private SpringFXContext context;
	
	/**
	 * A flag to indicate if SpringFX is relaunchable.
	 */
	private static boolean relaunchable = false;
	
	/**
	 * Controls the Lifecycle of JavaFX Application
	 */
	private static ExecutorService executorService;
	
	/**
	 * Flag to indicate that JavaFX launch was once initialized
	 */
	private static boolean fxApplicationLaunched = false;
	private static final Log LOGGER = LogFactory.getLog(SpringFXLauncher.class);
	
	/**
	 * Create a new instance of {@link SpringFXLauncher} with a custom {@link SpringApplicationBuilder}
	 * @param context a implementation of {@link SpringFXContext} to initialize Spring Boot
	 * @throws IllegalArgumentException if SpringFXContext is null
	 */
	public SpringFXLauncher(SpringFXContext context)
	{
		initialize(context);
	}
	
	/**
	 * Launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application
	 * @param args arguments to pass to Spring Boot and JavaFX
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public void launch(String args[]) throws Exception
	{
		if(args != null) {
			if(fxApplicationLaunched && !relaunchable) {
				throw new SpringFXLauncherException("Relaunchable attribute is false");
			} else {
				initLaunch(args);
			}	
		} else {
			throw new IllegalArgumentException("Args must not be null");
		}
	}
	
	/**
	 * Launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application.
	 * @param props properties to pass to {@link System#setProperty(String, String)}
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public void launch(Properties props) throws Exception
	{
		if(props != null) {
			if(fxApplicationLaunched && !relaunchable) {
				throw new SpringFXLauncherException("Relaunchable attribute is false");
			} else {
				System.getProperties().putAll(props);
				initLaunch(new String[0]);
			}	
		} else {
			throw new IllegalArgumentException("Properties must not be null");
		}
	}
	
	/**
	 * Get the SpringFXContext
	 * @return a instance of {@link SpringFXContext}. May not be initialized. Use this after launch
	 */
	public SpringFXContext getContext()
	{
		return context;
	}
	
	private void initialize(SpringFXContext context)
	{
		if(context != null) {
			DialogBuilder.init();
			executorService = Executors.newCachedThreadPool();
			this.context = context;	
		} else {
			throw new IllegalArgumentException("SpringFXContext must not be null");
		}
	}
	
	private void initLaunch(String args[]) throws SpringFXLauncherException
	{
		context.run(args);
		if(isSpringFXContextEmpty()) {
			context.getSpringContext().addApplicationListener(new ApplicationListener<ContextStoppedEvent>() {
				@Override
				public void onApplicationEvent(ContextStoppedEvent event) 
				{
					LOGGER.info("Fx Thread Executor Shutdown");
					executorService.shutdownNow();
					if(!relaunchable) {
						Platform.exit();
						context.getSpringContext().close();
						LOGGER.info("SpringFXContext closed");
					} else {
						PlatformImpl.runAndWait(() -> {
							try {
								BootstrapApplication.getInstance().stop();
							} catch (Exception e) {
								LOGGER.error(e.getMessage(), e);
							}
							context.getSpringContext().close();
							LOGGER.info("SpringFXContext closed");
						});
					}
				}
			});
			LOGGER.info("SpringFX launched");
			launchFxApplication(args);	
		} else {
			throw new SpringFXLauncherException("SpringFXContext must not be empty");
		}
	}
	
	private boolean isSpringFXContextEmpty()
	{
		try {
			if(context.getSpringContext() == null || context.getApplication() == null || context.getEnvironment() == null)
				return false;
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
		return true;
	}	
	
	private void launchFxApplication(String args[])
	{
		fxApplicationLaunched = true;
		final BootstrapApplication application = new BootstrapApplication(context);
		if(!BootstrapApplication.isToolkitInitialized()) {
			executorService.submit(() -> {
				Application.launch(BootstrapApplication.class, args);
			});
		} else {
			executorService.submit(() -> {
				try {
					application.init();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				Platform.runLater(() -> {
					try {
						application.start(null);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				});
			});
		}
	}
	
	/**
	 * Helper for launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application
	 * @param appClass the user application class with Spring configuration annotations
	 * @return a initialized {@link SpringFXContext}
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public static SpringFXContext launch(final Class<? extends SpringFXApplication> appClass) throws Exception
	{
		return launch(appClass, new String[0]);
	}
	
	/**
	 * Helper for launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application.
	 * @param appClass the user application class with Spring configuration annotations
	 * @param args arguments to pass to Spring Boot and JavaFX
	 * @return a initialized {@link SpringFXContext}
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public static SpringFXContext launch(final Class<? extends SpringFXApplication> appClass, final String args[]) throws Exception
	{
		final SpringFXLauncher launcher = new SpringFXLauncher(new SpringFXContextImpl(appClass));
		launcher.launch(args);
		return launcher.getContext();
	}
	
	/**
	 * Helper for launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application.
	 * @param appClass the user application class with Spring configuration annotations
	 * @param props properties to pass to {@link System#setProperty(String, String)}
	 * @return a initialized {@link SpringFXContext}
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public static SpringFXContext launch(final Class<? extends SpringFXApplication> appClass, final Properties props) throws Exception
	{
		final SpringFXLauncher launcher = new SpringFXLauncher(new SpringFXContextImpl(appClass));
		launcher.launch(props);
		return launcher.getContext();
	}
	
	/**
	 * Helper for launch a {@link SpringFXApplication} to initialize and run the Spring Boot and a JavaFX Application.
	 * @param context a implementation of {@link SpringFXContext} to initialize Spring Boot
	 * @param args arguments to pass to Spring Boot and JavaFX
	 * @throws Exception if Spring Boot or JavaFX fails to initialize
	 */
	public static void launch(SpringFXContext context, String args[]) throws Exception
	{
		new SpringFXLauncher(context).launch(args);
	}
	
	
	/**
	 * Helper for check if SpringFX can be relaunched. This is for not call {@link Platform#exit()} (if relaunchable is true)
	 * since the JAVAFX startup can only called once.
	 * @return true if is relaunchable
	 * @defaultValue false
	 */
	public static boolean isRelaunchable()
	{
		return relaunchable;
	}
	
	/**
	 * Helper for set SpringFX to be relaunchable. This is for not call {@link Platform#exit()} (if relaunchable is true).
	 * This is mainly for testing and it's not considered safe.
	 * @param relaunchable attribute to define if the SpringFX will relaunchable
	 */
	public static void setRelaunchable(boolean relaunchable)
	{
		SpringFXLauncher.relaunchable = relaunchable;
	}
}
