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
package net.codecrafting.springfx.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.codecrafting.springfx.context.ViewStage;
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
