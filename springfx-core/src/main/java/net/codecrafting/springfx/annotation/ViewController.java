/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
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
package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.codecrafting.springfx.context.ViewContext;

/**
 * Annotation meant to be used by a {@link ViewContext} indicating that the class is a Spring {@link Component}.
 * Can be also used to customize the {@link ViewContext} viewName and viewTitle. If the name or the title 
 * was not informed the {@link ViewContext} will use the name of the superclass to define these values. This annotation
 * has a Spring {@link Lazy} annotation to delay the initialization of the controller bean.
 * 
 * @author Lucas Marotta
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Lazy
public @interface ViewController 
{
	/**
	 * The view name value to be informed to {@link ViewContext}
	 * @return the view name {@link String} value
	 */
	String name() default "";
	
	/**
	 * The view title name to be informed to {@link ViewContext}
	 * @return the view title name {@link String} vlaue
	 */
	String title() default "";
}
