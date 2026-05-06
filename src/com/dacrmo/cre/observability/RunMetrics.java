package com.dacrmo.cre.observability;

public class RunMetrics
{
    private final String before;
    private final String after;
    private final long cpuNanos;
    private final double rewardRate;
    private final int episodes;
    private final int acceptedDecisions;
    private final int overruledDecisions;

    public RunMetrics(String before, String after, long cpuNanos, double rewardRate, int episodes, int acceptedDecisions, int overruledDecisions)
    {
        this.before = before;
        this.after = after;
        this.cpuNanos = cpuNanos;
        this.rewardRate = rewardRate;
        this.episodes = episodes;
        this.acceptedDecisions = acceptedDecisions;
        this.overruledDecisions = overruledDecisions;
    }

    public String getBefore()
    {
        return before;
    }

    public String getAfter()
    {
        return after;
    }

    public long getCpuNanos()
    {
        return cpuNanos;
    }

    public double getRewardRate()
    {
        return rewardRate;
    }

    public int getEpisodes()
    {
        return episodes;
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
