package com.dacrmo.cre.bench;

public class BenchmarkResult
{
    private final String workload;
    private final int charsIn;
    private final int charsOut;
    private final int iterations;
    private final double meanMillis;

    public BenchmarkResult(String workload, int charsIn, int charsOut, int iterations, double meanMillis)
    {
        this.workload = workload;
        this.charsIn = charsIn;
        this.charsOut = charsOut;
        this.iterations = iterations;
        this.meanMillis = meanMillis;
    }

    public String getWorkload()
    {
        return workload;
    }

    public int getCharsIn()
    {
        return charsIn;
    }

    public int getCharsOut()
    {
        return charsOut;
    }

    public int getIterations()
    {
        return iterations;
    }

    public double getMeanMillis()
    {
        return meanMillis;
    }

    public double getCharsPerSecond()
    {
        return meanMillis == 0 ? 0 : charsIn / (meanMillis / 1000);
    }
}
