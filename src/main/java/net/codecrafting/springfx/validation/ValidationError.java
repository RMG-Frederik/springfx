package net.codecrafting.springfx.validation;

import net.codecrafting.springfx.annotation.ValidationBind;

/**
 * The validation error class containing the following attributes:
 * 
 * <ul>
 * <li>Field: The name of the model attribute or the value of {@link ValidationBind}. 
 * Use this as a reference to the {@link FormModel} attribute name.</li>
 * <li>Message: The message error associated to the field name.</li>
 * <li>Value: The error {@link Object} value associated to the field name.</li>
 * </ul>
 * 
 * @author Lucas Marotta
 * @see #getField()
 * @see #setField(String)
 * @see #getMessage()
 * @see #setMessage(String)
 * @see #getValue()
 * @see #setValue(Object)
 */
public class ValidationError
{
	/**
	 * The name of the model attribute or the value of {@link ValidationBind}
	 */
	private String field;
	
	/**
	 * The message error associated to the field name
	 */
	private String message;
	
	/**
	 * The error {@link Object} value associated to the field name
	 */
	private Object value;

	/**
	 * Creates a new instance of {@link ValidationError}. The field name is required
	 * @param field name of the model attribute or the value of {@link ValidationBind}
	 * @throws IllegalArgumentException if field is null
	 */
	public ValidationError(String field)
	{
		this(field, null, null);
	}
	
	/**
	 * Creates a new instance of {@link ValidationError}. The field name is required
	 * @param field name of the model attribute or the value of {@link ValidationBind}
	 * @param message error associated to the field name
	 * @param value {@link Object} value associated to the field name
	 * @throws IllegalArgumentException if field is null
	 */
	public ValidationError(String field, String message, Object value)
	{
		if(field != null) {
			this.field = field;
			this.message = message;
			this.value = value;	
		} else {
			throw new IllegalArgumentException("field must not be null");
		}
	}
	
	public String getField() 
	{
		return field;
	}

	public void setField(String field) 
	{
		this.field = field;
	}

	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public Object getValue() 
	{
		return value;
	}
	
	public void setValue(Object value) 
	{
		this.value = value;
	}

	@Override
	public String toString() 
	{
		return "ValidationError [field=" + field + ", message=" + message + ", value=" + value + "]";
	}
}
