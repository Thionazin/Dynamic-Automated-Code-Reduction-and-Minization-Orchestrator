package com.dacrmo.cre.qa.framework;

public class TestOutcome
{
    public enum Status { PASSED, FAILED, ERRORED }

    private final String suite;
    private final String scenario;
    private final String description;
    private final Status status;
    private final long durationMillis;
    private final Throwable cause;

    public TestOutcome(String suite, String scenario, String description, Status status, long durationMillis, Throwable cause)
    {
        this.suite = suite;
        this.scenario = scenario;
        this.description = description;
        this.status = status;
        this.durationMillis = durationMillis;
        this.cause = cause;
    }

    public String getSuite()
    {
        return suite;
    }

    public String getScenario()
    {
        return scenario;
    }

    public String getDescription()
    {
        return description;
    }

    public Status getStatus()
    {
        return status;
    }

    public long getDurationMillis()
    {
        return durationMillis;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
