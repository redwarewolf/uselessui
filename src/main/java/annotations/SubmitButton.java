package main.java.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("rawtypes")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubmitButton
{
	String label() default "Submit";
	Class nextForm() default void.class;
}
