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

import javafx.scene.layout.Region;
import net.codecrafting.springfx.context.Intent;
import net.codecrafting.springfx.context.ViewContext;

/**
 * Annotation meant to be used at a {@link Region} attribute from a {@link ViewContext} JavaFX controller. This annotation
 * indicate that the {@link Region} will trigger a new {@link Intent} to the informed {@link ViewContext} when is clicked.
 * 
 * @author Lucas Marotta
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewLink 
{
	/**
	 * The value to be used to create a new {@link Intent} to the desired {@link ViewContext}
	 * when the associated {@link Region} is clicked.
	 * @return the {@link ViewContext} to create a new {@link Intent}.
	 */
	Class<? extends ViewContext> value();
}
