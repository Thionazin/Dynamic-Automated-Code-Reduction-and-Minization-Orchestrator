package com.dacrmo.cre.ai.ensemble;

import com.dacrmo.cre.ai.model.SpaceDecisionModel;

// combines models the quantum-inspired way: average their amplitudes (square roots of probability),
// then square the result, so confident agreement interferes constructively and disagreement cancels out
public class SuperpositionEnsemble implements SpaceDecisionModel
{
    private final SpaceDecisionModel[] members;

    public SuperpositionEnsemble(SpaceDecisionModel... members)
    {
        this.members = members;
    }

    public double predict(double[] x)
    {
        double amplitude = 0;
        for (SpaceDecisionModel member : members) amplitude += Math.sqrt(member.predict(x));
        amplitude /= members.length;
        return amplitude * amplitude;
    }
}
