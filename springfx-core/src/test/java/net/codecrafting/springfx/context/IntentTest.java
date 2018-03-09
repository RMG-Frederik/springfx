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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import net.codecrafting.springfx.application.controllers.TestController;

public class IntentTest 
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void instantiation()
	{
		Intent intent = new Intent(null, null);
		assertNull(intent.getCallerContext());
		assertNull(intent.getViewClass());
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		intent = new Intent(mockContext, TestController.class);
		assertNotNull(intent.getCallerContext());
		assertNotNull(intent.getViewClass());
		assertNull(intent.getExtra("test"));
	}
	
	@Test
	public void getExtra()
	{
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		Intent intent = new Intent(mockContext, TestController.class);
		assertNull(intent.getExtra("test"));
		intent.putExtra("test", new Integer(1));
		assertEquals(new Integer(1), intent.getExtra("test"));
	}
	
	@Test
	public void putMultipleExtras()
	{
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		Intent intent = new Intent(mockContext, TestController.class);
		intent.putExtra("test", new Integer(1));
		intent.putExtra("test", "bananas");
		intent.putExtra("test2", "apple");
		intent.putExtra("test3", null);
		assertEquals("bananas", intent.getExtra("test"));
		assertEquals("apple", intent.getExtra("test2"));
		assertNull(intent.getExtra("test3"));
	}
	
	@Test
	public void putExtraFieldMustNotBeNull()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("field must not be null");
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		Intent intent = new Intent(mockContext, TestController.class);
		intent.putExtra(null, null);
	}
	
	@Test
	public void putExtraFieldMustNotBeEmpty()
	{
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("field must not be empty");
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		Intent intent = new Intent(mockContext, TestController.class);
		intent.putExtra("", null);
	}
	
	@Test
	public void clearExtra()
	{
		ViewContext mockContext = Mockito.mock(ViewContext.class);
		Intent intent = new Intent(mockContext, TestController.class);
		intent.clearExtra();
		assertNull(intent.getExtra("test"));
		intent.putExtra("test", "banana");
		intent.putExtra("test2", "apple");
		intent.clearExtra();
		assertNull(intent.getExtra("test"));
		assertNull(intent.getExtra("test2"));
		intent.putExtra("test", "apple");
		assertEquals("apple", intent.getExtra("test"));
	}
}
