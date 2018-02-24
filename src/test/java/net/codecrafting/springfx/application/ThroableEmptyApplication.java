package net.codecrafting.springfx.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.codecrafting.springfx.controls.ViewStage;
import net.codecrafting.springfx.core.SpringFXApplication;

@SpringBootApplication
public class ThroableEmptyApplication extends SpringFXApplication
{
	public boolean throwInit = false;
	public boolean throwStart = false;
	public boolean throwStop = false;

	@Override
	public void init() {
		if(throwInit) throw new NullPointerException("Null Init");
	}

	@Override
	public void start(ViewStage viewStage) {
		if(throwStart) throw new NullPointerException("Null start");
	}

	@Override
	public void stop() {
		if(throwStop) throw new NullPointerException("Null stop");
	}

	public boolean isThrowInit() 
	{
		return throwInit;
	}

	public void setThrowInit(boolean throwInit) 
	{
		this.throwInit = throwInit;
	}

	public boolean isThrowStart() 
	{
		return throwStart;
	}

	public void setThrowStart(boolean throwStart) 
	{
		this.throwStart = throwStart;
	}

	public boolean isThrowStop() 
	{
		return throwStop;
	}

	public void setThrowStop(boolean throwStop) 
	{
		this.throwStop = throwStop;
	}
}
