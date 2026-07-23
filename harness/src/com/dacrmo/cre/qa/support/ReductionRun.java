package com.dacrmo.cre.qa.support;

public class ReductionRun
{
    private final String output;
    private final int acceptedDecisions;
    private final int overruledDecisions;

    public ReductionRun(String output, int acceptedDecisions, int overruledDecisions)
    {
        this.output = output;
        this.acceptedDecisions = acceptedDecisions;
        this.overruledDecisions = overruledDecisions;
    }

    public String getOutput()
    {
        return output;
    }

    public int getAcceptedDecisions()
    {
        return acceptedDecisions;
    }

    public int getOverruledDecisions()
    {
        return overruledDecisions;
    }
}
