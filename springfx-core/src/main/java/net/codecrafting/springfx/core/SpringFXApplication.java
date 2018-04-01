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
package net.codecrafting.springfx.core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.codecrafting.springfx.context.ViewStage;
import net.codecrafting.springfx.util.Dialog;
import net.codecrafting.springfx.util.DialogBuilder;

/**
 * Abstract class to be used on {@link BootstrapApplication} for the JAVAFX initialization process.
 * This class its similar to the JAVAFX {@link Application}, but instead of passing a traditional
 * {@link Stage}, it passes a {@link ViewStage}, so that would be possible to use the easy swappable 
 * view system @see ViewStage. It was opted to use a separate class to expose to the user the same fammilar
 * methods from {@link Application}.
 * 
 * @author Lucas Marotta
 * @see #init()
 * @see #start(ViewStage)
 * @see #stop()
 */

public abstract class SpringFXApplication 
{
    /**
     * The application initialization method. This method is called immediately
     * after the {@link BootstrapApplication} class is loaded and constructed. 
     * An application may override this method to perform initialization prior 
     * to the actual starting of the application.
     *
     * <p>
     * The implementation of this method provided by {@link SpringFXApplication} does nothing.
     * </p>
     * 
     * <p>
     * NOTE: the {@link BootstrapApplication} is initialized after the Spring Boot initialization.
     * </p>
     * 
     * <p>
     * NOTE: the {@link DialogBuilder#setProperties(java.util.Properties)} is called to load the user
     * {@link Dialog} properties
     * </p> 
     *
     * <p>
     * NOTE: This method is not called on the JavaFX Application Thread. An
     * application must not construct a Scene or a Stage in this
     * method.
     * An application may construct other JavaFX objects in this method.
     * </p>
	 * @throws Exception if the initialization fails
	 */
	public void init() throws Exception {}
	
	/**
     * The main entry point for all JavaFX applications.
     * The start method is called after the {@link #init()} method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     * 
	 * @param viewStage the ViewStage (common window) created by {@link BootstrapApplication}
	 * @throws Exception if the start process fails.
	 */
	public abstract void start(ViewStage viewStage) throws Exception;
	
    /**
     * This method is called when the application should stop, and provides a
     * convenient place to prepare for application exit and destroy resources.
     *
     * <p>
     * The implementation of this method provided by {@link SpringFXApplication} does nothing.
     * </p>
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     * 
     * <p>
     * NOTE: Don't use this directly to exit the application, but instead use {@link Platform#exit()}
     * that will eventually call this method.
     * </p>
     * 
	 * @throws Exception if the stop process fails.
	 */
	public void stop() throws Exception {}
}
