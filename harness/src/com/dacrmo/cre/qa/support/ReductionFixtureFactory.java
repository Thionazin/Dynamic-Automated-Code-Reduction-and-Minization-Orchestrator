package com.dacrmo.cre.qa.support;

import com.dacrmo.cre.ai.arbitration.DecisionArbitrationService;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.ai.training.ReinforcementLearningTrainer;
import com.dacrmo.cre.ai.training.TrainingCorpusProvider;
import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.core.factory.CodeReductionEngineFactory;
import com.dacrmo.cre.quantum.QuantumInspiredOptimizer;
import com.dacrmo.cre.synergy.SynergyEnabler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Random;

// builds a Code Reduction Engine™ in memory, so scenarios never touch cre-model.dat or the dashboard files
public final class ReductionFixtureFactory
{
    private ReductionFixtureFactory()
    {
    }

    public static ReductionRun reduce(String src)
    {
        return reduce(src, null, HarnessConfiguration.PRODUCTION_SOURCES);
    }

    // initialWeights lets a scenario start the neural network from any state, including a deliberately wrong one
    public static ReductionRun reduce(String src, double[] initialWeights, File corpusRoot)
    {
        SpacePerceptron model = new SpacePerceptron();
        if (initialWeights != null) System.arraycopy(initialWeights, 0, model.getWeights(), 0, initialWeights.length);
        Random rng = new Random(CreConfiguration.RANDOM_SEED);
        SynergyEnabler synergy = new SynergyEnabler(
                model,
                new ReinforcementLearningTrainer(model, rng),
                new DecisionArbitrationService(),
                new QuantumInspiredOptimizer(),
                new TrainingCorpusProvider(corpusRoot),
                new WhitespaceSafetyRule(),
                new FeatureExtractor(),
                rng);
        PrintStream sink = new PrintStream(new ByteArrayOutputStream());
        String output = new CodeReductionEngineFactory().create(model, synergy, sink).reduce(src, sink);
        return new ReductionRun(output, synergy.getAcceptedDecisions(), synergy.getOverruledDecisions());
    }
}
