package net.codecrafting.springfx.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.scene.paint.Color;
import net.codecrafting.springfx.application.controllers.ValidationController;
import net.codecrafting.springfx.application.models.ValidationFormModel;

public class FormModelTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private ValidationController context;
	private ValidationFormModel formModel;
	private final Color springFXColor = Color.valueOf("#60B03F");
	private final LocalDate replicantDate = LocalDate.of(2021, 10, 6);
	
	@Before
	public void init()
	{
		context = new ValidationController();
		formModel = new ValidationFormModel(context);
	}
	
	@Test
	public void contextMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("context must not be null");
		new ValidationFormModel(null);
	}
	
	@Test
	public void getContext()
	{
		assertNotNull(formModel.getContext());
	}
	
	@Test
	public void setValuesFromFrom()
	{
		context.getTextField().setText("text field");
		context.getCheckField().setSelected(true);
		context.getChoiceField().setValue("test choice");
		context.getColorField().setValue(springFXColor);
		context.getWrongField().setValue(Color.GREEN);
		context.getCustomField().setText("custom text");
		context.getDateField().setValue(replicantDate);
		context.getPassField().setText("pass text");
		context.getRadioField().setSelected(false);
		context.getTextAreaField().setText("text area");
		context.getSliderField().setValue(51);
		
		formModel.setValuesFromForm();
		assertEquals(context.getTextField().getText(), formModel.getTextField());
		assertEquals(context.getCheckField().isSelected(), formModel.getCheckField());
		assertEquals(context.getChoiceField().getValue(), formModel.getChoiceField());
		assertEquals(context.getColorField().getValue(), formModel.getColorField());
		assertEquals(context.getCustomField().getText(), formModel.getCustomField());
		assertEquals(context.getDateField().getValue(), formModel.getDateField());
		assertEquals(context.getPassField().getText(), formModel.getPassField());
		assertEquals(context.getRadioField().isSelected(), formModel.getRadioField());
		assertEquals(context.getSliderField().getValue(), formModel.getSliderField(), 0);
	}
	
	@Test
	public void setValuesToForm()
	{
		formModel.setTextField("text field");
		formModel.setCheckField(true);
		formModel.setChoiceField("test choice");
		formModel.setColorField(springFXColor);
		formModel.setCustomField("custom text");
		formModel.setDateField(replicantDate);
		formModel.setPassField("pass text");
		formModel.setRadioField(false);
		formModel.setComboStringField("combo string field");
		formModel.setSliderField(51.0);
		formModel.setBarField(0.0);
		formModel.setWrongField(LocalDate.now());
		
		formModel.setValuesToForm();
		assertEquals(formModel.getTextField(), context.getTextField().getText());
		assertEquals(formModel.getCheckField(), context.getCheckField().isSelected());
		assertEquals(formModel.getChoiceField(), context.getChoiceField().getValue());
		assertEquals(formModel.getColorField(), context.getColorField().getValue());
		assertEquals(formModel.getCustomField(), context.getCustomField().getText());
		assertEquals(formModel.getDateField(), context.getDateField().getValue());
		assertEquals(formModel.getPassField(), context.getPassField().getText());
		assertEquals(formModel.getRadioField(), context.getRadioField().isSelected());
		assertEquals(formModel.getComboStringField(), context.getComboStringField().getValue());
		assertEquals(formModel.getSliderField(), context.getSliderField().getValue(), 0);
	}
}
