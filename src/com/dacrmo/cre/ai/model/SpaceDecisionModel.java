package com.dacrmo.cre.ai.model;

// anything that can estimate the probability that a space should be kept
public interface SpaceDecisionModel
{
    double predict(double[] x);

    default boolean decide(double[] x)
    {
        return predict(x) > 0.5;
    }
}
