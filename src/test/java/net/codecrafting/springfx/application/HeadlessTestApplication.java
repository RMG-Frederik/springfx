package net.codecrafting.springfx.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.codecrafting.springfx.annotation.HeadlessApplication;
import net.codecrafting.springfx.controls.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;

@SpringBootApplication
@HeadlessApplication(false)
public class HeadlessTestApplication extends SpringFXApplication
{
	@Override
	public void start(ViewStage viewStage) throws Exception {}
}
