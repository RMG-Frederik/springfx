/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.util.ReflectionUtils;

import javafx.scene.control.TextField;
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
	
	@Test
	public void getContextFieldNonNode()
	{
		assertNull(formModel.getNodeField(ReflectionUtils.findField(context.getClass(), "nonNode")));
	}
	
	@Test
	public void setValueToModelFieldWithNullName()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("fieldName must not be null");
		formModel.setValueToModelField((String) null, null);
	}
	
	@Test
	public void setValueToModelFieldWithInvalidName()
	{
		formModel.setValueToModelField("banana", "");
	}

	@Test
	public void setValueToModelField()
	{
		formModel.setValueToModelField("textField", "banana");
		assertEquals("banana", formModel.getTextField());
	}
	
	@Test
	public void getContextFieldNodeWithNullName()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("fieldName must not be null");
		formModel.getContextFieldNode((String) null);
	}
	
	@Test
	public void getContextFieldNodeWithInvalidName()
	{
		formModel.getContextFieldNode("banana");
	}
	
	@Test
	public void getContextFieldNode()
	{
		context.getTextField().setText("banana");
		assertEquals("banana", ((TextField) formModel.getContextFieldNode("textField")).getText());
	}
	
	@Test
	public void postUpdateValuesCall()
	{
		assertNull(formModel.getHookField());
		formModel.setHookField("test");
		assertEquals("test", formModel.getHookField());
		formModel.setValuesFromForm();
		assertNull(formModel.getHookField());
	}
	
	@Test
	public void preUpdateValuesCall()
	{
		assertNull(formModel.getHookField());
		formModel.setHookField("test");
		assertEquals("test", formModel.getHookField());
		formModel.setValuesToForm();
		assertNull(formModel.getHookField());
	}
}
