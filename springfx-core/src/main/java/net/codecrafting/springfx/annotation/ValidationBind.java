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

import net.codecrafting.springfx.context.ViewContext;
import net.codecrafting.springfx.validation.FormModel;
import net.codecrafting.springfx.validation.FormValidator;
import net.codecrafting.springfx.validation.ValidationModel;

/**
 * Annotation meant to be used for the validation process with the {@link FormValidator} and {@link FormModel}.
 * Use this annotation to indicate that a field from {@link ViewContext} has to be validated. This annotation
 * also is used to bind the name of the {@link ViewContext} (or vice versa) to the corresponding implementation 
 * {@link ValidationModel} attribute value.
 * @author Lucas Marotta
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationBind 
{
	/**
	 * The value indicating the name of field to be binded to the {@link ValidationModel}
	 * or {@link ViewContext}.
	 * @return the attribute name to be binded
	 */
	String value() default "";
}
