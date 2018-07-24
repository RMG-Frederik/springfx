package net.codecrafting.springfx.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.annotation.ViewLink;
import net.codecrafting.springfx.context.StageContext;

@ViewController(name="main")
public class MainFitController extends StageContext
{
	@FXML
	private AnchorPane mainNode;
	
	@FXML
	@ViewLink(TestController.class)
	private Pane testPane;
	
	private String viewStageTitle;
	
	public static boolean fitWidth = true;
	public static boolean fitHeight = true;
	
	public MainFitController() 
	{
		super();
		setFitWidth(fitWidth);
		setFitHeight(fitHeight);
	}

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
