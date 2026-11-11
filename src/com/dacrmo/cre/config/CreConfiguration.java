package com.dacrmo.cre.config;

import java.io.File;

public final class CreConfiguration
{
    public static final File INPUT_FILE = new File("changeThis.dat");
    public static final String INPUT_HINT_FILE = "Copy paste code to changeThis";
    public static final File OUTPUT_FILE = new File("output.txt");
    public static final File MODEL_FILE = new File("cre-model.dat");
    public static final File HISTORY_FILE = new File("cre-history.csv");
    public static final File KPI_REPORT_FILE = new File("cre-kpi-report.txt");
    public static final File TRAINING_CORPUS_ROOT = new File("src");

    public static final double LEARNING_RATE = 0.05;
    public static final double EPSILON = 0.1;
    public static final int GENERATIONS = 10;
    public static final int POPULATION = 4;
    public static final double ROTATION = 0.05 * Math.PI;
    public static final long RANDOM_SEED = 42;
    public static final double HYPERGAUSSIAN_WIDTH = 0.5;
    public static final double HYPERGAUSSIAN_ORDER = 2;

    private CreConfiguration()
    {
    }
}
