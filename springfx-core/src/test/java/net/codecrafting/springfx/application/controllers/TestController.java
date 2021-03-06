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
package net.codecrafting.springfx.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.annotation.ViewLink;
import net.codecrafting.springfx.context.ViewContext;

@ViewController
public class TestController extends ViewContext
{
	@FXML
	@ViewLink(TestController.class)
	private StackPane testPane;
	
	@ViewLink(TestController.class)
	private StackPane nullPane = null;
	
	@ViewLink(TestController.class)
	private StackPane throwPane;
	
	private boolean test;
	
	private Region region;
	
	@Override
	public Node getMainNode() 
	{
		return testPane;
	}

	@Override
	protected void onStart() {}

	@Override
	protected void onCreate() {}
	
	public boolean getTest()
	{
		return test;
	}
	
	public Region getRegion()
	{
		return region;
	}
}
