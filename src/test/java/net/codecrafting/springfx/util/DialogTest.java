package net.codecrafting.springfx.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sun.javafx.application.PlatformImpl;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.codecrafting.springfx.core.BootstrapApplication;

@SuppressWarnings("restriction")
public class DialogTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setup() throws InterruptedException
	{
		if(!BootstrapApplication.isToolkitInitialized()) {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			PlatformImpl.startup(() -> {
				Platform.setImplicitExit(false);
				countDownLatch.countDown();
				new BootstrapApplication();
			});
			countDownLatch.await();
		}
	}
	
	@Test
	public void defaultAlertType()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));
		assertEquals(AlertType.INFORMATION, dialog.getDialogAlert().getAlertType());
		assertTrue(dialog.getDialogPane().getStyleClass().contains("springfx-dialog"));
	}
	
	@Test
	public void customAlertType()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog(AlertType.WARNING);
		}));
		assertEquals(AlertType.WARNING, dialog.getDialogAlert().getAlertType());
		
		dialog = waitFor(asyncFx(() -> {
			return new Dialog(AlertType.ERROR);
		}));
		assertEquals(AlertType.ERROR, dialog.getDialogAlert().getAlertType());
		assertTrue(dialog.getDialogPane().getStyleClass().contains("springfx-dialog"));
	}
	
	@Test
	public void setIconNotOnJavaFX()
	{
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Not on JavaFX thread");
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));	
		dialog.setIcon(null);
	}
	
	@Test
	public void setIconMustNotBeNull()
	{
		Future<Dialog> futureDialog = asyncFx(() -> {
			Dialog dialog = new Dialog();
			dialog.setIcon(null);
			return dialog;
		});
		
		try {
			futureDialog.get();
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals("icon must not be null", e.getCause().getMessage());
		}
	}
	
	@Test
	public void setIcon()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			return d;
		}));
		Image image = new Image("/icons/launcher_icon.png");
		
		assertNull(dialog.getIcon());
		assertEquals(0, ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().size());
		waitFor(asyncFx(() -> {
			dialog.setIcon(image);
		}));
		assertNotNull(dialog.getIcon());
		assertEquals(1, ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().size());
	}
	
	@Test
	public void getIcon()
	{
		Image image = new Image("/icons/launcher_icon.png");
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.setIcon(image);
			return d;
		}));
		assertEquals(image, dialog.getIcon());
	}
	
	@Test
	public void getDialogPane()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));
		assertNotNull("DialogPane is null", dialog.getDialogAlert());
	}
	
	@Test
	public void getTitle()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.getDialogAlert().setTitle("");
			return d;
		}));
		assertEquals("", dialog.getTitle());
		waitFor(asyncFx(() -> {
			dialog.getDialogAlert().setTitle("test");
		}));
		assertEquals("test", dialog.getTitle());
	}
	
	@Test
	public void setTitle()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("title must not be null");
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.setTitle("test");
			return d;
		}));
		assertEquals("test", dialog.getDialogAlert().getTitle());
		Dialog nDialog = waitFor(asyncFx(() -> {
			return dialog.setTitle("test2");
		}));
		assertEquals("test2", nDialog.getDialogAlert().getTitle());
		nDialog.setTitle(null);
	}
	
	@Test
	public void getHeader()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.getDialogAlert().setHeaderText("");
			return d;
		}));
		assertEquals("", dialog.getHeader());
		waitFor(asyncFx(() -> {
			dialog.getDialogAlert().setHeaderText("test");
		}));
		assertEquals("test", dialog.getHeader());
	}
	
	@Test
	public void setHeader()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("header must not be null");
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.setHeader("test");
			return d;
		}));
		assertEquals("test", dialog.getDialogAlert().getHeaderText());
		Dialog nDialog = waitFor(asyncFx(() -> {
			return dialog.setHeader("test2");
		}));
		assertEquals("test2", nDialog.getDialogAlert().getHeaderText());
		nDialog.setHeader(null);
	}
	
	@Test
	public void getContent()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.getDialogAlert().setContentText("");
			return d;
		}));
		assertEquals("", dialog.getContent());
		waitFor(asyncFx(() -> {
			dialog.getDialogAlert().setContentText("test");
		}));
		assertEquals("test", dialog.getContent());
	}
	
	@Test
	public void setContent()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("content must not be null");
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			d.setContent("test");
			return d;
		}));
		assertEquals("test", dialog.getDialogAlert().getContentText());
		Dialog nDialog = waitFor(asyncFx(() -> {
			return dialog.setContent("test2");
		}));
		assertEquals("test2", nDialog.getDialogAlert().getContentText());
		nDialog.setContent(null);
	}
	
	@Test
	public void getStyleClass()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));
		assertEquals(dialog.getDialogAlert().getDialogPane().getStyleClass().size(), dialog.getStyleClass().size());
		dialog.getStyleClass().add("test");
		assertTrue("test class is missing", dialog.getDialogAlert().getDialogPane().getStyleClass().contains("test"));
	}
	
	@Test
	public void getStylesheets()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));
		assertEquals(0, dialog.getStylesheets().size());
		dialog.getDialogAlert().getDialogPane().getStylesheets().add("-fx-background-color:red");
		assertEquals(1, dialog.getStylesheets().size());
	}
	
	@Test
	public void addStylesheets()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("stylesheets must not be null");
		Dialog dialog = waitFor(asyncFx(() -> {
			return new Dialog();
		}));
		assertEquals(0, dialog.getDialogAlert().getDialogPane().getStylesheets().size());
		ObservableList<String> styles = FXCollections.observableArrayList();
		styles.add("-fx-background-color:red");
		dialog = dialog.addStylesheets(styles);
		assertEquals(1, dialog.getDialogAlert().getDialogPane().getStylesheets().size());
		styles = FXCollections.observableArrayList();
		styles.add("-fx-background-color:yellow");
		dialog.addStylesheets(styles);
		assertEquals(2, dialog.getDialogAlert().getDialogPane().getStylesheets().size());
		assertTrue(dialog.getDialogAlert().getDialogPane().getStylesheets().contains("-fx-background-color:red"));
		assertTrue(dialog.getDialogAlert().getDialogPane().getStylesheets().contains("-fx-background-color:yellow"));
		dialog.addStylesheets(null);
	}
	
	@Test
	public void show()
	{
		Dialog dialog = waitFor(asyncFx(() -> {
			Dialog d = new Dialog();
			Button button = (Button) d.getDialogAlert().getDialogPane().lookupButton(ButtonType.OK);
			d.getDialogAlert().setOnShown((event) -> {
				Timeline timeline = new Timeline(new KeyFrame(
				        Duration.millis(500),
				        ae -> button.fire()));
				timeline.play();
			});
			return d;
		}));
		Optional<ButtonType> buttonType = waitFor(asyncFx(() -> {
			return dialog.show();
		}));
		assertEquals(ButtonType.OK, buttonType.get());
	}
}
