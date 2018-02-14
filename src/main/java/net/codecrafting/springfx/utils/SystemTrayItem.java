package net.codecrafting.springfx.utils;

import java.awt.event.ActionListener;

public class SystemTrayItem 
{
	private String label;
	private ActionListener action;

	public SystemTrayItem(String label, ActionListener action)
	{
		super();
		this.label = label;
		this.action = action;
	}
	
	public String getLabel() 
	{
		return label;
	}
	
	public void setLabel(String label) 
	{
		this.label = label;
	}
	
	public ActionListener getAction() 
	{
		return action;
	}
	
	public void setAction(ActionListener action) 
	{
		this.action = action;
	}
	
	@Override
	public String toString() 
	{
		return "SystemTrayItem [label=" + label + ", action=" + action + "]";
	}
}
