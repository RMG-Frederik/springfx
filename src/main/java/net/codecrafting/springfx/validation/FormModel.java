package net.codecrafting.springfx.validation;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.controls.ViewContext;

public abstract class FormModel implements ValidationModel
{
	protected ViewContext context;
	private static final Log LOGGER = LogFactory.getLog(FormModel.class);

	public FormModel(ViewContext context)
	{
		if(context != null) {
			this.context = context;
		} else {
			throw new IllegalArgumentException("context must not be null");
		}
	}
	
	public ViewContext getContext()
	{
		return context;
	}
	
	public abstract void setValidation(List<ValidationError> errors);
	
	public abstract void resetValidation();
	
	@SuppressWarnings("unchecked")
	public void setValuesFromForm()
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			Field modelField = getModelField(field);
			if(modelField != null) {
				Node fieldNode = getContextFieldNode(field);
				if(fieldNode != null) {
					Class<?> fieldType = field.getType();
					if(fieldType.isAssignableFrom(TextField.class)) {
						setValueToModelField(modelField, ((TextField) fieldNode).getText());
					
					} else if(fieldType.isAssignableFrom(PasswordField.class)) {
						setValueToModelField(modelField, ((PasswordField) fieldNode).getText());
					
					} else if(fieldType.isAssignableFrom(CheckBox.class)) {
						setValueToModelField(modelField, ((CheckBox) fieldNode).isSelected());
					
					} else if(fieldType.isAssignableFrom(ChoiceBox.class)) {
						setValueToModelField(modelField, ((ChoiceBox<Object>) fieldNode).getValue());
					
					} else if(fieldType.isAssignableFrom(ColorPicker.class)) {
						setValueToModelField(modelField, ((ColorPicker) fieldNode).getValue());
					
					} else if(fieldType.isAssignableFrom(ComboBox.class)) {
						setValueToModelField(modelField, ((ComboBox<Object>) fieldNode).getValue());
					
					} else if(fieldType.isAssignableFrom(DatePicker.class)) {
						setValueToModelField(modelField, ((DatePicker) fieldNode).getValue());
					
					} else if(fieldType.isAssignableFrom(RadioButton.class)) {
						setValueToModelField(modelField, ((RadioButton) fieldNode).isSelected());
					
					} else if(fieldType.isAssignableFrom(TextArea.class)) {
						setValueToModelField(modelField, ((TextArea) fieldNode).getText());
					
					} else if(fieldType.isAssignableFrom(Slider.class)) {
						setValueToModelField(modelField, ((Slider) fieldNode).getValue());
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setValuesToForm()
	{
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object modelFieldValue = getValueFromModelField(field);
			if(modelFieldValue != null) {
				Node fieldNode = getContextFieldNode(field);
				if(fieldNode != null) {
					Class<?> fieldType = field.getType();
					if(fieldType.isAssignableFrom(TextField.class)) {
						((TextField) fieldNode).setText((String) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(PasswordField.class)) {
						((PasswordField) fieldNode).setText((String) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(CheckBox.class)) {
						((CheckBox) fieldNode).setSelected((Boolean) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(ChoiceBox.class)) {
						((ChoiceBox<Object>) fieldNode).setValue(modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(ColorPicker.class)) {
						((ColorPicker) fieldNode).setValue((Color) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(ComboBox.class)) {
						((ComboBox<Object>) fieldNode).setValue(modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(DatePicker.class)) {
						((DatePicker) fieldNode).setValue((LocalDate) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(RadioButton.class)) {
						((RadioButton) fieldNode).setSelected((Boolean) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(TextArea.class)) {
						((TextArea) fieldNode).setText((String) modelFieldValue);
					
					} else if(fieldType.isAssignableFrom(Slider.class)) {
						((Slider) fieldNode).setValue((Double) modelFieldValue);
					}
				}
			}
		}
	}
	
	protected Field getModelField(Field contextField)
	{
		if(contextField.isAnnotationPresent(ValidationBind.class)) {
			String fieldName = contextField.getAnnotation(ValidationBind.class).value();
			if(!fieldName.isEmpty()) {
				return ReflectionUtils.findField(getClass(), fieldName);
			} else {
				return ReflectionUtils.findField(getClass(), contextField.getName());
			}		
		}
		return null;
	}
	
	protected void setValueToModelField(Field field, Object value)
	{
		try {
			boolean access = field.isAccessible();
			field.setAccessible(true);
			ReflectionUtils.setField(field, this, value);
			field.setAccessible(access);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}		
	}
	
	protected Node getContextFieldNode(Field field)
	{
		try {
			boolean access = field.isAccessible();
			field.setAccessible(true);
			Node fieldNode = (Node) ReflectionUtils.getField(field, context);
			field.setAccessible(access);
			return fieldNode;
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
	
	protected Object getValueFromModelField(Field contextField)
	{
		Field field = getModelField(contextField);
		if(field != null) {
			try {
				boolean access = field.isAccessible();
				field.setAccessible(true);
				Object value = ReflectionUtils.getField(field, this);
				field.setAccessible(access);
				return value;
			} catch(Exception e) {
				LOGGER.error(e.getMessage(), e);
			}	
		}
		return null;
	}
}
