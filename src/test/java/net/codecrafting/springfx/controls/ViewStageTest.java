package net.codecrafting.springfx.controls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.testfx.util.WaitForAsyncUtils;

import com.sun.javafx.application.PlatformImpl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import net.codecrafting.springfx.application.EmptyApplication;
import net.codecrafting.springfx.application.controllers.BadMainController;
import net.codecrafting.springfx.application.controllers.BadTestController;
import net.codecrafting.springfx.application.controllers.FakeMainController;
import net.codecrafting.springfx.application.controllers.MainController;
import net.codecrafting.springfx.application.controllers.TestController;
import net.codecrafting.springfx.core.BootstrapApplication;
import net.codecrafting.springfx.core.SpringFXLauncher;
import net.codecrafting.springfx.utils.Mipmap;
import net.codecrafting.springfx.utils.MipmapLevel;

@SuppressWarnings("restriction")
public class ViewStageTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	public ApplicationContext springContext;
	
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
	public void init()
	{
		springContext = new SpringApplicationBuilder().sources(EmptyApplication.class).web(false).run();
	}
	
	@Test
	public void constructorWithoutDefaultPath()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));		
		assertEquals(ViewStage.DEFAULT_VIEW_PATH, viewStage.getViewPath());
	}
	
	@Test
	public void constructorWithViewPath()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext, "test");
		}));
		assertEquals("test", viewStage.getViewPath());
	}
	
	@Test
	public void badInstantiation()
	{
		Future<ViewStage> futureStage = asyncFx(() -> {
			return new ViewStage(null);
		});
		
		try {
			futureStage.get();
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("springContext and viewPath must not be null", e.getCause().getMessage());
		}
		
		futureStage = asyncFx(() -> {
			return new ViewStage(springContext, null);
		});
		
		try {
			futureStage.get();
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("springContext and viewPath must not be null", e.getCause().getMessage());
		}
		
		futureStage = asyncFx(() -> {
			return new ViewStage(null, "");
		});
		
		try {
			futureStage.get();
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("springContext and viewPath must not be null", e.getCause().getMessage());
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void onMinimizeRequest()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.show();
			vs.setIconified(true);
			vs.setIconified(false);
			return vs;
		}));
		EventHandler<ActionEvent> mockEventHandler = Mockito.mock(EventHandler.class);
		viewStage.setOnMinimizeRequest(mockEventHandler);
		asyncFx(() -> {viewStage.setIconified(true);});
		WaitForAsyncUtils.waitForFxEvents();
		Mockito.verify(mockEventHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void onMaximizeRequest()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.show();	
			vs.setMaximized(true);
			vs.setMaximized(false);
			return vs;
		}));
		EventHandler<ActionEvent> mockEventHandler = Mockito.mock(EventHandler.class);
		viewStage.setOnMaximizeRequest(mockEventHandler);
		asyncFx(() -> {viewStage.setMaximized(true);});
		WaitForAsyncUtils.waitForFxEvents();
		Mockito.verify(mockEventHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
	}
	
	@Test
	public void setIcon()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		viewStage.setIcon("/icons/launcher_icon@4x.png");
		assertEquals(1, viewStage.getIcons().size());
		
		try {
			viewStage.setIcon(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("iconPath must not be null", e.getMessage());
		}
		
		try {
			viewStage.setIcon("test.png");
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getClass());
		}
	}
	
	@Test
	public void setIconByMipmap()
	{
		this.thrown.expect(RuntimeException.class);
		this.thrown.expectMessage("mipmap must not be null");
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		List<MipmapLevel> levels = new ArrayList<MipmapLevel>();
		MipmapLevel mipmapLevel = Mockito.mock(MipmapLevel.class);
		Image img1 = new Image("/icons/launcher_icon.png");
		Mockito.when(mipmapLevel.getImage()).thenReturn(img1);
		Mockito.when(mipmapLevel.getLevel()).thenReturn(1);
		levels.add(mipmapLevel);
		
		mipmapLevel = Mockito.mock(MipmapLevel.class);
		Image img2 = new Image("/icons/launcher_icon@2x.png");
		Mockito.when(mipmapLevel.getImage()).thenReturn(img2);
		Mockito.when(mipmapLevel.getLevel()).thenReturn(2);
		levels.add(mipmapLevel);
		
		Mipmap mipmap = Mockito.mock(Mipmap.class);
		Mockito.when(mipmap.getAllLevels()).thenReturn(levels);
		viewStage.setIconByMipmap(mipmap);
		assertNotNull(viewStage.getIconMipmap());
		assertEquals(2, viewStage.getIcons().size());
		viewStage.setIconByMipmap(null);
	}
	
	@Test
	public void querySelector()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		
		try {
			viewStage.querySelector(".test");
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
			assertEquals("ViewStage is not initialized", e.getMessage());
		}
		
		waitFor(asyncFx(() -> {
			viewStage.init(MainController.class);
		}));

		try {
			viewStage.querySelector(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("selector must not be null", e.getMessage());
		}
		
		assertNull(viewStage.querySelector(".error"));
		assertNotNull(viewStage.querySelector(".test"));
	}
	
	@Test
	public void querySelectorAll()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		
		try {
			viewStage.querySelectorAll(".test");
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
			assertEquals("ViewStage is not initialized", e.getMessage());
		}
		
		waitFor(asyncFx(() -> {
			viewStage.init(MainController.class);
		}));
		
		try {
			viewStage.querySelectorAll(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("selector must not be null", e.getMessage());
		}
		
		assertEquals(0, viewStage.querySelectorAll(".error").size());
		assertEquals(1, viewStage.querySelectorAll(".test").size());
		assertEquals(2, viewStage.querySelectorAll(".inner-test").size());
	}
	
	@Test
	public void show()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.show(false);
			return vs;
		}));
		
		assertTrue("View Stage not showing", viewStage.isShowing());
		waitFor(asyncFx(() -> {
			viewStage.close();
			AnchorPane root = new AnchorPane();
			root.setPrefSize(200, 200);
			root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
			root.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			viewStage.setScene(new Scene(root, 200, 200));
			viewStage.show(true);
		}));
		
		assertTrue("View Stage not showing", viewStage.isShowing());
		assertEquals(200, viewStage.getScene().getWindow().getHeight(), 40);
		assertEquals(200, viewStage.getScene().getWindow().getWidth(), 40);
	}
	
	@Test
	public void initialize() throws Exception
	{
		Future<ViewStage> futureStage = asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(null);
			return vs;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("StageContext class must not be null", e.getCause().getMessage());
		}
		
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			return vs;
		}));
		
		assertTrue("ViewStage not initialized", viewStage.isInitialized());
		assertNotNull(viewStage.getStageContext());
		
		//Don't know why it's not covered by IconMipmap test
		assertNull("Mipmap should be null", viewStage.getIconMipmap());
		
		futureStage = asyncFx(() -> {
			viewStage.init(MainController.class);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getCause().getClass());
			assertEquals("ViewStage already initialized", e.getCause().getMessage());
		}

		futureStage = asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(FakeMainController.class);
			return vs;
		});
		try {
			futureStage.get();
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
		}
		
		futureStage = asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext, "fxml/");
			vs.init(MainController.class);
			return vs;
		});
		try {
			futureStage.get();
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getCause().getClass());
			assertEquals("Could not initialize with \"fxml/main.fxml\"", e.getCause().getMessage());
		}
		
		futureStage = asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(BadMainController.class);
			return vs;
		});
		try {
			futureStage.get();
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
		}
	}
	
	@Test
	public void loadInvalidIntent() throws Exception
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.show(true);
			return vs;
		}));
		
		Future<ViewStage> futureStage = asyncFx(() -> {
			viewStage.loadIntent(null);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getCause().getClass());
			assertEquals("ViewStage is not initialized", e.getCause().getMessage());
		}
		
		asyncFx(() -> {
			viewStage.init(MainController.class);
		}).get();
		
		futureStage = asyncFx(() -> {
			viewStage.loadIntent(null);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("Intent must not be null", e.getCause().getMessage());
		}
		
		Intent mockIntent = Mockito.mock(Intent.class);
		Mockito.doReturn(null).when(mockIntent).getViewClass();
		futureStage = asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("Intent viewClass must not be null", e.getCause().getMessage());
		}
		
		Mockito.reset(mockIntent);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(null).when(mockIntent).getCallerContext();
		futureStage = asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("Intent callerContext must not be null", e.getCause().getMessage());
		}
	}
	
	@Test
	public void loadIntentBadControllers()
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			vs.show(true);
			return vs;
		}));
		
		Intent mockIntent = Mockito.mock(Intent.class);
		Mockito.doReturn(FakeMainController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(viewStage.getStageContext()).when(mockIntent).getCallerContext();
		Future<ViewStage> futureStage = asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		});	
		
		try {
			futureStage.get();
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
			assertNull(viewStage.getIntent());
		}	
		
		Mockito.reset(mockIntent);
		Mockito.doReturn(BadTestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(viewStage.getStageContext()).when(mockIntent).getCallerContext();
		futureStage = asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
			assertEquals("Could not load \"/views/bad_test.fxml\"", e.getCause().getMessage());
			assertNull(viewStage.getIntent());
		}
		
		Mockito.reset(mockIntent);
		Mockito.doReturn(BadMainController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(viewStage.getStageContext()).when(mockIntent).getCallerContext();
		futureStage = asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		});
		
		try {
			futureStage.get();
			assertFalse("RuntimeException not thrown", true);
		} catch(Exception e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
			assertNull(viewStage.getIntent());
		}
	}
	
	@Test
	public void loadIntentControllers() throws Exception
	{
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			vs.show(true);
			return vs;
		}));

		Intent mockIntent = Mockito.mock(Intent.class);
		ViewContext mockCallerContext = Mockito.mock(ViewContext.class);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(mockCallerContext).when(mockIntent).getCallerContext();
		asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		}).get();
		assertNotNull(viewStage.getIntent());
		
		Mockito.reset(mockIntent, mockCallerContext);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(mockCallerContext).when(mockIntent).getCallerContext();
		
		asyncFx(() -> {
			viewStage.loadIntent(mockIntent);
			return viewStage;
		}).get();
		Pane testPane = (Pane) viewStage.getScene().getRoot().lookup("#testPane");
		assertNotNull("Test Pane not found", testPane);
		assertNotNull("View Link event should be configured", testPane.getOnMouseClicked());
		waitFor(asyncFx(() -> {
			testPane.getOnMouseClicked().handle(null);
		}));
		assertEquals(TestController.class, viewStage.getIntent().getCallerContext().getClass());
	}
	
	@Test
	public void removeIntentFromCache()
	{
		Intent mockIntent = Mockito.mock(Intent.class);
		ViewContext mockCallerContext = Mockito.mock(ViewContext.class);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(mockCallerContext).when(mockIntent).getCallerContext();
		
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			vs.loadIntent(mockIntent);
			vs.show(true);
			return vs;
		}));
		
		try {
			viewStage.removeViewCache(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("ViewContext class must not be null", e.getMessage());
		}
		
		viewStage.removeViewCache(BadMainController.class);
		assertTrue("Intent not cached", viewStage.isViewCached(TestController.class));
		viewStage.removeViewCache(TestController.class);
		assertFalse("Intent still cached", viewStage.isViewCached(TestController.class));
	}
	
	@Test
	public void isIntentCached()
	{
		Intent mockIntent = Mockito.mock(Intent.class);
		ViewContext mockCallerContext = Mockito.mock(ViewContext.class);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(mockCallerContext).when(mockIntent).getCallerContext();
		
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			vs.loadIntent(mockIntent);
			vs.show(true);
			return vs;
		}));
		
		try {
			viewStage.isViewCached(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("ViewContext class must not be null", e.getMessage());
		}
		
		assertTrue("Intent not cached", viewStage.isViewCached(TestController.class));
		viewStage.removeViewCache(TestController.class);
		assertFalse("Intent still cached", viewStage.isViewCached(TestController.class));
	}
	
	@Test
	public void clearIntentCache()
	{
		Intent mockIntent = Mockito.mock(Intent.class);
		ViewContext mockCallerContext = Mockito.mock(ViewContext.class);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.doReturn(mockCallerContext).when(mockIntent).getCallerContext();
		
		ViewStage viewStage = waitFor(asyncFx(() -> {
			ViewStage vs = new ViewStage(springContext);
			vs.init(MainController.class);
			vs.loadIntent(mockIntent);
			vs.show(true);
			return vs;
		}));
	
		viewStage.clearViewCache();
		assertFalse("Intent still cached", viewStage.isViewCached(TestController.class));
	}
}
