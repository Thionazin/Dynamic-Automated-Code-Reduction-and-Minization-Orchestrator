package com.dacrmo.cre.qa.support;

public class Fixture
{
    private final String className;
    private final String source;

    public Fixture(String className, String source)
    {
        this.className = className;
        this.source = source;
    }

    public String getClassName()
    {
        return className;
    }

    public String getSource()
    {
        return source;
    }
}
