package com.liu.learnjava.metrics;

import static  java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

//性能监控
@Target(METHOD)
@Retention(RUNTIME)
public @interface MetricTime {
	String value();
}
