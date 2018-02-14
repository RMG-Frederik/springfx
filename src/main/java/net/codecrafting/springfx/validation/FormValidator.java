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

public class FormValidator<T>
{
	protected Validator validator;
	private ValidationListener validationListener;
	private ValidationModel model;
	private static final Log LOGGER = LogFactory.getLog(FormValidator.class);
	
	public FormValidator(ValidationModel model)
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.model = model;
	}
	
	public void setValidationListener(final ValidationListener validationListener)
	{
		if(validationListener != null) {
			this.validationListener = validationListener;
		}	
	}
	
	public ValidationModel getModel()
	{
		return model;
	}
	
	public List<ValidationError> validate()
	{
		Set<ConstraintViolation<ValidationModel>> violations = validator.validate(model);
		List<ValidationError> errors = new ArrayList<ValidationError>();
		for (ConstraintViolation<ValidationModel> violation : violations) 
		{
			ValidationError validationError = new ValidationError();
			String field = violation.getPropertyPath().toString();
			try {
				Field clsField = model.getClass().getDeclaredField(field);
				if(clsField.isAnnotationPresent(ValidationBind.class)) {
					field = clsField.getAnnotation(ValidationBind.class).value();
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			validationError.setField(field);
			validationError.setError(violation.getMessage());
			validationError.setValue(violation.getInvalidValue());
			errors.add(validationError);
		}
		model.update();
		if(validationListener != null) {
			if(violations.isEmpty()) {
				validationListener.onValidationSucceeded();
			} else {
				validationListener.onValidationFailed(errors);
			}
			return null;
		}
		return errors;
	}
	
}
