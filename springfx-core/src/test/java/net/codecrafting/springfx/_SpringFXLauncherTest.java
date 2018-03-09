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
package net.codecrafting.springfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import net.codecrafting.springfx.application.AnnotatedTestApplication;
import net.codecrafting.springfx.application.EmptyApplication;
import net.codecrafting.springfx.application.ThroableEmptyApplication;
import net.codecrafting.springfx.core.SpringFXContext;
import net.codecrafting.springfx.core.SpringFXLauncher;
import net.codecrafting.springfx.exception.SpringFXLaunchException;

//Make this test class be the last
public class _SpringFXLauncherTest 
{
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private SpringFXContext context;
	
	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	private static final Log LOGGER = LogFactory.getLog(_SpringFXLauncherTest.class);
	
	@Test
	public void applicationClassMustNotBeNull() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("SpringFXContext must not be null");
		new SpringFXLauncher(null).launch(new String[0]);
	}
	
	@Test
	public void launch() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Mockito.doAnswer((Answer<Void>) i1 -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) i1.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			return null;
		}).when(context).run(new String[0]);
		SpringFXLauncher launcher = new SpringFXLauncher(context);
		launcher.launch(new String[0]);
		assertNotNull(launcher.getContext());
		LOGGER.info("Test launch");
	}
	
	@Test
	public void launchWithStop() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Mockito.doAnswer((Answer<Void>) i1 -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) i1.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			Mockito.doAnswer((Answer<Void>) i2 -> {
				if(springContext.isActive() && springContext.isRunning())
					springContext.stop();
				return null;
			}).when(context).stop();
			return null;
		}).when(context).run(new String[0]);
		new SpringFXLauncher(context).launch(new String[0]);
		context.stop();
	}
	
	@Test
	public void launchApplicationWithErrors() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Mockito.doAnswer((Answer<Void>) i1 -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(ThroableEmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) i1.getArguments()[0]);
			ThroableEmptyApplication application = springContext.getBean(ThroableEmptyApplication.class);
			application.setThrowInit(true);
			application.setThrowStart(true);
			application.setThrowStop(true);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(application);
			Mockito.doAnswer((Answer<Void>) i2 -> {
				if(springContext.isActive() && springContext.isRunning())
					springContext.stop();
				return null;
			}).when(context).stop();
			return null;
		}).when(context).run(new String[0]);
		new SpringFXLauncher(context).launch(new String[0]);
		context.stop();
		LOGGER.info("Test launchWithStopException");
	}
	
	@Test
	public void launchWithWebContext() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.SERVLET);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			return null;
		}).when(context).run(new String[0]);
		SpringFXLauncher launcher = new SpringFXLauncher(context);
		launcher.launch(new String[0]);
		assertNotNull(launcher.getContext());
		LOGGER.info("Test launchWithWebContext");
	}
	
	@Test
	public void launchWithoutArgs() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Args must not be null");
		new SpringFXLauncher(context).launch((String[]) null);
	}
	
	@Test
	public void launchWithoutProperties() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Properties must not be null");
		new SpringFXLauncher(context).launch((Properties) null);
	}
	
	@Test
	public void relaunchableIsFalse() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			Mockito.doAnswer((Answer<Void>) i2 -> {
				if(springContext.isActive() && springContext.isRunning())
					springContext.stop();
				return null;
			}).when(context).stop();			
			return null;
		}).when(context).run(new String[0]);
		new SpringFXLauncher(context).launch(new String[0]);
		SpringFXLauncher.setRelaunchable(false);
		context.stop();
		
		try {
			new SpringFXLauncher(context).launch(new String[0]);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("Relaunchable attribute is false", ex.getMessage());
		}
		
		Properties props = new Properties();
		props.put("prism.lcdtext", "false");
		try {
			new SpringFXLauncher(context).launch(props);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("Relaunchable attribute is false", ex.getMessage());
		}
		LOGGER.info("Test springFXRelaunchableIsFalse");
	}
	
	@Test
	public void launchWithArgs() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		String[] args = {"--myArg=test"};
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(args);
		new SpringFXLauncher(context).launch(args);
		AnnotatedTestApplication application = (AnnotatedTestApplication) context.getApplication();
		assertNotNull(application);
		assertNotNull(application.getArgs());
		assertEquals("test", application.getArgs().getOptionValues("myArg").get(0));
		LOGGER.info("Test springFXLauncherWithArgs");
	}
	
	@Test
	public void launchWithProperties() throws Exception
	{
		SpringFXLauncher.setRelaunchable(true);
		Properties props = new Properties();
		props.put("prism.lcdtext", "false");
		props.put("prism.text", "t2k");
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			return null;
		}).when(context).run(new String[0]);
		new SpringFXLauncher(context).launch(props);
		assertNotNull(context.getEnvironment());
		assertEquals(true, context.getEnvironment().containsProperty("prism.text"));
		LOGGER.info("Test springFXLauncherWithProperties");
	}
	
	@Test
	public void springFXContextMustNotBeEmpty() {
		//THROW with all null
		SpringFXLauncher.setRelaunchable(true);
		try {
			new SpringFXLauncher(context).launch(new String[0]);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("SpringFXContext must not be empty", ex.getMessage());
		}		
		
		//THROW with environment null
		Mockito.reset(context);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(null);
			Mockito.when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			return null;
		}).when(context).run(new String[0]);	
		try {
			new SpringFXLauncher(context).launch(new String[0]);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("SpringFXContext must not be empty", ex.getMessage());
		}
		
		//THROW with application null
		Mockito.reset(context);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			Mockito.when(context.getApplication()).thenReturn(null);
			return null;
		}).when(context).run(new String[0]);	
		try {
			new SpringFXLauncher(context).launch(new String[0]);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("SpringFXContext must not be empty", ex.getMessage());
		}
		
		//THROW with NullPointerException
		Mockito.reset(context);
		Mockito.doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			Mockito.when(context.getSpringContext()).thenReturn(springContext);
			Mockito.when(context.getEnvironment()).thenReturn(null);
			Mockito.when(context.getApplication()).thenThrow(new NullPointerException("Application is null"));
			return null;
		}).when(context).run(new String[0]);	
		try {
			new SpringFXLauncher(context).launch(new String[0]);
			assertFalse("SpringFXLauncher should thrown SpringFXLauncherException", true);
		} catch(Exception ex) {
			assertEquals(SpringFXLaunchException.class, ex.getClass());
			assertEquals("SpringFXContext must not be empty", ex.getMessage());
		}
	}
}
