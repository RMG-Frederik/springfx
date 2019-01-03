package net.codecrafting.springfx.demo.controllers;

import java.util.List;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.context.ViewContext;
import net.codecrafting.springfx.demo.form.DemoForm;
import net.codecrafting.springfx.util.DialogBuilder;
import net.codecrafting.springfx.validation.FormValidator;
import net.codecrafting.springfx.validation.ValidationError;
import net.codecrafting.springfx.validation.ValidationListener;

@ViewController(name="demo")
public class DemoFormController extends ViewContext implements ValidationListener
{
	@FXML
	private AnchorPane mainNode;
	
	@FXML
	@ValidationBind("user")
	private JFXTextField userField;
	
	@FXML
	@ValidationBind("pass")
	private JFXPasswordField passField;
	
	@FXML
	@ValidationBind("text")
	private JFXTextArea textField;
	
	@FXML
	@ValidationBind("date")
	private JFXDatePicker datePicker;
	
	@FXML
	@ValidationBind("color")
	private JFXColorPicker colorPicker;
	
	@FXML
	@ValidationBind
	private JFXToggleButton funToggle;
	
	private DemoForm demoForm;
	private FormValidator<DemoForm> validator;
	
	public DemoFormController()
	{
		//this.setFitHeight(false);
	}
	
	@Override
	public Node getMainNode() 
	{
		return mainNode;
	}

	@Override
	protected void onCreate() 
	{
		demoForm = new DemoForm(this);
		validator = new FormValidator<DemoForm>(demoForm);
		validator.setValidationListener(this);
	}

	@Override
	protected void onStart() 
	{
		loadFormData();
		demoForm.setValuesToForm();
	}
	
	@FXML
	public void submit()
	{
		demoForm.setValuesFromForm();
		validator.validate();
	}

	@Override
	public void onValidationSucceeded() 
	{
		DialogBuilder.showInfo("Nice! Validation Succeeded\n"+demoForm.toString());
	}

	@Override
	public void onValidationFailed(List<ValidationError> errors) 
	{
		DialogBuilder.showWarn(errors.toString());
	}
	
	private void loadFormData()
	{
		demoForm.setUser(System.getProperty("user.name"));
	}
}
