package net.codecrafting.springfx.validation;

import java.util.List;

public interface ValidationModel 
{
	public abstract void setValidation(List<ValidationError> errors);
}
