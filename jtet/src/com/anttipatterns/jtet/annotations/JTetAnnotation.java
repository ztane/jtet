package com.anttipatterns.jtet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
public @interface JTetAnnotation {
	public static final String NULL = "\0Unset value\0";
}
