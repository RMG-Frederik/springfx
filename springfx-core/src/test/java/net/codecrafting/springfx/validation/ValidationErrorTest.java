/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ValidationErrorTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void fieldMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("field must not be null");
		new ValidationError(null);
	}
	
	@Test
	public void fieldMustNotBeNullFullInstantiation()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("field must not be null");
		new ValidationError(null, null, null);
	}
	
	@Test
	public void instantiation()
	{
		new ValidationError("test");
		new ValidationError("test", null, null);
		new ValidationError("test", "test msg", null);
		new ValidationError("test", "test msg", "test value");
	}
	
	@Test
	public void getField()
	{
		ValidationError error = new ValidationError("test");
		assertEquals("test", error.getField());
		error = new ValidationError("test 2", null, null);
		assertEquals("test 2", error.getField());
	}
	
	@Test
	public void setField()
	{
		ValidationError error = new ValidationError("test");
		error.setField("test 2");
		assertEquals("test 2", error.getField());
	}
	
	@Test
	public void getMessage()
	{
		ValidationError error = new ValidationError("test");
		assertNull(error.getMessage());
		error = new ValidationError("test 2", "test 2 msg", null);
		assertEquals("test 2 msg", error.getMessage());
	}
	
	@Test
	public void setMessage()
	{
		ValidationError error = new ValidationError("test");
		error.setMessage("test msg");
		assertEquals("test msg", error.getMessage());
	}
	
	@Test
	public void getValue()
	{
		ValidationError error = new ValidationError("test");
		assertNull(error.getValue());
		error = new ValidationError("test 2", null, "test 2 value");
		assertEquals("test 2 value", error.getValue());
	}
	
	@Test
	public void setValue()
	{
		ValidationError error = new ValidationError("test");
		error.setValue("test msg");
		assertEquals("test msg", error.getValue());
	}
	
	@Test
	public void castToString()
	{
		ValidationError error = new ValidationError("test", null, null);
		assertEquals("ValidationError [field=test, message=null, value=null]", error.toString());
		error = new ValidationError("test", null, "test value");
		assertEquals("ValidationError [field=test, message=null, value=test value]", error.toString());
		error = new ValidationError("test", "test msg", null);
		assertEquals("ValidationError [field=test, message=test msg, value=null]", error.toString());
		error = new ValidationError("test", "test msg", "test value");
		assertEquals("ValidationError [field=test, message=test msg, value=test value]", error.toString());
	}
}
