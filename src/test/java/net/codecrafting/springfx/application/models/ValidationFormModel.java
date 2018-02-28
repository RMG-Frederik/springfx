package net.codecrafting.springfx.application.models;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import net.codecrafting.springfx.controls.ViewContext;
import net.codecrafting.springfx.validation.FormModel;
import net.codecrafting.springfx.validation.ValidationError;

public class ValidationFormModel extends FormModel
{
	private String textField;
	private String passField;
	private Boolean checkField;
	private String choiceField;
	private TableView<Object> tableField;
	private Color colorField;
	private LocalDate wrongField; //wrong type
	private LocalDate dateField;
	private Boolean radioField;
	//private String textAreaField; missing
	private String comboStringField;
	private Double sliderField;
	private String customField;
	private Double barField;
	
	public ValidationFormModel(ViewContext context) 
	{
		super(context);
	}

	@Override
	public void setValidation(List<ValidationError> errors) 
	{
		
	}
	
	public Node getNodeField(Field field)
	{
		return getContextFieldNode(field);
	}
	
	@Override
	public void setValueToModelField(String fieldName, Object value)
	{
		super.setValueToModelField(fieldName, value);
	}
	
	@Override
	public Node getContextFieldNode(String fieldName)
	{
		return super.getContextFieldNode(fieldName);
	}

	public String getTextField() 
	{
		return textField;
	}

	public void setTextField(String textField) 
	{
		this.textField = textField;
	}

	public String getPassField() {
		return passField;
	}

	public void setPassField(String passField) 
	{
		this.passField = passField;
	}

	public Boolean getCheckField() 
	{
		return checkField;
	}

	public void setCheckField(Boolean checkField) 
	{
		this.checkField = checkField;
	}

	public String getChoiceField() 
	{
		return choiceField;
	}

	public void setChoiceField(String choiceField) 
	{
		this.choiceField = choiceField;
	}

	public Color getColorField() 
	{
		return colorField;
	}

	public void setColorField(Color colorField) 
	{
		this.colorField = colorField;
	}

	public LocalDate getWrongField() 
	{
		return wrongField;
	}

	public void setWrongField(LocalDate wrongField) 
	{
		this.wrongField = wrongField;
	}

	public LocalDate getDateField() 
	{
		return dateField;
	}

	public void setDateField(LocalDate dateField) 
	{
		this.dateField = dateField;
	}

	public Boolean getRadioField() 
	{
		return radioField;
	}

	public void setRadioField(Boolean radioField) 
	{
		this.radioField = radioField;
	}

	public String getCustomField() 
	{
		return customField;
	}

	public void setCustomField(String customField) 
	{
		this.customField = customField;
	}

	
	public TableView<Object> getTableField() 
	{
		return tableField;
	}

	public void setTableField(TableView<Object> tableField) 
	{
		this.tableField = tableField;
	}

	public String getComboStringField() 
	{
		return comboStringField;
	}

	public void setComboStringField(String comboStringField) 
	{
		this.comboStringField = comboStringField;
	}

	public Double getSliderField() 
	{
		return sliderField;
	}

	public void setSliderField(Double sliderField) 
	{
		this.sliderField = sliderField;
	}

	public Double getBarField() 
	{
		return barField;
	}

	public void setBarField(Double barField) 
	{
		this.barField = barField;
	}
}
