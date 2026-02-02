package com.dacrmo.cre.ai.model;

import com.dacrmo.cre.lexical.CharacterClass;

// one-hot encoding of the (previous class, next class) pair. There is no bias input: every example
// activates exactly one feature, so a shared bias would only let the common "drop" pairs drown out the rare "keep" ones
public class FeatureExtractor
{
    public static final int FEATURE_COUNT = CharacterClass.values().length * CharacterClass.values().length;

    public double[] extract(char prev, char next)
    {
        double[] x = new double[FEATURE_COUNT];
        x[CharacterClass.of(prev).ordinal() * CharacterClass.values().length + CharacterClass.of(next).ordinal()] = 1;
        return x;
    }
}
