package com.dacrmo.cre.ai.arbitration;

import com.dacrmo.cre.ai.model.SpaceDecisionModel;

// the neural network's (or ensemble's) decision is used unless it contradicts the safety rule,
// in which case the learned result is thrown out and the rule's answer is used
public class DecisionArbitrationService
{
    public ArbitrationResult arbitrate(SpaceDecisionModel model, double[][] features, boolean[] ruleAnswers)
    {
        boolean[] decisions = new boolean[features.length];
        int accepted = 0;
        int overruled = 0;
        for (int i = 0; i < features.length; i++)
        {
            boolean learned = model.decide(features[i]);
            if (learned != ruleAnswers[i])
            {
                decisions[i] = ruleAnswers[i];
                overruled++;
            }
            else
            {
                decisions[i] = learned;
                accepted++;
            }
        }
        return new ArbitrationResult(decisions, accepted, overruled);
    }
}
