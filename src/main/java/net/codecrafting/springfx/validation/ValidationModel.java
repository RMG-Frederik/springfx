package net.codecrafting.springfx.validation;

import java.util.List;

import net.codecrafting.springfx.controls.ViewContext;

/**
 * Interface that is used to abstract the model data for {@link FormValidator}. The goal to this
 * interface is to represents the actual data that could be retrieved from UI, and to provide
 * a standard way to receive the validations errors from {@link FormValidator}.
 * 
 * Implementations of this class should be use to contain the constraints and validation annotations
 * of Hibernate Validation library.
 * 
 * <br><b>NOTE:</b> For the implementation of this class it's recommended to have a {@link ViewContext}
 * dependency to access UI elements. For a standard usage of {@link ValidationModel} on that manner, take a look
 * on the {@link FormModel} abstract class.
 * 
 * @author Lucas Marotta
 * @see #setValidation(List)
 */
public interface ValidationModel 
{
	/**
	 * A method that could be used as a standard way to update the UI with the validations made
	 * by {@link FormValidator}. This method is always called by {@link FormValidator#validate()}
	 * before the {@link ValidationListener} methods. 
	 * @param errors is always a non {@literal null} {@link ValidationError} list
	 */
	public abstract void setValidation(List<ValidationError> errors);
}
