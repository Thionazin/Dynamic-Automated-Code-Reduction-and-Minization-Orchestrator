package com.dacrmo.cre.ai.hypergaussian;

// hyper-Gaussian (super-Gaussian) radial basis: exp(-(r^2 / 2 sigma^2)^order). Order 1 is a plain Gaussian;
// higher orders give the flat top and steep shoulders that keep each unit from interfering with its neighbors
public class HypergaussianActivationFunction
{
    private final double width;
    private final double order;

    public HypergaussianActivationFunction(double width, double order)
    {
        this.width = width;
        this.order = order;
    }

    public double activate(double[] x, double[] center)
    {
        double squaredDistance = 0;
        for (int i = 0; i < x.length; i++) squaredDistance += (x[i] - center[i]) * (x[i] - center[i]);
        return Math.exp(-Math.pow(squaredDistance / (2 * width * width), order));
    }
}
