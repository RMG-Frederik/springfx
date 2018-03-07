package net.codecrafting.springfx.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

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
	protected void onCreate(URL location, ResourceBundle resources) {}
	
	public boolean getTest()
	{
		return test;
	}
	
	public Region getRegion()
	{
		return region;
	}
}
