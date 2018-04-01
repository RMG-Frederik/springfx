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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.javafx.application.PlatformImpl;

import javafx.stage.Stage;
import net.codecrafting.springfx.application.AnnotatedTestApplication;
import net.codecrafting.springfx.application.EmptyApplication;
import net.codecrafting.springfx.application.controllers.MainController;
import net.codecrafting.springfx.util.DialogBuilder;

@SuppressWarnings("restriction")
public class BootstrapApplicationTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private SpringFXContext context;
	
	private Properties dialogTestProps;
	private static final Log LOGGER = LogFactory.getLog(BootstrapApplicationTest.class);
	
	@BeforeClass
	public static void setup() throws InterruptedException
	{
		SpringFXLauncher.setRelaunchable(true);
		if(!BootstrapApplication.isToolkitInitialized()) {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			PlatformImpl.startup(() -> {
				countDownLatch.countDown();
				new BootstrapApplication();
			});
			countDownLatch.await();
		}
	}
	
	@Before
	public void init() throws Exception 
	{
		DialogBuilder.init();
		dialogTestProps = new Properties(DialogBuilder.DEFAULT_PROPS);
		for(String key : dialogTestProps.stringPropertyNames()) 
		{
			if(key.contains("icon")) {
				dialogTestProps.put(key, "/icons/launcher_icon.png");
			} else if(key.contains("stylesheets")) {
				dialogTestProps.put(key, "/css/test.css");
			} else {
				dialogTestProps.put(key, dialogTestProps.get(key)+" - TEST");
			}
		}
		MockitoAnnotations.initMocks(this);
	}
	
	@After
	public void clear()
	{
		String props[] = {"springfx.app.root-controller", "springfx.app.auto-open", "springfx.app.name", "springfx.app.icon"};
		for (String string : props) 
		{
			if(System.getProperties().containsKey(string)) {
				System.getProperties().remove(string);
			}
		}
		for(String key : dialogTestProps.stringPropertyNames()) 
		{
			if(System.getProperties().containsKey(key)) {
				System.getProperties().remove(key);
			}
		}
		DialogBuilder.init();
	}
	
	@Test
	public void toolkitAlreadyInitialized()
	{
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Application Toolkit has already initialized.");
		new BootstrapApplication();
	}
	
	@Test
	public void springFXContextNotNull() throws Exception
	{
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(EmptyApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		assertNotNull(new BootstrapApplication(context).getSpringFXContext());
	}
	
	@Test
	public void initialization() throws Exception
	{
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		assertTrue("Application was not initialized", ((AnnotatedTestApplication) context.getApplication()).getInit());
	}
	
	@Test
	public void simpleStartup() throws Exception
	{
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		AnnotatedTestApplication application = (AnnotatedTestApplication) context.getApplication();
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
				bootApplication.stop();
				throw new NullPointerException("teste");
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		assertTrue("Application init failed", application.getInit());
		assertTrue("Application start failed", application.getStart());
		assertTrue("Application stop failed", application.getStop());
	}
	
	@Test
	public void startupWithDialogProperties() throws Exception
	{
		System.getProperties().putAll(dialogTestProps);
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
				bootApplication.stop();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		try {
			Properties dialogProps = DialogBuilder.getProperties();
			for(String key : dialogTestProps.stringPropertyNames()) 
			{
				if(!dialogProps.containsKey(key)) {
					assertFalse("Missing Dialog property: "+key, true);
				} else {
					assertEquals(dialogTestProps.get(key), dialogProps.get(key));
				}
			}	
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			assertFalse(e.getMessage(), true);
		}
	}
	
	@Test
	public void startupWithMainController() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		System.getProperties().setProperty("springfx.app.auto-open", "false");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		AnnotatedTestApplication application = (AnnotatedTestApplication) context.getApplication();
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
				bootApplication.stop();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		assertTrue("Application init failed", application.getInit());
		assertTrue("Application start failed", application.getStart());
		assertTrue("Application stop failed", application.getStop());
	}
	
	@Test
	public void badStartupWithMainController() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName()+"Test");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		Future<Void> futureStart = asyncFx(() -> {
			bootApplication.start(new Stage());
			return null;
		});
		try {
			futureStart.get();
			assertFalse("Exception with bad controller not thrown", true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		context.stop();
	}

	@Test
	public void startupMainControllerWithCache() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		System.getProperties().setProperty("springfx.cache-loaded-node", "true");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		MainController mainController = context.getSpringContext().getBean(MainController.class);
		assertTrue("Cache node is not true", mainController.getViewStage().isCacheLoadedNode());
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@Test
	public void startupOpenWithMainController() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		System.getProperties().setProperty("springfx.app.auto-open", "true");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		AnnotatedTestApplication application = (AnnotatedTestApplication) context.getApplication();
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
				assertTrue("ViewStage is not open", application.getViewStage().isShowing());
				bootApplication.stop();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		assertTrue("Application init failed", application.getInit());
		assertTrue("Application start failed", application.getStart());
		assertTrue("Application stop failed", application.getStop());
	}
	
	@Test
	public void startupWithMainControllerAndIcons() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		System.getProperties().setProperty("springfx.app.auto-open", "true");
		System.getProperties().setProperty("springfx.app.name", "SpringFX Test");
		System.getProperties().setProperty("springfx.app.icon", "/icons/launcher_icon.png");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(WebApplicationType.NONE);
			ConfigurableApplicationContext springContext = springBuilder.run((String[]) invocation.getArguments()[0]);
			when(context.getSpringContext()).thenReturn(springContext);
			when(context.getEnvironment()).thenReturn(springContext.getEnvironment());
			when(context.getApplication()).thenReturn(springContext.getBean(AnnotatedTestApplication.class));
			return null;
		}).when(context).run(new String[0]);
		context.run(new String[0]);
		AnnotatedTestApplication application = (AnnotatedTestApplication) context.getApplication();
		BootstrapApplication bootApplication = new BootstrapApplication(context);
		bootApplication.init();
		PlatformImpl.runAndWait(() -> {
			try {
				bootApplication.start(new Stage());
				assertTrue("ViewStage is not open", application.getViewStage().isShowing());
				assertEquals("SpringFX Test", application.getViewStage().getTitle());
				assertEquals(5, application.getViewStage().getIcons().size());
				bootApplication.stop();
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("Application startup failed", false);
			}
		});
		assertTrue("Application init failed", application.getInit());
		assertTrue("Application start failed", application.getStart());
		assertTrue("Application stop failed", application.getStop());
	}
}
