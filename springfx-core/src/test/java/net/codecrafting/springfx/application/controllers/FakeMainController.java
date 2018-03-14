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
package net.codecrafting.springfx.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.context.StageContext;

public class FakeMainController extends StageContext
{
	@FXML
	private AnchorPane mainNode;
	
	private String viewStageTitle;

	@Override
	public void setViewStageTitle(String title) 
	{
		viewStageTitle = title;
	}
	
	public String getViewStageTitle()
	{
		return viewStageTitle;
	}

	@Override
	public AnchorPane getMainNode() 
	{
		return mainNode;
	}

	@Override
	protected void onStart() 
	{
		
	}

	@Override
	protected void onCreate() 
	{
		
	}
}