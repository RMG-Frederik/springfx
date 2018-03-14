package net.codecrafting.springfx.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import net.codecrafting.springfx.context.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;
import net.codecrafting.springfx.core.SpringFXLauncher;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class App extends SpringFXApplication
{
	private static final Log LOGGER = LogFactory.getLog(App.class);
	
    public static void main(String[] args)
	{
        try {
			SpringFXLauncher.launch(App.class, args);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
    }

	@Override
	public void start(ViewStage viewStage) throws Exception 
	{
		viewStage.setMinHeight(viewStage.getHeight());
		viewStage.setMinWidth(viewStage.getWidth());
	}
}