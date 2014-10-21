package com.anttipatterns.jtet.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anttipatterns.jtet.annotations.JTetAnnotation;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@JTetAnnotation
public @interface ViewConfigs {
    ViewConfig[] value();
}
