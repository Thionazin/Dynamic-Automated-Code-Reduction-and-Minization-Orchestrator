package com.dacrmo.cre.ai.model;

// a single-layer neural network that predicts whether a space should be kept
public class SpacePerceptron
{
    private final double[] weights = new double[FeatureExtractor.FEATURE_COUNT];

    public double[] getWeights()
    {
        return weights;
    }

    public double predict(double[] x)
    {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) sum += weights[i] * x[i];
        return 1 / (1 + Math.exp(-sum));
    }

    public boolean decide(double[] x)
    {
        return predict(x) > 0.5;
    }

    // mean certainty over the four canonical character-class pairings
    public double confidence()
    {
        FeatureExtractor extractor = new FeatureExtractor();
        char[][] pairs = { {'a', 'b'}, {'+', '-'}, {'a', '+'}, {'+', 'a'} };
        double total = 0;
        for (char[] pair : pairs) total += Math.abs(predict(extractor.extract(pair[0], pair[1])) - 0.5) * 2;
        return total / pairs.length;
    }
}
