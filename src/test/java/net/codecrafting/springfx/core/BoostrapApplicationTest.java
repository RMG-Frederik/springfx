package net.codecrafting.springfx.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

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
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.javafx.application.PlatformImpl;

import javafx.stage.Stage;
import net.codecrafting.springfx.application.AnnotatedTestApplication;
import net.codecrafting.springfx.application.EmptyApplication;
import net.codecrafting.springfx.application.controllers.MainController;
import net.codecrafting.springfx.utils.DialogBuilder;

@SuppressWarnings("restriction")
public class BoostrapApplicationTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private SpringFXContext context;
	
	private Properties dialogTestProps;
	private static final Log LOGGER = LogFactory.getLog(BoostrapApplicationTest.class);
	
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(EmptyApplication.class).web(false);
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
	public void startupOpenWithMainController() throws Exception
	{
		System.getProperties().setProperty("springfx.app.root-controller", MainController.class.getName());
		System.getProperties().setProperty("springfx.app.auto-open", "true");
		doAnswer((Answer<Void>) invocation -> {
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
			SpringApplicationBuilder springBuilder = new SpringApplicationBuilder().sources(AnnotatedTestApplication.class).web(false);
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
