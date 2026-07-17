package com.dacrmo.cre.qa.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// instantiates each suite once per scenario and runs every @SynergyTest method in name order
public class TestExecutionOrchestrator
{
    public List<TestOutcome> execute(List<Class<?>> suites, TestReportRenderer renderer)
    {
        List<TestOutcome> outcomes = new ArrayList<>();
        for (Class<?> suite : suites)
        {
            Method[] methods = suite.getDeclaredMethods();
            Arrays.sort(methods, Comparator.comparing(Method::getName));
            for (Method method : methods)
            {
                SynergyTest annotation = method.getAnnotation(SynergyTest.class);
                if (annotation == null) continue;
                TestOutcome outcome = run(suite, method, annotation.value());
                renderer.onOutcome(outcome);
                outcomes.add(outcome);
            }
        }
        return outcomes;
    }

    private TestOutcome run(Class<?> suite, Method method, String description)
    {
        long start = System.nanoTime();
        TestOutcome.Status status = TestOutcome.Status.PASSED;
        Throwable cause = null;
        try
        {
            Object instance = suite.getDeclaredConstructor().newInstance();
            method.setAccessible(true);
            method.invoke(instance);
        }
        catch (InvocationTargetException e)
        {
            cause = e.getCause();
            status = cause instanceof AssertionViolationException ? TestOutcome.Status.FAILED : TestOutcome.Status.ERRORED;
        }
        catch (ReflectiveOperationException e)
        {
            cause = e;
            status = TestOutcome.Status.ERRORED;
        }
        long durationMillis = (System.nanoTime() - start) / 1000000;
        return new TestOutcome(suite.getSimpleName(), method.getName(), description, status, durationMillis, cause);
    }
}
