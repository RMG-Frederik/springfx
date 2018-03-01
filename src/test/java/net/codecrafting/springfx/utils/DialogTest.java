package net.codecrafting.springfx.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.concurrent.CountDownLatch;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.control.Alert.AlertType;
import net.codecrafting.springfx.core.BootstrapApplication;

@SuppressWarnings("restriction")
public class DialogTest 
{
	@BeforeClass
	public static void setup() throws InterruptedException
	{
		if(!BootstrapApplication.isToolkitInitialized()) {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			PlatformImpl.startup(() -> {
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
}
