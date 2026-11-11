package com.dacrmo.cre.synergy;

import com.dacrmo.cre.ai.arbitration.ArbitrationResult;
import com.dacrmo.cre.ai.arbitration.DecisionArbitrationService;
import com.dacrmo.cre.ai.ensemble.SuperpositionEnsemble;
import com.dacrmo.cre.ai.hypergaussian.QuantumInspiredHypergaussianNetwork;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.ai.training.ReinforcementLearningTrainer;
import com.dacrmo.cre.ai.training.TrainingCorpusProvider;
import com.dacrmo.cre.quantum.QuantumInspiredOptimizer;
import java.util.List;
import java.util.Random;

// cross-functional synergy: the perceptron and the quantum-inspired hypergaussian network are placed in superposition,
// the ensemble seeds the quantum optimizer, and the optimizer's answer is fed back to train both networks
public class SynergyEnabler
{
    private final SpacePerceptron model;
    private final QuantumInspiredHypergaussianNetwork hypergaussianNetwork;
    private final SuperpositionEnsemble ensemble;
    private final ReinforcementLearningTrainer trainer;
    private final DecisionArbitrationService arbitrationService;
    private final QuantumInspiredOptimizer optimizer;
    private final TrainingCorpusProvider corpusProvider;
    private final WhitespaceSafetyRule rule;
    private final FeatureExtractor extractor;
    private final Random rng;
    private int acceptedDecisions;
    private int overruledDecisions;

    public SynergyEnabler(SpacePerceptron model, QuantumInspiredHypergaussianNetwork hypergaussianNetwork, ReinforcementLearningTrainer trainer, DecisionArbitrationService arbitrationService,
            QuantumInspiredOptimizer optimizer, TrainingCorpusProvider corpusProvider, WhitespaceSafetyRule rule,
            FeatureExtractor extractor, Random rng)
    {
        this.model = model;
        this.hypergaussianNetwork = hypergaussianNetwork;
        this.ensemble = new SuperpositionEnsemble(model, hypergaussianNetwork);
        this.trainer = trainer;
        this.arbitrationService = arbitrationService;
        this.optimizer = optimizer;
        this.corpusProvider = corpusProvider;
        this.rule = rule;
        this.extractor = extractor;
        this.rng = rng;
    }

    public boolean[] optimize(String src, List<Integer> spaces)
    {
        trainer.train(src, rule, extractor);
        hypergaussianNetwork.train(src, rule, extractor);
        for (String document : corpusProvider.corpus())
        {
            trainer.train(document, rule, extractor);
            hypergaussianNetwork.train(document, rule, extractor);
        }

        int n = spaces.size();
        double[] priors = new double[n];
        boolean[] ruleAnswers = new boolean[n];
        double[][] x = new double[n][];
        for (int i = 0; i < n; i++)
        {
            char prev = src.charAt(spaces.get(i) - 1);
            char next = src.charAt(spaces.get(i) + 1);
            x[i] = extractor.extract(prev, next);
            priors[i] = ensemble.predict(x[i]);
            ruleAnswers[i] = rule.needsSpace(prev, next);
        }

        ArbitrationResult arbitration = arbitrationService.arbitrate(ensemble, x, ruleAnswers);
        acceptedDecisions += arbitration.getAccepted();
        overruledDecisions += arbitration.getOverruled();

        boolean[] best = optimizer.evolve(priors, arbitration.getDecisions(), rng);
        for (int i = 0; i < n; i++)
        {
            trainer.reinforce(x[i], best[i]);
            hypergaussianNetwork.rotate(x[i], best[i]);
        }
        return best;
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
