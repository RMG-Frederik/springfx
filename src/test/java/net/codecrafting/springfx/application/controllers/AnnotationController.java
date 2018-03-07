package net.codecrafting.springfx.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.context.ViewContext;

@ViewController(name="ann", title="test")
public class AnnotationController extends ViewContext
{
	private URL location;
	private ResourceBundle resources;
	
	@Override
	public Node getMainNode() {return null;}

	@Override
	protected void onStart() {}

	@Override
	protected void onCreate(URL location, ResourceBundle resources) 
	{
		this.location = location;
		this.resources = resources;
	}
	
	public URL getLocation()
	{
		return location;
	}
	
	public ResourceBundle getResources()
	{
		return resources;
	}
}
