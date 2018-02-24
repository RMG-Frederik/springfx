package net.codecrafting.springfx.validation;

public class ValidationError
{
	private String field;
	private String message;
	private Object value;

	public ValidationError(String field)
	{
		this(field, null, null);
	}
	
	public ValidationError(String field, String message, Object value)
	{
		this.field = field;
		this.message = message;
		this.value = value;
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
