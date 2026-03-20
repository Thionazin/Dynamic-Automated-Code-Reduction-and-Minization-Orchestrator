package com.dacrmo.cre.ai.arbitration;

public class ArbitrationResult
{
    private final boolean[] decisions;
    private final int accepted;
    private final int overruled;

    public ArbitrationResult(boolean[] decisions, int accepted, int overruled)
    {
        this.decisions = decisions;
        this.accepted = accepted;
        this.overruled = overruled;
    }

    public boolean[] getDecisions()
    {
        return decisions;
    }

    public int getAccepted()
    {
        return accepted;
    }

    public int getOverruled()
    {
        return overruled;
    }
}
