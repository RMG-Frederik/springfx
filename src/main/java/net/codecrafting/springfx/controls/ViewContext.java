package net.codecrafting.springfx.controls;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import net.codecrafting.springfx.animation.AnimationBuilder;
import net.codecrafting.springfx.animation.EaseInterpolator;
import net.codecrafting.springfx.annotation.ViewController;

/**
 * This class is used to abstract a JavaFX controller. The typical JavaFX controller {@link Initializable}
 * is implemented by this class. The goal of {@link ViewContext} is to provide a standard manipulation of 
 * views and their key {@link Node}s. This class also facilitates the access of the top level structure of 
 * JavaFX, the {@link ViewStage}, along with the transaction data within the {@link Intent}s. 
 * With these features, {@link ViewContext} allow a easy management of views for all controllers.
 * 
 * @author Lucas Marotta
 * @see #initialize(URL, ResourceBundle)
 * @see #getViewName()
 * @see #getViewTitle()
 * @see #setViewStage(ViewStage)
 * @see #getViewStage()
 * @see #getIntent()
 * @see #swapAnimation(Node)
 * @see #onStart()
 * @see #onCreate(URL, ResourceBundle)
 */
public abstract class ViewContext implements Initializable
{
	/**
	 * The view context name that will be used to find the corresponding FXML file
	 */
	protected String viewName;
	
	/**
	 * The view context title that can be used to indicate the title of the view interface
	 */
	protected String viewTitle;
	
	/**
	 * The view stage associated with this context
	 */
	protected ViewStage viewStage;
	
	//The internal AnimationBuilder to create a animation between views
	private final AnimationBuilder animationBuilder;
	private static final String UPPER_CAMEL_REGEX = "([a-z])([A-Z]+)";
	private static final String UPPER_CAMEL_REPLACEMENT = "$1_$2";
	
	/**
	 * Create a new instance of a abstraction of {@link ViewContext}.
	 * Use this constructor to set {@link #viewName} and {@link #viewTitle} by
	 * the controller name or via {@link ViewController} annotation
	 */
	public ViewContext()
	{
		animationBuilder = new AnimationBuilder(.4).percentage(.2).interpolator(EaseInterpolator.EASE_OUT);
		loadAnnotations();
	}
	
	/**
	 * Create a new instance of a abstraction of {@link ViewContext}.
	 * This constructor allow manual setup for viewName and viewTitle.
	 * @param viewName the name of the view that will be used for find a corresponding FXML file
	 * @param viewTitle the name that can be use for a title of this view context
	 */
	public ViewContext(String viewName, String viewTitle)
	{
		if(viewName != null) {
			animationBuilder = new AnimationBuilder(.4).percentage(.2).interpolator(EaseInterpolator.EASE_OUT);
			this.viewName = viewName;
			this.viewTitle = viewTitle;	
		} else {
			throw new IllegalArgumentException("viewName must not be null");
		}
	}
	/**
	 * {@inheritDoc}
	 * 
	 * <br><b>NOTE:</b> This method will call the {@link #onCreate(URL, ResourceBundle)} 
	 * implementation
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		onCreate(location, resources);
	}	
	
	public String getViewName() 
	{
		return viewName;
	}

	public String getViewTitle() 
	{
		return viewTitle;
	}
	
	/**
	 * Configure the association of a {@link ViewStage} to the view context. This can be
	 * only configured once.
	 * @param viewStage the initialized {@link ViewStage} to be configured for this context
	 * @throws IllegalArgumentException if the viewStage is null
	 * @throws IllegalStateException if the viewStage is not initialized
	 * @throws IllegalStateException if the viewStage is already configured
	 */
	public void setViewStage(ViewStage viewStage)
	{
		if(viewStage != null) {
			if(viewStage.isInitialized()) {
				if(this.viewStage == null) {
					this.viewStage = viewStage;
				} else {
					throw new IllegalStateException("viewStage is already loaded for this context");
				}
			} else {
				throw new IllegalStateException("viewStage is not initialized");
			}
		} else {
			throw new IllegalArgumentException("viewStage must not be null");
		}
	}
	
	/**
	 * Get the associated {@link ViewStage}. Can be null on early creation of a 
	 * abstraction of this class. When used on a JavaFX Controller will always be
	 * available.
	 * @return the {@link ViewStage} to be configured for this context 
	 */
	public ViewStage getViewStage()
	{
		return viewStage;
	}
	
	/**
	 * Get the current {@link Intent} from {@link ViewStage} that called this view context.
	 * Can be {@literal null} if the {@link StageContext} is the only context loaded.
	 * @return the current {@link Intent} from {@link ViewStage} 
	 */
	public Intent getIntent()
	{
		return viewStage.getIntent();
	}
	
	/**
	 * Get the {@link StageContext} from the associated {@link ViewStage} of this view context.
	 * @return the current associated {@link ViewStage}
	 */
	public StageContext getStageContext()
	{
		return viewStage.getStageContext();
	}
	
	/**
	 * The performing animation that will be executed from {@link StageContext#swapContent(ViewContext)}
	 * to swap to a new view context. Override this method to use a particular animation for the content
	 * {@link Node} to be swapped. The default animation is configured by {@link AnimationBuilder#fadeInLeft(Node)}
	 * with a {@literal 400ms} duration time with the {@link EaseInterpolator#EASE_OUT} {@link Interpolator}. 
	 * @param content that is used for the swap animation
	 */
	protected void swapAnimation(final Node content)
	{
		animationBuilder.fadeInLeft(content).play();
	}
	
	/**
	 * Get the root main {@link Node} for this view context. Need to be implemented.
	 * @return the root main {@link Node} of this view context.
	 */
	public abstract Node getMainNode();
	
	/**
	 * The implementation that is called every time that a new context is called by {@link ViewStage#loadIntent(Intent)}.
	 * If the view context its not cached by {@link ViewStage} this method will be called before {@link #onStart()}. 
	 * Have in mind that the state of JavaFX controller its maintained by the {@link ViewStage} cache, so use this
	 * method to set any initialization configuration that you would use on the typical {@link Initializable#initialize(URL, ResourceBundle)}
	 * method of the JavaFX controller.
	 * @param location used to resolve relative paths for the root object, or {@literal null} if the location is not known.
	 * @param resources used to localize the root object, or {@literal null} if the root object was not localized.
	 */
	protected abstract void onCreate(URL location, ResourceBundle resources);
	
	/**
	 * The implementation that is called on every time that this context is called by {@link ViewStage#loadIntent(Intent)}.
	 * Use this to perform any action every time that a view its swapped.
	 */
	protected abstract void onStart();
	
	//Load any ViewController annotation and set the viewName and viewTitle.
	private void loadAnnotations()
	{
		Annotation annotation = this.getClass().getAnnotation(ViewController.class);
		if(annotation != null) {
			ViewController viewAnnotation = (ViewController) annotation;
			viewName = viewAnnotation.name();
			viewTitle = viewAnnotation.title();
		}
		if(viewName == null || viewName.length() == 0) {
			String contextName = this.getClass().getSimpleName();
			if(contextName.endsWith("Controller")) {
				viewName = upperCamelToLowerUnderscore(contextName.replace("Controller", ""));
			} else {
				throw new IllegalStateException("Class name must ends with \"Controller\" or use ViewController annotation instead");
			}			
		}
		if(viewTitle == null) viewTitle = viewName;
	}
	
	//Convert a UpperCamel to a lower_underscore string
	private String upperCamelToLowerUnderscore(String str)
	{
		return str.replaceAll(UPPER_CAMEL_REGEX, UPPER_CAMEL_REPLACEMENT).toLowerCase();
	}
}
