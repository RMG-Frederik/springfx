package net.codecrafting.springfx.demo.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.context.ViewContext;
import net.codecrafting.springfx.validation.ValidationError;
import net.codecrafting.springfx.validation.ValidationListener;

@ViewController
public class LoginController extends ViewContext implements ValidationListener
{
	@FXML
	private FlowPane mainNode;
	
	@Override
	public Node getMainNode() 
	{
		return mainNode;
	}

	@Override
	protected void onCreate() 
	{
		
	}

	@Override
	protected void onStart() 
	{
		
	}

	@Override
	public void onValidationSucceeded() 
	{
		
	}

	@Override
	public void onValidationFailed(List<ValidationError> errors) 
	{
		
	}
}
