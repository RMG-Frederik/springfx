package net.codecrafting.springfx.validation;

import java.util.List;

public interface ValidationListener 
{
	void onValidationSucceeded();
	void onValidationFailed(List<ValidationError> violations);
}
