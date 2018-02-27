package net.codecrafting.springfx.validation;

import java.util.List;

import net.codecrafting.springfx.controls.ViewContext;

public abstract class FormModel implements ValidationModel
{
	protected ViewContext context;

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
	
	public void setValuesFromForm()
	{
		
	}
	
	public void setValuesToForm()
	{
		
	}
}
