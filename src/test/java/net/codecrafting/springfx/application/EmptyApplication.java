package net.codecrafting.springfx.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.codecrafting.springfx.controls.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;

@SpringBootApplication
public class EmptyApplication extends SpringFXApplication
{
	@Override
	public void start(ViewStage viewStage) {}
}
