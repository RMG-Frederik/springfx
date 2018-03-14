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
package net.codecrafting.springfx.context;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

/**
 * The transaction data class that is used to switch {@link ViewContext}s. The idea
 * is inspired by Android Intent class. This class provide some context to the 
 * JavaFX controllers, so that the user could pass some parameters to the new controller
 * or get a reference to the previous controller with the {@link #callerContext}.
 * 
 * @author Lucas Marotta
 * @see #getCallerContext()
 * @see #getViewClass()
 * @see #getResources()
 * @see #getExtra(String)
 * @see #putExtra(String, Object)
 * @see #clearExtra()
 */
public class Intent 
{
	/**
	 * The caller context or the previous controller that is calling for a new {@link ViewContext}
	 */
	private final ViewContext callerContext;
	
	/**
	 * The {@link ViewContext} JavaFX controller class that will be loaded
	 */
	private final Class<? extends ViewContext> viewClass;
	
	/**
	 * Map to hold the parameters of the transaction to the new {@link ViewContext}
	 */
	private final Map<String, Object> extraData;
	
	/**
	 * The resources to be passed to the {@link FXMLLoader} on the {@link ViewStage#loadIntent(Intent)}
	 */
	private final ResourceBundle resources;
	
	/**
	 * Create a new instance of {@link Intent}. The extra data will be initialized
	 * with empty parameters.
	 * 
	 * <br><b>NOTE:</b> The callerContext can be {@literal null}.
	 * 
	 * @param callerContext the previous controller that is calling. Use the instance (this) to
	 * pass the current controller.
	 * @param viewClass the new {@link ViewContext} JavaFX controller class to be loaded
	 * @throws IllegalArgumentException if viewClass is {@literal null}
	 */
	public Intent(ViewContext callerContext, Class<? extends ViewContext> viewClass)
	{
		this(callerContext, viewClass, null);
	}
	
	/**
	 * Create a new instance of {@link Intent} with a {@link ResourceBundle}. 
	 * The extra data will be initialized with empty parameters.
	 * 
	 * <br><b>NOTE:</b> The callerContext and resources can be {@literal null}.
	 * 
	 * @param callerContext the previous controller that is calling. Use the instance (this) to
	 * pass the current controller.
	 * @param viewClass the new {@link ViewContext} JavaFX controller class to be loaded
	 * @param resources the {@link ResourceBundle} to be passed to the new {@link ViewContext}
	 * @throws IllegalArgumentException if viewClass is {@literal null}
	 */
	public Intent(ViewContext callerContext, Class<? extends ViewContext> viewClass, ResourceBundle resources)
	{
		if(viewClass != null) {
			this.callerContext = callerContext;
			this.viewClass = viewClass;
			this.resources = resources;
			extraData = new HashMap<String, Object>();	
		} else {
			throw new IllegalArgumentException("viewClass must not be null");
		}
	}

	public ViewContext getCallerContext() 
	{
		return callerContext;
	}

	public Class<? extends ViewContext> getViewClass() 
	{
		return viewClass;
	}
	
	public ResourceBundle getResources()
	{
		return resources;
	}
	
	/**
	 * Set a extra parameter to be accessible for the new controller to be loaded.
	 * @param field the name reference to find the parameter
	 * @param value the value of the name reference
	 * @throws IllegalArgumentException if field is null
	 * @throws IllegalArgumentException if field is empty
	 */
	public void putExtra(String field, Object value)
	{
		if(field != null) {
			if(!field.isEmpty()) {
				extraData.put(field, value);	
			} else {
				throw new IllegalArgumentException("field must not be empty");
			}
		} else {
			throw new IllegalArgumentException("field must not be null");
		}
	}
	
	/**
	 * Find the extra parameter that match the field
	 * @param field he name reference to find the parameter
	 * @return the value of the name reference
	 */
	public Object getExtra(String field)
	{
		return extraData.get(field);
	}
	
	/**
	 * Clear all parameters previously added
	 */
	public void clearExtra()
	{
		extraData.clear();
	}
}
