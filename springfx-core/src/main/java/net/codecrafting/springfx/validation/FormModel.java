/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2018 The SpringFX Contributors
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
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.context.ViewContext;

/**
 * This class is used to abstract a JavaFX form validation with {@link ValidationModel}. The goal is to provide 
 * a standard way to retrieve and set values from both the model and the form elements contained on the 
 * associated {@link ViewContext}. This class has support for the following JavaFX classes or superclasses controls:
 * 
 * <ul>
 * <li>{@link TextField}</li>
 * <li>{@link PasswordField}</li>
 * <li>{@link CheckBox}</li>
 * <li>{@link ChoiceBox}</li>
 * <li>{@link ColorPicker}</li>
 * <li>{@link ComboBox}</li>
 * <li>{@link DatePicker}</li>
 * <li>{@link RadioButton}</li>
 * <li>{@link TextArea}</li>
 * <li>{@link Slider}</li>
 * </ul>
 * 
 * To use this class certain conventions has to be fulfilled for a correct operation:
 * 
 * <ul>
 * <li>This class only will manage form elements and associated model attributes that has a {@link ValidationBind} annotation</li>
 * <li>Non initialized form elements or model attributes will be ignored</li>
 * <li>Form elements that has empty {@link ValidationBind} value will match models with the same attribute name</li>
 * <li>The model attributes has to be compatible with the JavaFX controls present on the {@link ViewContext}</li>
 * <li>Form elements that aren't included on the supported JavaFX controls list will be ignored</li>
 * </ul>
 * 
 * @author Lucas Marotta
 * @see #getContext()
 * @see #setValuesFromForm()
 * @see #setValuesToForm()
 * @see #getModelField(Field)
 * @see #getContextFieldNode(Field)
 * @see #getValueFromModelField(Field)
 */
public abstract class FormModel implements ValidationModel
{
	/**
	 * The associated JavaFX controller context to retrieve form elements
	 */
	protected ViewContext context;
	private static final Log LOGGER = LogFactory.getLog(FormModel.class);

	/**
	 * Create a new instance of {@link ViewContext}. The {@link ViewContext} is required for 
	 * retrieve the form elements from the JavaFX controller.
	 * @param context associated JavaFX controller {@link ViewContext}.
	 * @throws IllegalArgumentException if context is null
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void setValidation(List<ValidationError> errors);
	
	/**
     * This method is called at the end of {@link #setValuesFromForm()} after the model values 
     * are filled. Use this method to post process model data retrieved from {@link ViewContext} elements.
     * 
     * <p>
     * The implementation of this method provided by {@link FormModel} does nothing.
     * </p>
	 */
	protected void postUpdateValues(){}
	
	/**
     * This method is called at the top of {@link #setValuesToForm()} before the {@link ViewContext} form
     * elements values are filled. Use this method to pre process model data to be passed to 
     * {@link ViewContext} elements.
     * 
     * <p>
     * The implementation of this method provided by {@link FormModel} does nothing.
     * </p>
	 */
	protected void preUpdateValues(){}
	
	/**
	 * This method set the values of the model attributes from the {@link ViewContext} form elements containing
	 * {@link ValidationBind} annotation. The value present on the {@link ValidationBind} is used to find a 
	 * corresponding attribute with that name. If the annotation has a empty value the name of the form element 
	 * attribute will be used instead.
	 */
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
					if(TextField.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((TextField) fieldNode).getText());
					
					} else if(PasswordField.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((PasswordField) fieldNode).getText());
					
					} else if(CheckBox.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((CheckBox) fieldNode).isSelected());
					
					} else if(ChoiceBox.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((ChoiceBox<Object>) fieldNode).getValue());
					
					} else if(ColorPicker.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((ColorPicker) fieldNode).getValue());
					
					} else if(ComboBox.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((ComboBox<?>) fieldNode).getValue());
					
					} else if(DatePicker.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((DatePicker) fieldNode).getValue());
					
					} else if(RadioButton.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((RadioButton) fieldNode).isSelected());
						
					} else if(ToggleButton.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((ToggleButton) fieldNode).isSelected());
					
					} else if(TextArea.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((TextArea) fieldNode).getText());
					
					} else if(Slider.class.isAssignableFrom(fieldType)) {
						setValueToModelField(modelField, ((Slider) fieldNode).getValue());
					}
				}
			}
		}
		postUpdateValues();
	}
	
	/**
	 * This method set values from the model attributes to the the {@link ViewContext} form elements containing
	 * {@link ValidationBind} annotation. The value present on the {@link ValidationBind} is used to find a 
	 * corresponding attribute with that name. If the annotation has a empty value the name of the form element 
	 * attribute will be used instead.
	 */
	@SuppressWarnings("unchecked")
	public void setValuesToForm()
	{
		preUpdateValues();
		Field[] fields = context.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object modelFieldValue = getValueFromModelField(field);
			if(modelFieldValue != null) {
				Node fieldNode = getContextFieldNode(field);
				if(fieldNode != null) {
					Class<?> fieldType = field.getType();
					try {
						if(TextField.class.isAssignableFrom(fieldType)) {
							((TextField) fieldNode).setText((String) modelFieldValue);
						
						} else if(PasswordField.class.isAssignableFrom(fieldType)) {
							((PasswordField) fieldNode).setText((String) modelFieldValue);
						
						} else if(CheckBox.class.isAssignableFrom(fieldType)) {
							((CheckBox) fieldNode).setSelected((Boolean) modelFieldValue);
						
						} else if(ChoiceBox.class.isAssignableFrom(fieldType)) {
							((ChoiceBox<Object>) fieldNode).setValue(modelFieldValue);
						
						} else if(ColorPicker.class.isAssignableFrom(fieldType)) {
							((ColorPicker) fieldNode).setValue((Color) modelFieldValue);
						
						} else if(ComboBox.class.isAssignableFrom(fieldType)) {
							((ComboBox<Object>) fieldNode).setValue(modelFieldValue);
						
						} else if(DatePicker.class.isAssignableFrom(fieldType)) {
							((DatePicker) fieldNode).setValue((LocalDate) modelFieldValue);
						
						} else if(RadioButton.class.isAssignableFrom(fieldType)) {
							((RadioButton) fieldNode).setSelected((Boolean) modelFieldValue);
							
						} else if(ToggleButton.class.isAssignableFrom(fieldType)) {
							((ToggleButton) fieldNode).setSelected((Boolean) modelFieldValue);
						
						} else if(TextArea.class.isAssignableFrom(fieldType)) {
							((TextArea) fieldNode).setText((String) modelFieldValue);
						
						} else if(Slider.class.isAssignableFrom(fieldType)) {
							((Slider) fieldNode).setValue((Double) modelFieldValue);
						}
					} catch(Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	/**
	 * This method get the model {@link Field} from this class (or superclass) by looking the {@link ValidationBind} 
	 * present at a {@link Field} from {@link ViewContext}. If the {@link ValidationBind} has empty value
	 * the {@link ViewContext} {@link Field#getName()} will be used instead. 
	 * @param contextField the {@link Field} from {@link ViewContext}
	 * @return a {@link Field} from the instance of this superclass. Can be {@literal null} if the {@link ValidationBind}
	 * is not present, if the field was not found or the actual field is not initialized.
	 */
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
	
	/**
	 * This method set a value to this superclass instance {@link Field}. The value is set directly to the attribute
	 * and does not use any get/set methods. Private and protected attributes are also covered. The method will fail
	 * (without throwing a exception) due to possible {@link IllegalAccessException} exception.
	 * @param fieldName the name of the {@link Field} of this superclass
	 * @param value any {@link Object} value to be set at a field instance {@link Field} of this superclass
	 * @throws IllegalArgumentException if fieldName is null
	 */
	protected void setValueToModelField(String fieldName, Object value)
	{
		if(fieldName != null) {
			Field field = ReflectionUtils.findField(getClass(), fieldName);
			if(field != null) setValueToModelField(field, value);
		} else {
			throw new IllegalArgumentException("fieldName must not be null");
		}
	}	
	
	/**
	 * This method set a value to this superclass instance {@link Field}. The value is set directly to the attribute
	 * and does not use any get/set methods. Private and protected attributes are also covered. The method will fail
	 * (without throwing a exception) due to possible {@link IllegalAccessException} exception.
	 * @param field a instance {@link Field} of this superclass
	 * @param value any {@link Object} value to be set at a field instance {@link Field} of this superclass
	 */
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
	
	/**
	 * This method get a {@link Node} value from a {@link Field} of the {@link ViewContext}. The value is get directly 
	 * from the attribute and does not use any get/set methods. Private and protected methods are also covered. 
	 * @param fieldName the {@link Field} name from {@link ViewContext} class
	 * @return a {@link Field} from the {@link ViewContext}. Can be {@literal null} if the field value isn't a {@link Node}
	 * class or superclass or due to possible {@link IllegalAccessException} exception.
	 * @throws IllegalArgumentException if fieldName is null
	 */
	protected Node getContextFieldNode(String fieldName)
	{
		if(fieldName != null) {
			Field field = ReflectionUtils.findField(context.getClass(), fieldName);
			if(field != null) return getContextFieldNode(field);
			return null;
		} else {
			throw new IllegalArgumentException("fieldName must not be null");
		}
	}	
	
	/**
	 * This method get a {@link Node} value from a {@link Field} of the {@link ViewContext}. The value is get directly 
	 * from the attribute and does not use any get/set methods. Private and protected methods are also covered. 
	 * @param field a {@link ViewContext} {@link Field}
	 * @return a {@link Field} from the {@link ViewContext}. Can be {@literal null} if the field value isn't a {@link Node}
	 * class or superclass or due to possible {@link IllegalAccessException} exception.
	 */
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
	
	/**
	 * This method get a value from this superclass instance by looking the {@link ValidationBind} present 
	 * at a {@link Field} from {@link ViewContext}. If the {@link ValidationBind} has empty value
	 * the {@link ViewContext} {@link Field#getName()} will be used instead. The value is get directly 
	 * from the attribute and does not use any get/set methods. Private and protected methods are also covered.
	 * @param contextField the {@link Field} from {@link ViewContext}
	 * @return a {@link Object} value of a {@link Field} from this superclass.  Can be {@literal null} if the {@link ValidationBind}
	 * is not present, if the field was not found, if the actual field is not initialized or due to possible 
	 * {@link IllegalAccessException} exception.
	 */
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
