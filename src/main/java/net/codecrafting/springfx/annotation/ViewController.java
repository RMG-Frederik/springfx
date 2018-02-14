package net.codecrafting.springfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Lazy
public @interface ViewController 
{
	String name() default "";
	String title() default "";
}
