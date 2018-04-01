/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2018 The SpringFX Contributors
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
package net.codecrafting.springfx.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.codecrafting.springfx.application.EmptyApplication;
import net.codecrafting.springfx.application.HeadlessTestApplication;

public class SpringFXContextImplTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void applicationMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Application class must not be null");
		new SpringFXContextImpl(null);
	}
	
	@Test
	public void getAttributesBeforeLaunch()
	{
		SpringFXContextImpl context = new SpringFXContextImpl(EmptyApplication.class);
		assertNull(context.getApplication());
		assertNull(context.getEnvironment());
		assertNull(context.getSpringContext());
	}
	
	@Test
	public void initialization()
	{
		SpringFXContextImpl context = new SpringFXContextImpl(EmptyApplication.class);
		context.run(new String[0]);
		assertNotNull(context.getApplication());
		assertNotNull(context.getEnvironment());
		assertNotNull(context.getSpringContext());
	}
	
	@Test
	public void initializationWithoutArgs()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Args must not be null");
		SpringFXContextImpl context = new SpringFXContextImpl(EmptyApplication.class);
		context.run(null);
	}
	
	@Test
	public void stopContext()
	{
		SpringFXContextImpl context = new SpringFXContextImpl(EmptyApplication.class);
		context.run(new String[0]);
		assertNotNull(context.getSpringContext());
		context.stop();
		assertFalse("SpringContext still running", context.getSpringContext().isRunning());
		assertFalse("SpringFXContext still running", context.getSpringContext().isRunning());
		
		context = new SpringFXContextImpl(EmptyApplication.class);
		context.stop();
		context.run(new String[0]);
		assertNotNull(context.getSpringContext());
		context.getSpringContext().stop();
		context.stop();
		context.getSpringContext().close();
		context.stop();	
	}
	
	@Test
	public void headlessApplication()
	{
		SpringFXContextImpl context = new SpringFXContextImpl(HeadlessTestApplication.class);
		context.run(new String[0]);
		assertNotNull(context.getApplication());
		assertFalse("Application is not headless", context.isHeadless());
	}
}
