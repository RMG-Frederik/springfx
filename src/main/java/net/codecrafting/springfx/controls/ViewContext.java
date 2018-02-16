package net.codecrafting.springfx.controls;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import net.codecrafting.springfx.animation.AnimationBuilder;
import net.codecrafting.springfx.animation.EaseInterpolator;
import net.codecrafting.springfx.annotation.ViewController;

public abstract class ViewContext implements Initializable
{
	protected String viewName;
	protected String viewTitle;
	protected ViewStage viewStage;
	private final AnimationBuilder animationBuilder = new AnimationBuilder(.4).percentage(.2).interpolator(EaseInterpolator.EASE_OUT);
	private static final String UPPER_CAMEL_REGEX = "([a-z])([A-Z]+)";
	private static final String UPPER_CAMEL_REPLACEMENT = "$1_$2";
	
	public ViewContext()
	{
		loadAnnotations();
	}
	
	public ViewContext(String viewName, String viewTitle)
	{
		this.viewName = viewName;
		this.viewTitle = viewTitle;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		onCreate(location, resources);
	}	
	
	public String getViewName() 
	{
		return viewName;
	}

	public void setViewName(String viewName) 
	{
		this.viewName = viewName;
	}

	public String getViewTitle() 
	{
		return viewTitle;
	}
	
	public void setViewStage(ViewStage viewStage)
	{
		if(this.viewStage == null) {
			this.viewStage = viewStage;
		} else {
			throw new RuntimeException("ViewStage is already loaded for this context");
		}
	}
	
	public ViewStage getViewStage()
	{
		return viewStage;
	}
	
	public Intent getIntent()
	{
		return viewStage.getIntent();
	}
	
	protected void swapAnimation(final Node content)
	{
		animationBuilder.fadeInLeft(content).play();
	}
	
	public abstract Node getMainNode();
	
	protected abstract void onStart();
	
	protected abstract void onCreate(URL location, ResourceBundle resources);
	
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
				throw new RuntimeException("Class name must ends with \"Controller\" or use ViewController annotation instead");
			}			
		}
		if(viewTitle == null) viewTitle = viewName;
	}
	
	private String upperCamelToLowerUnderscore(String str)
	{
		return str.replaceAll(UPPER_CAMEL_REGEX, UPPER_CAMEL_REPLACEMENT).toLowerCase();
	}
}
