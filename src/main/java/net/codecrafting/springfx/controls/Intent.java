package net.codecrafting.springfx.controls;

import java.util.HashMap;
import java.util.Map;

/**
 * The transaction data class that is used to switch {@link ViewContext}s. The idea
 * is inspired by Android Intent class. This class provide some context to the 
 * JavaFX controllers, so that the user could pass some parameters to the new controller
 * or get a reference to the previous controller with the {@link #callerContext}.
 * 
 * @author Lucas Marotta
 * @see #getCallerContext()
 * @see #getViewClass()
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
	 * Create a new instance of {@link Intent}. The extra data will be initialized
	 * with empty parameters.
	 * 
	 * <br><b>NOTE:</b> The callerContext and the viewClass can be {@literal null}.
	 * 
	 * @param callerContext the previous controller that is calling. Use the instance (this) to
	 * pass the current controller.
	 * @param viewClass the new {@link ViewContext} JavaFX controller class to be loaded
	 */
	public Intent(ViewContext callerContext, Class<? extends ViewContext> viewClass)
	{
		this.callerContext = callerContext;
		this.viewClass = viewClass;
		extraData = new HashMap<String, Object>();
	}

	public ViewContext getCallerContext() 
	{
		return callerContext;
	}

	public Class<? extends ViewContext> getViewClass() 
	{
		return viewClass;
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
