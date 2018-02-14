package net.codecrafting.springfx.controls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
import javafx.scene.image.Image;
import net.codecrafting.springfx.application.EmptyApplication;
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
	
	public ViewStage viewStage;
	
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
		springContext = new SpringApplicationBuilder().sources(EmptyApplication.class).web(false).run(new String[0]);
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
		this.thrown.expect(RuntimeException.class);
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		viewStage.setIcon("/icons/launcher_icon@4x.png");
		assertEquals(1, viewStage.getIcons().size());
		viewStage.setIcon(null);
	}
	
	@Test
	public void setIconByMipmap()
	{
		this.thrown.expect(RuntimeException.class);
		ViewStage viewStage = waitFor(asyncFx(() -> {
			return new ViewStage(springContext);
		}));
		List<MipmapLevel> levels = new ArrayList<MipmapLevel>();
		MipmapLevel mipmapLevel = Mockito.mock(MipmapLevel.class);
		Image img1 = new Image("/icon/launcher_icon.png");
		Mockito.when(mipmapLevel.getImage()).thenReturn(img1);
		Mockito.when(mipmapLevel.getLevel()).thenReturn(1);
		levels.add(mipmapLevel);
		
		mipmapLevel = Mockito.mock(MipmapLevel.class);
		Image img2 = new Image("/icon/launcher_icon@2x.png");
		Mockito.when(mipmapLevel.getImage()).thenReturn(img2);
		Mockito.when(mipmapLevel.getLevel()).thenReturn(2);
		levels.add(mipmapLevel);
		
		Mipmap mipmap = Mockito.mock(Mipmap.class);
		Mockito.when(mipmap.getAllLevels()).thenReturn(levels);
		viewStage.setIconByMipmap(mipmap);
		assertNotNull(viewStage.getIconMipmap());
		assertEquals(2, viewStage.getIcons().size());
	}
}
