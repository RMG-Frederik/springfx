/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
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
package net.codecrafting.springfx.util;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import net.codecrafting.springfx.exception.StageSystemTrayNotSupported;

public class StageSystemTray 
{
	private Stage stage;
	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private boolean supported = false;
	private Task<Void> addToTray;
	private static final Log LOGGER = LogFactory.getLog(StageSystemTray.class);
	
	public StageSystemTray(Stage stage, String icon, List<SystemTrayItem> items)
	{
		this.stage = stage;
		addToTray = new Task<Void>() {
			@Override
			protected Void call() throws Exception 
			{
				if(systemTray == null) {
					if(supported || SystemTray.isSupported()) {
						init(icon, items);
						systemTray.add(trayIcon);
						supported = true;
						return null;
					}
				} else {
					systemTray.add(trayIcon);
					return null;
				}
				throw new StageSystemTrayNotSupported();
			}
		};
	}
	
	public Task<Void> addToTray()
	{
		SwingUtilities.invokeLater(addToTray);
		return addToTray;
	}
	
	public void removeFromTray() throws StageSystemTrayNotSupported
	{
		if(systemTray != null && supported) {
			SwingUtilities.invokeLater(() -> {
				systemTray.remove(trayIcon);
			});
		} else {
			throw new StageSystemTrayNotSupported();
		}	
	}
	
	private void init(String icon, List<SystemTrayItem> items)
	{
		URL iconURL = StageSystemTray.class.getResource(icon);
		if(iconURL != null) {
			Image image = null;
			try {
				image = ImageIO.read(iconURL);
			} catch(Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			if(image != null) {
	            trayIcon = new TrayIcon(image);
	            trayIcon.setImageAutoSize(true);
			}
		}
		if(trayIcon != null) {
			systemTray = SystemTray.getSystemTray();
			trayIcon.addActionListener(event -> Platform.runLater(() -> {
				stage.show();
				stage.toFront();
			}));
			final PopupMenu popup = new PopupMenu();
			for (SystemTrayItem item : items) 
			{
				MenuItem menuItem = new MenuItem(item.getLabel());
				menuItem.addActionListener(item.getAction());
				popup.add(menuItem);
				popup.addSeparator();
			}			
			trayIcon.setPopupMenu(popup);
		}
	}
}
