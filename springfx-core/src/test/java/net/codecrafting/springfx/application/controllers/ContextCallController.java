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
package net.codecrafting.springfx.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.context.StageContext;

@ViewController(name="main")
public class ContextCallController extends StageContext
{
	@FXML
	private AnchorPane mainNode;
	
	@Override
	public void setViewStageTitle(String title) 
	{
		
	}

	@Override
	public AnchorPane getMainNode() 
	{
		return mainNode;
	}

	@Override
	protected void onCreate() 
	{
		getViewStage().setNodeCacheHint(CacheHint.QUALITY);
	}

	@Override
	protected void onStart() 
	{
		getViewStage().setNodeCacheHint(CacheHint.QUALITY);
	}

}
