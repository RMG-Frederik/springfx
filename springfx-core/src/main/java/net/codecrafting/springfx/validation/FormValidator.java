/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.codecrafting.springfx.annotation.ValidationBind;

/**
 * This class is used to help validate JavaFX UI elements. The implementation uses Hibernate Validator to validate
 * a implementation of {@link ValidationModel}, which represents the actual data that could be retrieved from UI. The
 * validation model can contain annotations for set constraints about the attributes on the model. Each
 * {@link FormValidator} has to be a type of implementation of {@link ValidationModel}. The form validator can register a
 * {@link ValidationListener} which act as listener helper for calling {@link ValidationListener#onValidationFailed(List)}
 * passing the errors if failed or {@link ValidationListener#onValidationSucceeded()} if succeeded.
 *
 * @author Lucas Marotta
 * @see #getModel()
 * @see #setValidationListener(ValidationListener)
 * @see #validate()
 *
 * @param <T> The model implementation of {@link ValidationModel}
 */
public class FormValidator<T>
{
	/**
	 * The Hibernate Validator
	 */
	private Validator validator;

	/**
	 * The implementation of validation listener to call for a fail or success validation.
	 */
	private ValidationListener validationListener;

	/**
	 * The implementation of validation model containing the actual data and constraints that will be validated
	 */
	private ValidationModel model;
	private static final Log LOGGER = LogFactory.getLog(FormValidator.class);

	/**
	 * Create a new instance of {@link FormValidator}. The validation is done by the default validator {@link Validation#buildDefaultValidatorFactory()}.
	 * By default this constructor execute a preload validation @see #FormValidator(ValidationModel, boolean).
	 * @param model the implementation of {@link ValidationModel} containing the actual data and constraints that will be validated
	 * @throws IllegalArgumentException if model is null
	 */
	public FormValidator(ValidationModel model)
	{
		this(model, true);
	}
	
	/**
	 * Create a new instance of {@link FormValidator}. The validation is done by the default validator {@link Validation#buildDefaultValidatorFactory()}.
	 * This constructor offers a optional preloadValidation which will indicate to perform a early validation of the model. This
	 * is useful to significant diminish the time for the next validations which helps to not slow down the JavaFX Thread. 
	 * @param model the implementation of {@link ValidationModel} containing the actual data and constraints that will be validated
	 * @param preloadValidation the flag that indicates to perform a early validation or not
	 * @throws IllegalArgumentException if model is null
	 */
	public FormValidator(ValidationModel model, boolean preloadValidation)
	{
		if(model != null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.model = model;
			if(preloadValidation) validator.validate(model);
		} else {
			throw new IllegalArgumentException("model must not be null");
		}
	}

	/**
	 * Set a implementation of {@link ValidationListener} to call for a fail or success validation.
	 * This configuration is optional.
	 * @param validationListener the implementation of {@link ValidationListener}
	 * @throws IllegalArgumentException if validationListener is null
	 */
	public void setValidationListener(final ValidationListener validationListener)
	{
		if(validationListener != null) {
			this.validationListener = validationListener;
		} else {
			throw new IllegalArgumentException("validationListener must not be null");
		}
	}

	/**
	 * Get the implementation of {@link ValidationModel} containing the actual data and constraints that will be validated
	 * @return the implementation of {@link ValidationModel}
	 */
	public ValidationModel getModel()
	{
		return model;
	}

	/**
	 * Perform the validation of a implementation of {@link ValidationModel}. This method will
	 * always return a non {@literal null} {@link ValidationError} list. If there are no
	 * errors the list will be empty. If a {@link ValidationListener} is present this method will call
	 * {@link ValidationListener#onValidationFailed(List)} passing the errors if failed
	 * or {@link ValidationListener#onValidationSucceeded()} if succeeded. This method also will call for the
	 * {@link ValidationModel#setValidation(List)} (before the {@link ValidationListener} methods) as a point to 
	 * update or refresh the JavaFX UI.
	 *
	 * <br><b>NOTE:</b>If the {@link ValidationModel} has {@link ValidationBind} the {@link ValidationError#field} name
	 * will be the value informed on these annotations.
	 * @return a non {@literal null} {@link ValidationError} list
	 */
	public List<ValidationError> validate()
	{
		Set<ConstraintViolation<ValidationModel>> violations = validator.validate(model);
		List<ValidationError> errors = new ArrayList<ValidationError>();
		for (ConstraintViolation<ValidationModel> violation : violations)
		{
			String field = violation.getPropertyPath().toString();
			try {
				Field clsField = model.getClass().getDeclaredField(field);
				if(clsField.isAnnotationPresent(ValidationBind.class)) {
					field = clsField.getAnnotation(ValidationBind.class).value();
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			errors.add(new ValidationError(field, violation.getMessage(), violation.getInvalidValue()));
		}
		model.setValidation(errors);
		if(validationListener != null) {
			if(violations.isEmpty()) {
				validationListener.onValidationSucceeded();
			} else {
				validationListener.onValidationFailed(errors);
			}
		}
		return errors;
	}
}
