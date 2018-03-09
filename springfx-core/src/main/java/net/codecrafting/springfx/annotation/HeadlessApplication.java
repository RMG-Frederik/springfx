/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import net.codecrafting.springfx.core.SpringFXContext;

/**
 * Annotation meant to be used on the main application class that indicates to the {@link SpringFXContext} 
 * {@link SpringApplicationBuilder} configure a {@link ConfigurableApplicationContext} with the headless option. Use
 * this annotation with {@literal true} value to use swing elements inside JavaFX.
 * 
 * @author Lucas Marotta
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeadlessApplication 
{
	/**
	 * The value indicating if the application is headless or not.  
	 * @return {@literal true} if the application is headless otherwise returns {@literal false}.
	 */
	boolean value() default true;
}
