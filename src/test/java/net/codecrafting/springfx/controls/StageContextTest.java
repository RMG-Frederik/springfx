package net.codecrafting.springfx.controls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ResourceBundle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class StageContextTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void swapContentBadArgument()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("viewController must not be null");
		StageContext context = new StageContext("test", "test title") {
			
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public void setViewStageTitle(String title) {}
			
			@Override
			public AnchorPane getMainNode() {return null;}
		};
		context.swapContent(null);
	}
	
	@Test
	public void swapContentBadContent()
	{
		StageContext context = new StageContext("test", "test title") {
			
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public void setViewStageTitle(String title) {}
			
			@Override
			public AnchorPane getMainNode() {return null;}
		};
		ViewContext viewController = Mockito.mock(ViewContext.class);
		Mockito.when(viewController.getMainNode()).thenReturn(null);
		
		try {
			context.swapContent(viewController);
			assertFalse("NullPointerException not thrown", true);
		} catch(Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
			assertEquals("StageContext mainNode and content must not be null", e.getMessage());
		}
		
		context = new StageContext("test", "test title") {
			
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public void setViewStageTitle(String title) {}
			
			@Override
			public AnchorPane getMainNode() {return null;}
		};
		Mockito.reset(viewController);
		Mockito.when(viewController.getMainNode()).thenReturn(new AnchorPane());
		
		try {
			context.swapContent(viewController);
			assertFalse("NullPointerException not thrown", true);
		} catch(Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
			assertEquals("StageContext mainNode and content must not be null", e.getMessage());
		}
		
		context = new StageContext("test", "test title") {
			
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public void setViewStageTitle(String title) {}
			
			@Override
			public AnchorPane getMainNode() {return new AnchorPane();}
		};
		Mockito.reset(viewController);
		Mockito.when(viewController.getMainNode()).thenReturn(null);
		
		try {
			context.swapContent(viewController);
			assertFalse("NullPointerException not thrown", true);
		} catch(Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
			assertEquals("StageContext mainNode and content must not be null", e.getMessage());
		}		
	}
	
	@Test
	public void swapContent()
	{
		StageContext context = new StageContext("test", "test title") {
			
			private AnchorPane pane;
			
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public void setViewStageTitle(String title) {}
			
			@Override
			public AnchorPane getMainNode() 
			{
				if(pane == null) {
					pane = new AnchorPane();
					pane.setId("stage-main-node");	
				}
				return pane;
			}
		};
		ViewContext viewController = Mockito.mock(ViewContext.class);
		FlowPane pane = new FlowPane();
		pane.setId("view-main-node");
		Mockito.when(viewController.getMainNode()).thenReturn(pane);
		context.swapContent(viewController);
		assertNotNull("viewController main node not found", context.getMainNode().lookup("#view-main-node"));
		Mockito.verify(viewController, Mockito.times(1)).swapAnimation(ArgumentMatchers.any());
		pane.setVisible(false);
		pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		pane.setPrefSize(250, 250);
		context.swapContent(viewController);
		assertEquals(1, context.getMainNode().getChildren().size());
		assertEquals(0.0, pane.getProperties().get("pane-top-anchor"));
		assertEquals(0.0, pane.getProperties().get("pane-left-anchor"));
		assertEquals(0.0, pane.getProperties().get("pane-bottom-anchor"));
		assertEquals(0.0, pane.getProperties().get("pane-right-anchor"));
		assertTrue("viewController main node still not visible", pane.isVisible());
		assertEquals(250.0, pane.getPrefWidth(), 0.0);
		assertEquals(250.0, pane.getPrefHeight(), 0.0);
	}
}
