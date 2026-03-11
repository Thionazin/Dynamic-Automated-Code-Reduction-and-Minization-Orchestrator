package com.dacrmo.cre.ai.training;

import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import java.util.Random;

// the most basic reinforcement learning there is
public class ReinforcementLearningTrainer
{
    private final SpacePerceptron model;
    private final Random rng;
    private int episodes;
    private int rewards;

    public ReinforcementLearningTrainer(SpacePerceptron model, Random rng)
    {
        this.model = model;
        this.rng = rng;
    }

    // epsilon-greedy action, reward from the environment, push the weights toward rewarded actions
    public void reinforce(double[] x, boolean environmentAnswer)
    {
        boolean action = rng.nextDouble() < CreConfiguration.EPSILON ? rng.nextBoolean() : model.decide(x);
        int reward = action == environmentAnswer ? 1 : -1;
        double[] weights = model.getWeights();
        for (int i = 0; i < weights.length; i++) weights[i] += CreConfiguration.LEARNING_RATE * reward * (action ? 1 : -1) * x[i];
        episodes++;
        if (reward > 0) rewards++;
    }

    // self-supervised: the corpus labels itself through the safety rule
    public void train(String corpus, WhitespaceSafetyRule rule, FeatureExtractor extractor)
    {
        for (int index : JavaLexicalUtils.spacesOutsideLiterals(corpus))
        {
            char prev = corpus.charAt(index - 1);
            char next = corpus.charAt(index + 1);
            reinforce(extractor.extract(prev, next), rule.needsSpace(prev, next));
        }
    }

    public int getEpisodes()
    {
        return episodes;
    }

    public double getRewardRate()
    {
        return episodes == 0 ? 0 : (double) rewards / episodes;
    }
}
