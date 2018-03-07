package net.codecrafting.springfx.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.context.ViewContext;

public class ValidationController extends ViewContext
{
	@ValidationBind
	private TextField textField = new TextField();
	
	@ValidationBind
	private PasswordField passField = new PasswordField();
	
	@ValidationBind
	private CheckBox checkField = new CheckBox();
	
	@ValidationBind
	private ChoiceBox<String> choiceField = new ChoiceBox<String>();
	
	@ValidationBind
	private ColorPicker colorField = new ColorPicker();
	
	@ValidationBind
	private ColorPicker wrongField = new ColorPicker();
	
	@ValidationBind
	private DatePicker dateField = new DatePicker();
	
	@ValidationBind
	private RadioButton radioField = new RadioButton();
	
	@ValidationBind
	private TextArea textAreaField = new TextArea();
	
	@ValidationBind("customField")
	private TextArea customField = new TextArea();
	
	@ValidationBind
	private TableView<Object> tableField = new TableView<Object>();
	
	@ValidationBind
	private ProgressBar barField = new ProgressBar();
	
	@ValidationBind
	private Slider sliderField = new Slider();
	
	@ValidationBind
	private ComboBox<String> comboStringField = new ComboBox<String>();
	
	private AnchorPane pane = new AnchorPane();
	
	private Integer nonNode = 0;
	
	@Override
	public Node getMainNode() 
	{
		return pane;
	}

	@Override
	protected void onCreate(URL location, ResourceBundle resources) 
	{
		
	}

	@Override
	protected void onStart() 
	{
		
	}

	public TextField getTextField() 
    {
		return textField;
	}

	public void setTextField(TextField textField) 
    {
		this.textField = textField;
	}

	public PasswordField getPassField() 
    {
		return passField;
	}

	public void setPassField(PasswordField passField) 
    {
		this.passField = passField;
	}

	public CheckBox getCheckField() 
    {
		return checkField;
	}

	public void setCheckField(CheckBox checkField) 
    {
		this.checkField = checkField;
	}

	public ChoiceBox<String> getChoiceField() 
    {
		return choiceField;
	}

	public void setChoiceField(ChoiceBox<String> choiceField) 
    {
		this.choiceField = choiceField;
	}

	public ColorPicker getColorField() 
    {
		return colorField;
	}

	public void setColorField(ColorPicker colorField) 
    {
		this.colorField = colorField;
	}
	
	public ColorPicker getWrongField() 
	{
		return wrongField;
	}

	public void setWrongField(ColorPicker wrongField) 
	{
		this.wrongField = wrongField;
	}

	public DatePicker getDateField() 
    {
		return dateField;
	}

	public void setDateField(DatePicker dateField) 
    {
		this.dateField = dateField;
	}

	public RadioButton getRadioField() 
    {
		return radioField;
	}

	public void setRadioField(RadioButton radioField) 
    {
		this.radioField = radioField;
	}

	public TextArea getTextAreaField() 
    {
		return textAreaField;
	}

	public void setTextAreaField(TextArea textAreaField) 
    {
		this.textAreaField = textAreaField;
	}

	public TableView<Object> getTableField() 
    {
		return tableField;
	}

	public void setTableField(TableView<Object> tableField) 
    {
		this.tableField = tableField;
	}

	public TextArea getCustomField() 
    {
		return customField;
	}

	public void setCustomField(TextArea customField) 
    {
		this.customField = customField;
	}

	public Slider getSliderField() 
	{
		return sliderField;
	}

	public void setSliderField(Slider sliderField) 
	{
		this.sliderField = sliderField;
	}

	public ComboBox<String> getComboStringField() 
	{
		return comboStringField;
	}

	public void setComboStringField(ComboBox<String> comboStringField) 
	{
		this.comboStringField = comboStringField;
	}
}
