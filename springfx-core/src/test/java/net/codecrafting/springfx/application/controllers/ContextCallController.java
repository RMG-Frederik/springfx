package net.codecrafting.springfx.application.controllers;

import javafx.fxml.FXML;
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
		getViewStage().setCacheLoadedNode(true);
	}

	@Override
	protected void onStart() 
	{
		getViewStage().setCacheLoadedNode(true);
	}

}
