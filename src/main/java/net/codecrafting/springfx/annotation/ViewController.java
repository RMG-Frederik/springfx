package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.codecrafting.springfx.context.ViewContext;

/**
 * Annotation meant to be used by a {@link ViewContext} indicating that the class is a Spring {@link Component}.
 * Can be also used to customize the {@link ViewContext} viewName and viewTitle. If the name or the title 
 * was not informed the {@link ViewContext} will use the name of the superclass to define these values. This annotation
 * has a Spring {@link Lazy} annotation to delay the initialization of the controller bean.
 * 
 * @author Lucas Marotta
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Lazy
public @interface ViewController 
{
	/**
	 * The view name value to be informed to {@link ViewContext}
	 * @return the view name {@link String} value
	 */
	String name() default "";
	
	/**
	 * The view title name to be informed to {@link ViewContext}
	 * @return the view title name {@link String} vlaue
	 */
	String title() default "";
}
