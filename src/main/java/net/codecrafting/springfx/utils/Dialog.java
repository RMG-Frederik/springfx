package net.codecrafting.springfx.utils;

import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Dialog
{
	private Image icon;
	private Alert alert;
	
	public static final AlertType INFO = AlertType.INFORMATION;
	public static final AlertType WARN = AlertType.WARNING;
	public static final AlertType ERROR = AlertType.ERROR;
	
	public Dialog()
	{
		this(AlertType.INFORMATION);
	}
	
	public Dialog(AlertType alertType) 
	{
		alert = new Alert(alertType);
		alert.getDialogPane().getStyleClass().add("springfx-dialog");
	}
	
	public Image getIcon()
	{
		return icon;
	}
	
	public Dialog setIcon(Image icon)
	{
		if(Platform.isFxApplicationThread()) {
			if(icon != null) {
				this.icon = icon;
				((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
				return this;		
			} else {
				throw new IllegalArgumentException("icon must not be null");
			}
		} else {
			throw new IllegalStateException("Not on JavaFX thread");
		}
	}
	
	public DialogPane getDialogPane()
	{
		return alert.getDialogPane();
	}
	
	public String getTitle()
	{
		return alert.getTitle();
	}
	
	public Dialog setTitle(String title)
	{
		if(title != null) {
			alert.setTitle(title);
			return this;	
		} else {
			throw new IllegalArgumentException("title must not be null");		
		}
	}
	
	public String getHeader()
	{
		return alert.getHeaderText();
	}
	
	public Dialog setHeader(String header)
	{
		if(header != null) {
			alert.setHeaderText(header);
			return this;	
		} else {
			throw new IllegalArgumentException("header must not be null");
		}
	}
	
	public String getContent()
	{
		return alert.getContentText();
	}
	
	public Dialog setContent(String content)
	{
		if(content != null) {
			alert.setContentText(content);	
			return this;
		} else {
			throw new IllegalArgumentException("content must not be null");
		}
	}
	
	public ObservableList<String> getStyleClass()
	{
		return getDialogPane().getStyleClass();
	}
	
	public Dialog setStylesheets(ObservableList<String> stylesheets)
	{
		if(stylesheets != null) {
			getDialogPane().getStylesheets().addAll(stylesheets);
			return this;
		} else {
			throw new IllegalArgumentException("stylesheets must not be null");
		}
	}
	
	public ObservableList<String> getStylesheets()
	{
		return getDialogPane().getStylesheets();
	}
	
	public Optional<ButtonType> show()
	{
		return alert.showAndWait();
	}

	protected Alert getDialogAlert()
	{
		return alert;
	}	
}
