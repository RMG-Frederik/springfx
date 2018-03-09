/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
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
 * Interface that is used to abstract the model data for {@link FormValidator}. The goal to this
 * interface is to represents the actual data that could be retrieved from UI, and to provide
 * a standard way to receive the validations errors from {@link FormValidator}.
 * 
 * Implementations of this class should be use to contain the constraints and validation annotations
 * of Hibernate Validation library.
 * 
 * <br><b>NOTE:</b> For the implementation of this class it's recommended to have a {@link ViewContext}
 * dependency to access UI elements. For a standard usage of {@link ValidationModel} on that manner, take a look
 * on the {@link FormModel} abstract class.
 * 
 * @author Lucas Marotta
 * @see #setValidation(List)
 */
public interface ValidationModel 
{
	/**
	 * A method that could be used as a standard way to update the UI with the validations made
	 * by {@link FormValidator}. This method is always called by {@link FormValidator#validate()}
	 * before the {@link ValidationListener} methods. 
	 * @param errors is always a non {@literal null} {@link ValidationError} list
	 */
	public abstract void setValidation(List<ValidationError> errors);
}
