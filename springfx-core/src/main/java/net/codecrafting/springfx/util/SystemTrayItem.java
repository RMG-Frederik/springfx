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
package net.codecrafting.springfx.util;

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
