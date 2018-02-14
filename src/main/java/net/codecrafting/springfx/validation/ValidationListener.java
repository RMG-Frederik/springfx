package net.codecrafting.springfx.validation;

import java.util.List;

public interface ValidationListener 
{
	public void onValidationSucceeded();
	public void onValidationFailed(List<ValidationError> violations);
}
