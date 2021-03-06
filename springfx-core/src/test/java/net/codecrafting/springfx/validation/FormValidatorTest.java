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
package net.codecrafting.springfx.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import net.codecrafting.springfx.application.models.LoginForm;

public class FormValidatorTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void validationModelMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("model must not be null");
		new FormValidator<ValidationModel>(null);
	}
	
	@Test
	public void instantiation()
	{
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel);
		assertNotNull("validation model is null", validator.getModel());
	}
	
	@Test
	//Don't know how to proper test this
	public void instantiationWithPreloadValidation()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("model must not be null");
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel, true);
		assertNotNull("validation model is null", validator.getModel());
		validator = new FormValidator<ValidationModel>(mockModel, false);
		new FormValidator<ValidationModel>(null, true);
	}
	
	@Test
	public void validatorListenerMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("validationListener must not be null");
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel);
		validator.setValidationListener(null);
	}
	
	@Test
	public void validatorListenerCall()
	{
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel);
		ValidationListener mockListener = Mockito.mock(ValidationListener.class);
		validator.setValidationListener(mockListener);
		validator.validate();
		Mockito.verify(mockListener, Mockito.times(1)).onValidationSucceeded();
	}
	
	@Test
	public void validatorWithoutListener()
	{
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel);
		assertEquals(0, validator.validate().size());	
	}
	
	@Test
	public void validatorWithListener()
	{
		LoginForm model = new LoginForm();
		model.setPass("123");
		model.setUser("springfx");
		FormValidator<LoginForm> validator = new FormValidator<LoginForm>(model);
		ValidationListener mockListener = Mockito.mock(ValidationListener.class);
		validator.setValidationListener(mockListener);
		validator.validate();
		Mockito.verify(mockListener, Mockito.times(1)).onValidationSucceeded();
	}
	
	@Test
	public void validatorWithErrorsWithoutListener()
	{
		LoginForm model = new LoginForm();
		model.setPass("");
		model.setUser("springfx");
		FormValidator<LoginForm> validator = new FormValidator<LoginForm>(model);
		List<ValidationError> errors = validator.validate();
		assertEquals(1, errors.size());
		assertEquals(LoginForm.PASS_FIELD_NAME, errors.get(0).getField());
		assertEquals(LoginForm.PASS_FIELD_MSG, errors.get(0).getMessage());
		assertEquals("", errors.get(0).getValue());
	}
	
	@Test
	public void validatorWithErrorsWithListener()
	{
		LoginForm model = new LoginForm();
		model.setPass("");
		model.setUser("springfx");
		model.setEmail("test");
		FormValidator<LoginForm> validator = new FormValidator<LoginForm>(model);
		ValidationListener mockListener = Mockito.mock(ValidationListener.class);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			List<ValidationError> errors = invocation.getArgument(0);
			assertNotNull(errors);
			assertEquals(2, errors.size());
			List<ValidationError> expectedErrors = Arrays.asList(new ValidationError(LoginForm.PASS_FIELD_NAME, LoginForm.PASS_FIELD_MSG, ""),
					new ValidationError("email", LoginForm.EMAIL_FIELD_MSG, "test"));
			int expectedErrorsSize = expectedErrors.size();
			for (ValidationError expectedError : expectedErrors) {
				for (ValidationError error : errors) {
					if(error.getField().equals(expectedError.getField())) {
						assertEquals(expectedError.getMessage(), error.getMessage());
						assertEquals(expectedError.getValue(), error.getValue());
						expectedErrorsSize--;
						break;
					}
				}
			}
			assertEquals("There are missing errors", expectedErrorsSize, 0);
			return null;
		}).when(mockListener).onValidationFailed(ArgumentMatchers.any());
		validator.setValidationListener(mockListener);
		validator.validate();
		Mockito.verify(mockListener, Mockito.times(1)).onValidationFailed(ArgumentMatchers.any());
	}

	@Test
	public void validatorUpdateCall()
	{
		ValidationModel mockModel = Mockito.mock(ValidationModel.class);
		FormValidator<ValidationModel> validator = new FormValidator<ValidationModel>(mockModel);
		validator.validate();
		Mockito.verify(mockModel, Mockito.times(1)).setValidation(ArgumentMatchers.any());
	}
}
