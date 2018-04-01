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
package net.codecrafting.springfx.util;

import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class is used to abstract a common use of JavaFX {@link Alert} dialogs. The goal is to provide 
 * a standard and chainable methods for {@link DialogBuilder} to easily build {@link Alert}s.
 * @author Lucas Marotta
 * @see #getIcon()
 * @see #setIcon(Image)
 * @see #getDialogPane()
 * @see #getTitle()
 * @see #setTitle(String)
 * @see #getHeader()
 * @see #setHeader(String)
 * @see #getContent()
 * @see #setContent(String)
 * @see #getStyleClass()
 * @see #addStylesheets(ObservableList)
 * @see #getStylesheets()
 * @see #show()
 * @see #getDialogAlert()
 */
public class Dialog
{
	/**
	 * The {@link Alert} {@link Stage} icon
	 */
	private Image icon;
	
	/**
	 * The internal {@link Alert}.
	 */
	private Alert alert;
	
	/**
	 * Short name for the typical {@link AlertType#INFORMATION} type
	 */
	public static final AlertType INFO = AlertType.INFORMATION;
	
	/**
	 * Short name for the typical {@link AlertType#WARNING} type
	 */
	public static final AlertType WARN = AlertType.WARNING;
	
	/**
	 * Short name for the typical {@link AlertType#ERROR} type
	 */
	public static final AlertType ERROR = AlertType.ERROR;
	
	/**
	 * Create a new instance of {@link Dialog}. This constructor uses the {@link AlertType#INFORMATION} type.
	 * The css class "springfx-dialog" will be added to the internal {@link Alert} dialog pane.
	 */
	public Dialog()
	{
		this(AlertType.INFORMATION);
	}
	
	/**
	 * Create a new instance of {@link Dialog}. This constructor allow a custom {@link AlertType} for
	 * the internal {@link Alert}. The css class "springfx-dialog" will be added to the internal 
	 * {@link Alert} dialog pane.
	 * @param alertType the {@link AlertType} to be passed for {@link Alert} instance.
	 */
	public Dialog(AlertType alertType) 
	{
		alert = new Alert(alertType);
		alert.getDialogPane().getStyleClass().add("springfx-dialog");
	}
	
	/**
	 * Get the last configured icon for the {@link Alert} {@link Stage}.
	 * @return the icon {@link Image}
	 */
	public Image getIcon()
	{
		return icon;
	}
	
	/**
	 * Set a {@link Image} icon to be set on the {@link Alert} {@link Stage}. Any images
	 * present on the {@link Alert} {@link Stage} will be removed before adding the new 
	 * image.
	 * <br><b>NOTE:</b>This method must be called on the JavaFX Application Thread.
	 * @param icon the {@link Image}
	 * @return this {@link Dialog} instance
	 * @throws IllegalArgumentException if icon is null
	 * @throws IllegalStateException if not on JavaFX Application thread.
	 */
	public Dialog setIcon(Image icon)
	{
		if(Platform.isFxApplicationThread()) {
			if(icon != null) {
				final Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().clear();
				stage.getIcons().add(icon);
				this.icon = icon;
				return this;	
			} else {
				throw new IllegalArgumentException("icon must not be null");
			}
		} else {
			throw new IllegalStateException("Not on JavaFX thread");
		}
	}
	
	/**
	 * Get the internal {@link Alert} {@link DialogPane}. This dialogPane represents
	 * the content of {@link Alert} {@link Stage} {@link Scene}.
	 * @return the {@link DialogPane}.
	 */
	public DialogPane getDialogPane()
	{
		return alert.getDialogPane();
	}
	
	/**
	 * Get the internal {@link Alert} {@link Stage} title.
	 * @return the {@link String} title.
	 */
	public String getTitle()
	{
		return alert.getTitle();
	}
	
	/**
	 * Set the internal {@link Alert} {@link Stage} title.
	 * @param title to be set on the {@link Alert} {@link Stage}
	 * @return this {@link Dialog} instance.
	 * @throws IllegalArgumentException if title is null
	 */
	public Dialog setTitle(String title)
	{
		if(title != null) {
			alert.setTitle(title);
			return this;	
		} else {
			throw new IllegalArgumentException("title must not be null");		
		}
	}
	
	/**
	 * Get the internal {@link Alert} {@link DialogPane} header.
	 * @return the {@link String} title.
	 */
	public String getHeader()
	{
		return alert.getHeaderText();
	}
	
	/**
	 * Set the internal {@link Alert} {@link DialogPane} header.
	 * @param header {@link String} to be set on the {@link Alert} {@link DialogPane}
	 * @return this {@link Dialog} instance.
	 * @throws IllegalArgumentException if header is null
	 */
	public Dialog setHeader(String header)
	{
		if(header != null) {
			alert.setHeaderText(header);
			return this;	
		} else {
			throw new IllegalArgumentException("header must not be null");
		}
	}
	
	/**
	 * Get the internal {@link Alert} {@link DialogPane} {@link String} content.
	 * @return the {@link String} content.
	 */
	public String getContent()
	{
		return alert.getContentText();
	}
	
	/**
	 * Set the internal {@link Alert} {@link DialogPane} {@link String} content.
	 * @param content {@link String} to be set on the {@link Alert} {@link DialogPane}
	 * @return this {@link Dialog} instance.
	 * @throws IllegalArgumentException if content is null
	 */
	public Dialog setContent(String content)
	{
		if(content != null) {
			alert.setContentText(content);	
			return this;
		} else {
			throw new IllegalArgumentException("content must not be null");
		}
	}
	
	/**
	 * Get the the internal {@link Alert} {@link DialogPane} {@link ObservableList} style class.
	 * @return the {@link ObservableList} style class
	 */
	public ObservableList<String> getStyleClass()
	{
		return getDialogPane().getStyleClass();
	}
	
	/**
	 * Add style sheets to the internal {@link Alert} {@link DialogPane}.
	 * @param stylesheets the {@link ObservableList} style sheet
	 * @return this {@link Dialog} instance.
	 * @throws IllegalArgumentException if stylesheets is null
	 */
	public Dialog addStylesheets(ObservableList<String> stylesheets)
	{
		if(stylesheets != null) {
			getDialogPane().getStylesheets().addAll(stylesheets);
			return this;
		} else {
			throw new IllegalArgumentException("stylesheets must not be null");
		}
	}
	
	/**
	 * Get the style sheets from the internal {@link Alert} {@link DialogPane}.
	 * @return the {@link ObservableList} style sheet
	 */
	public ObservableList<String> getStylesheets()
	{
		return getDialogPane().getStylesheets();
	}
	
    /**
     * Shows the internal {@link Alert} and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * <p>
     * This method must be called on the JavaFX Application thread.
     * Additionally, it must either be called from an input event handler or
     * from the run method of a Runnable passed to
     * {@link javafx.application.Platform#runLater Platform.runLater}.
     * It must not be called during animation or layout processing.
     * </p>
     *
     * @return An {@link Optional} that contains the {@link javafx.scene.control.Dialog#resultProperty() result}.
     *         Refer to the {@link javafx.scene.control.Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
	public Optional<ButtonType> show()
	{
		return alert.showAndWait();
	}
	
	protected Alert getDialogAlert()
	{
		return alert;
	}	
}
