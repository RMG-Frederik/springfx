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
 * @param <T> The validator implementation of {@link ValidationModel}
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
	 * Create a new instance of {@link FormValidator}. The validation is done by the default validator {@link Validation#buildDefaultValidatorFactory()}
	 * @param model the implementation of {@link ValidationModel} containing the actual data and constraints that will be validated
	 * @throws IllegalArgumentException if model is null
	 */
	public FormValidator(ValidationModel model)
	{
		if(model != null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.model = model;	
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
	 * {@link ValidationModel#update()} as a point to update or refresh the JavaFX UI.
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
		model.update();
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
