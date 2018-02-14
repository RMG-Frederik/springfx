package net.codecrafting.springfx.controls;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Intent 
{
	private ViewContext callerContext;
	private Class<? extends ViewContext> viewClass;
	private Map<String, Serializable> extraData;
	
	public Intent(ViewContext callerContext, Class<? extends ViewContext> viewClass)
	{
		this.callerContext = callerContext;
		this.viewClass = viewClass;
		extraData = new HashMap<String, Serializable>();
	}

	public ViewContext getCallerContext() 
	{
		return callerContext;
	}

	public Class<? extends ViewContext> getViewClass() 
	{
		return viewClass;
	}
	
	public void putExtra(String field, Serializable value)
	{
		extraData.put(field, value);
	}
	
	public Serializable getExtra(String field)
	{
		return extraData.get(field);
	}
	
	public void clearExtra()
	{
		extraData.clear();
	}
}
