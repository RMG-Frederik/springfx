package net.codecrafting.springfx.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import net.codecrafting.springfx.context.ViewContext;

public class TestWithoutAnnotationController extends ViewContext
{
	@Override
	public Node getMainNode() 
	{
		return null;
	}

	@Override
	protected void onStart() {}

	@Override
	protected void onCreate(URL location, ResourceBundle resources) {}

}
