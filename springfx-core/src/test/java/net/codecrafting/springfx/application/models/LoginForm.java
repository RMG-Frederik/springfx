/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
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
package net.codecrafting.springfx.application.models;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
