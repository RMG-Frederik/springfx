package net.codecrafting.springfx.demo.form;

import com.jfoenix.validation.base.ValidatorBase;

public class JFXValidator extends ValidatorBase 
{
	private boolean valid;
	
	public JFXValidator(boolean valid)
	{
		this.valid = valid;
	}
	
	public boolean isValid() 
	{
		return valid;
	}

	public void setValid(boolean valid) 
	{
		this.valid = valid;
	}

	@Override
	protected void eval() 
	{
		hasErrors.set(!valid);
	}
}
