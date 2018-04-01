/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2018 The SpringFX Contributors
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

import java.util.List;

import net.codecrafting.springfx.context.ViewContext;

/**
 * Interface indicating a listener to the {@link FormValidator}. This interface act as listener helper 
 * for calling {@link ValidationListener#onValidationFailed(List)} passing the errors if failed 
 * or {@link ValidationListener#onValidationSucceeded()} if succeeded. 
 * 
 * You can use your JavaFX controller ({@link ViewContext} implementation) as a validation listener to perform
 * the validation response. Check the login controller example below:
 * 
 * <pre class="code">
 * &#064;ViewController
 * public class LoginController extends ViewContext implements ValidationListener
 * {
 * 	private LoginForm formModel;
 * 	private FormValidator&lt;LoginForm&gt; loginValidator; //LoginForm implements ValidationModel
 * 
 * 	&#064;Override
 * 	public void onCreate(URL location, ResourceBundle resources)
 * 	{
 * 		loginValidator = new FormValidator&lt;LoginForm&gt;(formModel);
 * 		loginValidator.setValidationListener(this);
 * 	}
 * 
 * 	&#064;Override
 * 	public void onStart()
 * 	{
 * 			
 * 	}
 * 		
 * 	&#064;Override
 * 	public void onValidationSucceeded() 
 * 	{
 * 		// success login action here
 * 	}
 * 		
 * 	&#064;Override
 * 	public void onValidationFailed(List&lt;ValidationError&gt; errors) 
 * 	{
 * 		// fail login action here
 * 	}
 * 
 * 	&#064;FXML
 * 	public void login()
 * 	{
 * 		loginValidator.validate();
 * 	}
 * }
 * </pre>
 * 
 * @author Lucas Marotta
 * @see #onValidationSucceeded()
 * @see #onValidationFailed(List)
 */
public interface ValidationListener 
{
	/**
	 * Call for a validation success in {@link FormValidator#validate()}
	 */
	void onValidationSucceeded();
	
	/**
	 * Call for a validation fail in {@link FormValidator#validate()}
	 * @param errors a non {@literal null} {@link ValidationError} list
	 */
	void onValidationFailed(List<ValidationError> errors);
}
