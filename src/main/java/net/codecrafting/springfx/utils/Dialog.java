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
		this.icon = icon;
		Platform.runLater(() -> {
			((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
		});
		return this;
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
		alert.setTitle(title);
		return this;
	}
	
	public String getHeader()
	{
		return alert.getHeaderText();
	}
	
	public Dialog setHeader(String header)
	{
		alert.setHeaderText(header);
		return this;
	}
	
	public String getContent(String content)
	{
		return alert.getContentText();
	}
	
	public Dialog setContent(String content)
	{
		alert.setContentText(content);
		return this;
	}
	
	public ObservableList<String> getStyleClass()
	{
		return getDialogPane().getStyleClass();
	}
	
	public void setStylesheets(ObservableList<String> stylesheets)
	{
		getDialogPane().getStylesheets().addAll(stylesheets);
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
