package net.codecrafting.springfx.demo.controllers;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfoenix.controls.JFXSpinner;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.codecrafting.springfx.animation.AnimationBuilder;
import net.codecrafting.springfx.animation.EaseInterpolator;
import net.codecrafting.springfx.annotation.ViewController;
import net.codecrafting.springfx.context.Intent;
import net.codecrafting.springfx.context.StageContext;
import net.codecrafting.springfx.core.SpringFXLauncher;
import net.codecrafting.springfx.util.DialogBuilder;

@ViewController
public class MainController extends StageContext
{
	@FXML
	private StackPane mainNode;
	@FXML
	private AnchorPane contentScreen;
	@FXML
	private FlowPane splashScreen;
	@FXML
	private VBox splashMainContent;
	@FXML
	private ImageView splashLogo;
	@FXML
	private JFXSpinner splashLoader;
	
	private static final Log LOGGER = LogFactory.getLog(MainController.class);
	
	@Override
	protected void onCreate()
	{
		LOGGER.info("MAIN CONTROLLER STARTUP");
		new AnimationBuilder()
		.interpolator(EaseInterpolator.EASE_OUT_QUAD)
		.fadeIn(splashMainContent)
		.onFinished((onFinishedFade) -> {
			new AnimationBuilder(1.5).tada(splashLogo).onFinished((onFinishedTada) -> {
				Task<Void> loadTask = getLoadTask();
				loadTask.setOnSucceeded((onSuccess) -> {
					new AnimationBuilder(.4, .1)
					.interpolator(EaseInterpolator.EASE_OUT_QUAD)
					.zoomOut(splashScreen)
					.onFinished((onFinished) -> {
						splashScreen.setVisible(false);
						splashScreen.setDisable(true);
						getViewStage().loadIntent(new Intent(this, LoginController.class));
					}).play();
				});
				loadTask.setOnFailed((onFail) -> {
					Optional<ButtonType> btnType = DialogBuilder.showError("Failed to load application");
					if(btnType.get().equals(ButtonType.OK)) {
						SpringFXLauncher.exit();
					}
				});
			}).play();
		}).play();
	}

	@Override
	protected void onStart() 
	{
		
	}	
	
	@Override
	public void setViewStageTitle(String title) 
	{
		
	}

	@Override
	public AnchorPane getMainNode() 
	{
		return contentScreen;
	}
	
	private Task<Void> getLoadTask()
	{
		Task<Void> task = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception 
			{
				if(!isCancelled()) {
					//Simulating some initialization
					Thread.sleep(5000);
				}
				return null;
			}
		};
		new Thread(task).start();
		return task;
	}
}
