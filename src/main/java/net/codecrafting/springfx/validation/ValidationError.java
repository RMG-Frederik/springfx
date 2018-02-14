package net.codecrafting.springfx.validation;

public class ValidationError
{
	private String field;
	private String error;
	private Object value;

	public String getField() 
	{
		return field;
	}

	public void setField(String field) 
	{
		this.field = field;
	}

	public String getError()
	{
		return error;
	}
	
	public void setError(String error) 
	{
		this.error = error;
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
		return "ValidationError [field=" + field + ", error=" + error + ", value=" + value + "]";
	}
}
