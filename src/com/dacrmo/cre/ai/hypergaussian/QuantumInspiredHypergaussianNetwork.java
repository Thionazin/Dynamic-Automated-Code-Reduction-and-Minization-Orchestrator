package com.dacrmo.cre.ai.hypergaussian;

import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpaceDecisionModel;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.lexical.JavaLexicalUtils;

// a radial basis network of hyper-Gaussian units, one centred on each feature. Each unit's output weight is a Q-bit:
// an angle theta read out by the Born rule as sin²(theta), and trained by a quantum rotation gate instead of gradients
public class QuantumInspiredHypergaussianNetwork implements SpaceDecisionModel
{
    private final HypergaussianActivationFunction activation =
            new HypergaussianActivationFunction(CreConfiguration.HYPERGAUSSIAN_WIDTH, CreConfiguration.HYPERGAUSSIAN_ORDER);
    private final double[][] centers = new double[FeatureExtractor.FEATURE_COUNT][FeatureExtractor.FEATURE_COUNT];
    private final double[] theta = new double[FeatureExtractor.FEATURE_COUNT];
    private int rotations;

    public QuantumInspiredHypergaussianNetwork()
    {
        for (int k = 0; k < centers.length; k++)
        {
            centers[k][k] = 1;
            theta[k] = Math.PI / 4; // uniform superposition: no opinion yet
        }
    }

    public int getUnitCount()
    {
        return centers.length;
    }

    public int getRotations()
    {
        return rotations;
    }

    public double predict(double[] x)
    {
        double weighted = 0;
        double total = 0;
        for (int k = 0; k < centers.length; k++)
        {
            double phi = activation.activate(x, centers[k]);
            double amplitude = Math.sin(theta[k]);
            weighted += phi * amplitude * amplitude;
            total += phi;
        }
        return total == 0 ? 0.5 : weighted / total;
    }

    // rotation gate: turn every Q-bit toward the observed answer, in proportion to how strongly its unit fired
    public void rotate(double[] x, boolean answer)
    {
        for (int k = 0; k < centers.length; k++)
        {
            double step = CreConfiguration.ROTATION * activation.activate(x, centers[k]) * (answer ? 1 : -1);
            theta[k] = Math.max(0, Math.min(Math.PI / 2, theta[k] + step));
        }
        rotations++;
    }

    // self-supervised, like the perceptron: the safety rule labels every space in the corpus
    public void train(String corpus, WhitespaceSafetyRule rule, FeatureExtractor extractor)
    {
        for (int index : JavaLexicalUtils.spacesOutsideLiterals(corpus))
        {
            char prev = corpus.charAt(index - 1);
            char next = corpus.charAt(index + 1);
            rotate(extractor.extract(prev, next), rule.needsSpace(prev, next));
        }
    }

    // mean distance of each Q-bit from uniform superposition, 0 (undecided) to 1 (fully collapsed)
    public double certainty()
    {
        double total = 0;
        for (double t : theta) total += Math.abs(Math.sin(t) * Math.sin(t) - 0.5) * 2;
        return total / theta.length;
    }
}
