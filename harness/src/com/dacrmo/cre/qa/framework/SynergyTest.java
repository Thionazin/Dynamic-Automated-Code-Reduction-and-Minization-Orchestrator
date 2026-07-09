package com.dacrmo.cre.qa.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// marks a method as a quality-assurance scenario for the Enterprise Test Orchestration Framework™
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SynergyTest
{
    String value() default "";
}
