package net.codecrafting.springfx.application.models;

import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.validation.ValidationError;
import net.codecrafting.springfx.validation.ValidationModel;

public class LoginForm implements ValidationModel
{
	public static final String PASS_FIELD_NAME = "passField";
	public static final String PASS_FIELD_MSG = "The field pass must not be empty";
	public static final String USER_FIELD_MSG = "The field user must not be empty";
	public static final String EMAIL_FIELD_MSG = "The field email is not valid";
	
	@NotEmpty(message=PASS_FIELD_MSG)
	@ValidationBind(PASS_FIELD_NAME)
	private String pass;
	
	@NotEmpty(message=USER_FIELD_MSG)
	private String user;
	
	@Email(message=EMAIL_FIELD_MSG)
	private String email;
	
	public String getPass() 
	{
		return pass;
	}
	
	public void setPass(String pass) 
	{
		this.pass = pass;
	}
	
	public String getUser() 
	{
		return user;
	}

	public void setUser(String user) 
	{
		this.user = user;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public void setEmail(String email) 
	{
		this.email = email;
	}

	@Override
	public void setValidation(List<ValidationError> errors) 
	{

	}
}
