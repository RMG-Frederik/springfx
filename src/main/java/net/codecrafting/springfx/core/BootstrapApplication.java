package net.codecrafting.springfx.core;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.codecrafting.springfx.context.StageContext;
import net.codecrafting.springfx.context.ViewStage;
import net.codecrafting.springfx.util.Dialog;
import net.codecrafting.springfx.util.DialogBuilder;
import net.codecrafting.springfx.util.Mipmap;

/**
 * Class that implements the JavaFX {@link Application}. The main goal it's to bootstrap the JavaFX application
 * and servers as a interface to the user implementation of {@link SpringFXApplication}. The SpringFXApplication
 * able's a custom Stage with the {@link ViewStage} to a better startup process, handling multiple screens and support 
 * for Spring DI.
 * 
 * @author Lucas Marotta
 * @see #getSpringFXContext()
 * @see #init()
 * @see #start(Stage)
 * @see #stop()
 */
public class BootstrapApplication extends Application
{
	/**
	 * Last running BootstrapApplication
	 */
	private static BootstrapApplication instance;
	
	private ViewStage viewStage;
	private SpringFXContext springFXContext;
	
	/**
	 * Flag to indicate that JavaFX Toolkit has initialized
	 */
	private static boolean toolkitInitialized = false; 
	private static final Log LOGGER = LogFactory.getLog(BootstrapApplication.class);
	
	/**
	 * Create a new instance of {@link BootstrapApplication}.
	 * Meant to only be used for JavaFX {@link Application#launch(String...)}.
	 * To create regular instances of this class use @see {@link #BootstrapApplication(SpringFXContext)}
	 * @throws IllegalStateException if Application Toolkit has already initialized
	 */
	public BootstrapApplication()
	{
		if(!toolkitInitialized) {
			if(instance != null) springFXContext = instance.getSpringFXContext();
			toolkitInitialized = true;
		} else {
			throw new IllegalStateException("Application Toolkit has already initialized.");
		}
	}
	
	/**
	 * Create a new instance of {@link BootstrapApplication}.
	 * Every time that new instance is created, the {@link #instance} attribute will be updated.
	 * @param springFXContext the springFXContext. Always use a initialized context.
	 */
	public BootstrapApplication(SpringFXContext springFXContext)
	{
		this.springFXContext = springFXContext;
		instance = this;
	}
	
	/**
	 * Get the springFxContext
	 * @return {@link SpringFXContext}
	 */
	public SpringFXContext getSpringFXContext()
	{
		return springFXContext;
	}
	
    /**
     * The application initialization method. This method is called immediately
     * after the Application class is loaded and constructed. This method also
     * will call for the user implementation of {@link SpringFXApplication#init()}}.
     * 
     * <p>
     * NOTE: The {@link Dialog} properties passed to Spring are initialized here.
     * </p>
     *
     * <p>
     * NOTE: This method is not called on the JavaFX Application Thread. An
     * application must not construct a Scene or a Stage in this
     * method.
     * An application may construct other JavaFX objects in this method.
     * </p>
     * @throws Exception if the implementation of {@link SpringFXApplication#init()} fails
     */
	@Override
	public void init() throws Exception 
	{
		LOGGER.info("JavaFX Bootstrap Application initialization");
		setDialogProperties();
		springFXContext.getApplication().init();
	}
	
	/**
     * The main entry point for all JavaFX applications.
     * The start method is called after the {@link #init()} method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is has to be called on the JavaFX Application Thread.
     * </p>
     * 
	 * @param primaryStage the ViewStage (common window) created by {@link BootstrapApplication}
	 * @throws Exception if the implementation of {@link SpringFXApplication#start(ViewStage)} fails
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception 
	{
		LOGGER.info("JavaFX Bootstrap Application startup");
		if(SpringFXLauncher.isRelaunchable()) Platform.setImplicitExit(false);
		Environment env = springFXContext.getEnvironment();
		final SpringFXApplication application = springFXContext.getApplication();
		viewStage = new ViewStage(springFXContext.getSpringContext());
		String root = env.getProperty("springfx.app.root-controller");
		if(root != null) {
			Class<? extends StageContext> rootController = (Class<? extends StageContext>) Class.forName(root);
			viewStage.setTitle(env.getProperty("springfx.app.name", application.getClass().getSimpleName()));
			String iconPath = env.getProperty("springfx.app.icon");
			if(iconPath != null) {
				viewStage.setIconByMipmap(new Mipmap(iconPath, new int[] {1,2,4,8,16,32}));
			}
			viewStage.init(rootController);
			if(env.getProperty("springfx.app.auto-open", "true").equals("true")) viewStage.show(true);
		}
		application.start(viewStage);
	}
	
    /**
     * This method is called when the application should stop, and provides a
     * convenient place to prepare for application exit and destroy resources. This
     * method also executes the following sequence:
     * 
	 * <ul>
	 * <li>Close the {@link ViewStage} if available.</li>
	 * <li>Run the {@link SpringFXContext} with optional arguments</li>
	 * <li>Call the user implementation of {@link SpringFXContext#stop()}.</li>
	 * <li>Clear the current {@link BootstrapApplication#instance}.</li>
	 * <li>Call the stop implementation of {@link SpringFXContext#stop()}</li>
	 * </ul>
     *
     * <p>
     * NOTE: This method has to be called called on the JavaFX Application Thread.
     * </p>
     * 
	 * @throws Exception if the implementation of {@link SpringFXApplication#stop()} or
	 * {@link SpringFXContext#stop()} fails.
	 */
	@Override
	public void stop() throws Exception 
	{
		LOGGER.info("Stopping JavaFX Application");
		if(viewStage != null) viewStage.close();
		springFXContext.getApplication().stop();
		instance = null;
		
		//If the user does not call for stop
		springFXContext.stop();
	}

	private void setDialogProperties()
	{
		Environment env = springFXContext.getEnvironment();
		Properties props = new Properties();
		for (Map.Entry<Object, Object> e : DialogBuilder.DEFAULT_PROPS.entrySet()) {
			String key = e.getKey().toString();
			if(env.containsProperty(key)) {
				props.setProperty(key, env.getProperty(key));
			}
		}
		DialogBuilder.setProperties(props);
	}
	
	/**
	 * Get the last running BoostrapApplication instance. This is mainly used for
	 * handling re-launch's and for testing purposes. 
	 * @return the current instance of {@link BootstrapApplication}
	 */
	public static BootstrapApplication getInstance()
	{
		return instance;
	}
	
	/**
	 * Check if the status of {@link #toolkitInitialized} attribute.
	 * @return true if the toolkit is initialized.
	 */
	public static boolean isToolkitInitialized()
	{
		return toolkitInitialized;
	}
}
