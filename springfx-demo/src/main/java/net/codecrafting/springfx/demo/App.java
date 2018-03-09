package net.codecrafting.springfx.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.codecrafting.springfx.context.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;
import net.codecrafting.springfx.core.SpringFXLauncher;

@SpringBootApplication
public class App extends SpringFXApplication
{
    public static void main(String[] args)
	{
        try {
			SpringFXLauncher.launch(App.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public void start(ViewStage viewStage) throws Exception 
	{
		
	}
}