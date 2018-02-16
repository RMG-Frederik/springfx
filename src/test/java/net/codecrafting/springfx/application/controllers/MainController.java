package net.codecrafting.springfx.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.annotation.ViewLink;
import net.codecrafting.springfx.controls.StageContext;

@ViewController
public class MainController extends StageContext
{
	@FXML
	private AnchorPane mainNode;
	
	@FXML
	@ViewLink(TestController.class)
	private Pane testPane;
	
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
	protected void onCreate(URL location, ResourceBundle resources) 
	{
		
	}
}
