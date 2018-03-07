package net.codecrafting.springfx.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import net.codecrafting.springfx.annotation.HeadlessApplication;
import net.codecrafting.springfx.context.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;

@SpringBootApplication
@HeadlessApplication(false)
public class AnnotatedTestApplication extends SpringFXApplication
{
	@Autowired
	private Environment env;

	@Autowired
	private ApplicationArguments args;
	
	private ViewStage viewStage;
	
	private boolean init = false;
	private boolean start = false;
	private boolean stop = false;
	
	@Override
	public void init() throws Exception 
	{
		init = true;
	}

	@Override
	public void start(ViewStage viewStage) throws Exception 
	{
		AnchorPane pane = new AnchorPane();
		pane.setPrefSize(400, 400);
		viewStage.setScene(new Scene(pane));
		if(!viewStage.isShowing()) {
			viewStage.show(true);
			viewStage.close();	
		}
		this.viewStage = viewStage;
		start = true;
	}
	
	@Override
	public void stop() throws Exception 
	{
		stop = true;
	}
	
	public boolean getInit()
	{
		return init;
	}
	
	public boolean getStart()
	{
		return start;
	}
	
	public boolean getStop()
	{
		return stop;
	}
	
	public ViewStage getViewStage()
	{
		return viewStage;
	}
	
	public Environment getEnvironment()
	{
		return env;
	}
	
	public ApplicationArguments getArgs()
	{
		return args;
	}
}
