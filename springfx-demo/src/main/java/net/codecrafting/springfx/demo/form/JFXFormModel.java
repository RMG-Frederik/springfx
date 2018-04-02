package net.codecrafting.springfx.demo.form;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXTimePicker;

import javafx.scene.Node;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.context.ViewContext;
import net.codecrafting.springfx.validation.FormModel;
import net.codecrafting.springfx.validation.ValidationError;

public class JFXFormModel extends FormModel
{
	private static final Log LOGGER = LogFactory.getLog(JFXFormModel.class);
	
	public JFXFormModel(ViewContext context) 
	{
		super(context);
	}
	
	@Override
	public void setValuesFromForm()
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			Field modelField = getModelField(field);
			if(modelField != null) {
				Node fieldNode = getContextFieldNode(field);
				if(fieldNode != null) {
					Class<?> fieldType = field.getType();
					if(fieldType.isAssignableFrom(JFXTimePicker.class))
						setValueToModelField(modelField, ((JFXTimePicker) fieldNode).getValue());
				}
			}
		}
		super.setValuesFromForm();
	}

	@Override
	public void setValuesToForm()
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object modelFieldValue = getValueFromModelField(field);
			if(modelFieldValue != null) {
				Node fieldNode = getContextFieldNode(field);
				if(fieldNode != null) {
					Class<?> fieldType = field.getType();
					try {
						if(fieldType.isAssignableFrom(JFXTimePicker.class)) 
							((JFXTimePicker) fieldNode).setValue((LocalTime) modelFieldValue);
					} catch(Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
		super.setValuesToForm();
	}
	
	public void resetValidation()
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(ValidationBind.class) && IFXTextInputControl.class.isAssignableFrom(field.getType())) {
				IFXTextInputControl node = (IFXTextInputControl) context.getMainNode().lookup("#"+field.getName());
				JFXValidator validator = new JFXValidator(true);
				node.getValidators().clear();
				node.getValidators().add(validator);
				node.validate();
			}
		}
	}

	@Override
	public void setValidation(List<ValidationError> errors) 
	{
		resetValidation();
		List<String> hist = new ArrayList<String>();
		for (ValidationError validationError : errors) 
		{
			if(!hist.contains(validationError.getField())) {
				Node node = context.getMainNode().lookup("#"+validationError.getField());
				if(node != null && IFXTextInputControl.class.isAssignableFrom(node.getClass())) {
					JFXValidator validator = new JFXValidator(false);
					IFXTextInputControl field = (IFXTextInputControl) node;
					validator.autosize();
					validator.setMessage(validationError.getMessage());
					field.getValidators().clear();
					field.getValidators().add(validator);
					field.validate();
				}	
			}
			hist.add(validationError.getField());
		}		
	}
	
}
