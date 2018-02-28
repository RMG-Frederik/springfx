package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import net.codecrafting.springfx.core.SpringFXContext;

/**
 * Annotation meant to be used on the main application class that indicates to the {@link SpringFXContext} 
 * {@link SpringApplicationBuilder} configure a {@link ApplicationContext} with the headless option. Use
 * this annotation with {@literal true} value to use swing elements inside JavaFX.
 * 
 * @author Lucas Marotta
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeadlessApplication 
{
	/**
	 * The value indicating if the application is headless or not.  
	 * @return {@literal true} if the application is headless otherwise returns {@literal false}.
	 */
	boolean value() default true;
}
