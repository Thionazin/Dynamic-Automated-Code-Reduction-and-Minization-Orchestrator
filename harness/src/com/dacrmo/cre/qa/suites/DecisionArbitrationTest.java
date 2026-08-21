package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.ai.arbitration.ArbitrationResult;
import com.dacrmo.cre.ai.arbitration.DecisionArbitrationService;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.qa.framework.SynergyTest;
import java.util.Arrays;

public class DecisionArbitrationTest
{
    private final FeatureExtractor extractor = new FeatureExtractor();
    private final double[][] features = {
            extractor.extract('a', 'b'),
            extractor.extract('a', '='),
            extractor.extract('-', '-'),
            extractor.extract(')', '{') };
    private final boolean[] ruleAnswers = { true, false, true, false };

    @SynergyTest("a neural network that always says keep is overruled wherever it is wrong")
    public void misalignedDecisionsAreDiscarded()
    {
        SpacePerceptron alwaysKeep = new SpacePerceptron();
        Arrays.fill(alwaysKeep.getWeights(), 10);
        ArbitrationResult result = new DecisionArbitrationService().arbitrate(alwaysKeep, features, ruleAnswers);
        assertTrue(Arrays.equals(ruleAnswers, result.getDecisions()), "a wrong learned decision reached the output");
        assertEquals(2, result.getOverruled(), "both wrong decisions should be overruled");
        assertEquals(2, result.getAccepted(), "both right decisions should be accepted");
    }

    @SynergyTest("a fully aligned neural network is never overruled")
    public void alignedDecisionsAreAccepted()
    {
        SpacePerceptron aligned = new SpacePerceptron();
        double[] weights = aligned.getWeights();
        for (int i = 0; i < features.length; i++)
        {
            for (int f = 0; f < weights.length; f++) if (features[i][f] == 1) weights[f] = ruleAnswers[i] ? 10 : -10;
        }
        ArbitrationResult result = new DecisionArbitrationService().arbitrate(aligned, features, ruleAnswers);
        assertTrue(Arrays.equals(ruleAnswers, result.getDecisions()), "aligned decisions should pass through unchanged");
        assertEquals(0, result.getOverruled(), "an aligned network should never be overruled");
    }
}
