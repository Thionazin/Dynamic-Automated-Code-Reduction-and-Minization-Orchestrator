package com.dacrmo.cre.quantum;

import com.dacrmo.cre.config.CreConfiguration;
import java.util.Arrays;
import java.util.Random;

// Quantum-Inspired Evolutionary Algorithm: each removable space is a Q-bit whose rotation angle
// theta gives a keep-probability of sin²(theta)
public class QuantumInspiredOptimizer
{
    public boolean[] evolve(double[] priors, boolean[] oracle, Random rng)
    {
        int n = priors.length;
        double[] theta = new double[n];
        for (int i = 0; i < n; i++) theta[i] = Math.asin(Math.sqrt(priors[i]));

        boolean[] best = new boolean[n];
        Arrays.fill(best, true);
        int bestFitness = n + 1;
        for (int generation = 0; generation < CreConfiguration.GENERATIONS; generation++)
        {
            for (int c = 0; c < CreConfiguration.POPULATION; c++)
            {
                // measure each Q-bit, then repair any space the oracle requires
                boolean[] candidate = new boolean[n];
                int fitness = 0;
                for (int i = 0; i < n; i++)
                {
                    double amplitude = Math.sin(theta[i]);
                    candidate[i] = oracle[i] || rng.nextDouble() < amplitude * amplitude;
                    if (candidate[i]) fitness++;
                }
                // fitness is output length; fewer kept spaces is better
                if (fitness < bestFitness)
                {
                    best = candidate;
                    bestFitness = fitness;
                }
            }
            for (int i = 0; i < n; i++) theta[i] = Math.max(0, Math.min(Math.PI / 2, theta[i] + (best[i] ? CreConfiguration.ROTATION : -CreConfiguration.ROTATION)));
        }

        // greedy local search: drop every kept space that the oracle doesn't need
        for (int i = 0; i < n; i++) best[i] = best[i] && oracle[i];
        return best;
    }
}
