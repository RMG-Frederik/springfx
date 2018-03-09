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
package net.codecrafting.springfx.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ResourceBundle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.application.controllers.AnnotationController;
import net.codecrafting.springfx.application.controllers.TestController;
import net.codecrafting.springfx.application.controllers.TestWithoutAnnotationController;

public class ViewContextTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void contextWithoutControllerName()
	{
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Class name must ends with \"Controller\" or use ViewController annotation instead");
		new ViewContext() {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
	}
	
	@Test
	public void instantiationWithViewName()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("viewName must not be null");
		
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		assertEquals("test", context.getViewName());
		assertEquals("test title", context.getViewTitle());
		
		context = new ViewContext("test", null) {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		assertEquals("test", context.getViewName());
		assertNull(context.getViewTitle());
		
		new ViewContext(null, "test") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
	}
	
	@Test
	public void contextWithoutViewControllerAnnotation()
	{
		ViewContext context = new TestWithoutAnnotationController();
		assertEquals("test_without_annotation", context.getViewName());
		assertEquals("test_without_annotation", context.getViewTitle());
	}
	
	@Test
	public void contextWithViewControllerAnnotation()
	{
		ViewContext context = new TestController();
		assertEquals("test", context.getViewName());
		assertTrue("viewTitle is not empty", context.getViewTitle().isEmpty());
	}
	
	@Test
	public void contextWithViewControllerAnnotationName()
	{
		ViewContext context = new AnnotationController();
		assertEquals("ann", context.getViewName());
		assertEquals("test", context.getViewTitle());
	}
	
	@Test
	public void initialize() throws Exception
	{
		AnnotationController context = new AnnotationController();
		URL url = new URL("http://google.com");
		ResourceBundle mockResources = Mockito.mock(ResourceBundle.class);
		context.initialize(url, mockResources);
		assertEquals(url, context.getLocation());
		assertEquals(mockResources, context.getResources());
		context.initialize(null, null);
		assertNull(context.getLocation());
		assertNull(context.getResources());
	}

	@Test
	public void setViewStage()
	{
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("viewStage is already loaded for this context");
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		
		ViewStage mockStage = Mockito.mock(ViewStage.class);
		Mockito.when(mockStage.isInitialized()).thenReturn(true);
		Mockito.when(mockStage.getViewPath()).thenReturn("view path");
		context.setViewStage(mockStage);
		assertEquals("view path", context.getViewStage().getViewPath());
		context.setViewStage(mockStage);
	}
	
	@Test
	public void setBadViewStage()
	{
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		
		try {
			context.setViewStage(null);
			assertFalse("IllegalArgumentException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("viewStage must not be null", e.getMessage());
		}
		
		ViewStage mockStage = Mockito.mock(ViewStage.class);
		Mockito.when(mockStage.isInitialized()).thenReturn(false);
		try {
			context.setViewStage(mockStage);
			assertFalse("IllegalStateException not thrown", true);
		} catch(Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
			assertEquals("viewStage is not initialized", e.getMessage());
		}
	}
	
	@Test
	public void contextWithoutViewStage()
	{
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		assertNull(context.getIntent());
		assertNull(context.getStageContext());
	}
	
	@Test
	public void getIntent()
	{
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		
		ViewStage mockStage = Mockito.mock(ViewStage.class);
		Intent mockIntent = Mockito.mock(Intent.class);
		Mockito.doReturn(TestController.class).when(mockIntent).getViewClass();
		Mockito.when(mockStage.getIntent()).thenReturn(mockIntent);
		Mockito.when(mockStage.isInitialized()).thenReturn(true);
		Mockito.when(mockStage.getIntent()).thenReturn(mockIntent);
		context.setViewStage(mockStage);
		assertEquals(TestController.class, context.getIntent().getViewClass());
	}
	
	@Test
	public void getStageContext()
	{
		ViewContext context = new ViewContext("test", "test title") {
			@Override
			protected void onStart() {}
			
			@Override
			protected void onCreate(URL location, ResourceBundle resources) {}
			
			@Override
			public Node getMainNode() {return null;}
		};
		
		ViewStage mockStage = Mockito.mock(ViewStage.class);
		StageContext mockStageContext = Mockito.mock(StageContext.class);
		AnchorPane pane = new AnchorPane();
		pane.setId("test-pane");
		Mockito.when(mockStageContext.getMainNode()).thenReturn(pane);
		Mockito.when(mockStage.isInitialized()).thenReturn(true);
		Mockito.when(mockStage.getStageContext()).thenReturn(mockStageContext);
		context.setViewStage(mockStage);
		assertEquals("test-pane", context.getStageContext().getMainNode().getId());
	}
}
