package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.codecrafting.springfx.context.ViewContext;
import net.codecrafting.springfx.validation.FormModel;
import net.codecrafting.springfx.validation.FormValidator;
import net.codecrafting.springfx.validation.ValidationModel;

/**
 * Annotation meant to be used for the validation process with the {@link FormValidator} and {@link FormModel}.
 * Use this annotation to indicate that a field from {@link ViewContext} has to be validated. This annotation
 * also is used to bind the name of the {@link ViewContext} (or vice versa) to the corresponding implementation 
 * {@link ValidationModel} attribute value.
 * @author Lucas Marotta
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationBind 
{
	/**
	 * The value indicating the name of field to be binded to the {@link ValidationModel}
	 * or {@link ViewContext}.
	 * @return the attribute name to be binded
	 */
	String value() default "";
}
