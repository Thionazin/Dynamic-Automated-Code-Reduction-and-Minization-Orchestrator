package com.dacrmo.cre.bench;

import com.dacrmo.cre.ai.arbitration.DecisionArbitrationService;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.ai.training.ReinforcementLearningTrainer;
import com.dacrmo.cre.ai.training.TrainingCorpusProvider;
import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.core.factory.CodeReductionEngineFactory;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.quantum.QuantumInspiredOptimizer;
import com.dacrmo.cre.synergy.SynergyEnabler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// measures Code Reduction Engine™ throughput on every fixture plus the platform's own source,
// prints a hyper-scalability table and writes benchmarks/results/latest.csv
public final class PerformanceBenchmarkOrchestrator
{
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASURED_ITERATIONS = 20;
    private static final File CORPUS_ROOT = new File("src");

    private PerformanceBenchmarkOrchestrator()
    {
    }

    public static void main(String[] args) throws IOException
    {
        List<Path> workloads = new ArrayList<>(javaFiles(new File("fixtures/java")));
        List<BenchmarkResult> results = new ArrayList<>();
        for (Path workload : workloads) results.add(measure(workload.getFileName().toString(), JavaLexicalUtils.read(workload.toFile())));

        StringBuilder platform = new StringBuilder();
        for (Path source : javaFiles(CORPUS_ROOT)) platform.append(JavaLexicalUtils.read(source.toFile())).append('\n');
        results.add(measure("<entire platform source>", platform.toString()));

        render(results);
        writeCsv(results, new File("benchmarks/results/latest.csv"));
    }

    private static BenchmarkResult measure(String name, String src)
    {
        String output = "";
        for (int i = 0; i < WARMUP_ITERATIONS; i++) output = reduce(src);
        long start = System.nanoTime();
        for (int i = 0; i < MEASURED_ITERATIONS; i++) output = reduce(src);
        double meanMillis = (System.nanoTime() - start) / 1e6 / MEASURED_ITERATIONS;
        return new BenchmarkResult(name, src.length(), output.length(), MEASURED_ITERATIONS, meanMillis);
    }

    private static String reduce(String src)
    {
        SpacePerceptron model = new SpacePerceptron();
        Random rng = new Random(CreConfiguration.RANDOM_SEED);
        SynergyEnabler synergy = new SynergyEnabler(model, new ReinforcementLearningTrainer(model, rng), new DecisionArbitrationService(),
                new QuantumInspiredOptimizer(), new TrainingCorpusProvider(CORPUS_ROOT), new WhitespaceSafetyRule(), new FeatureExtractor(), rng);
        PrintStream sink = new PrintStream(new ByteArrayOutputStream());
        return new CodeReductionEngineFactory().create(model, synergy, sink).reduce(src, sink);
    }

    private static void render(List<BenchmarkResult> results)
    {
        System.out.println("+------------------- Hyper-Scalability Benchmark -------------------+");
        System.out.printf(" %-28s %9s %9s %10s %14s%n", "workload", "chars in", "chars out", "mean ms", "chars/sec");
        for (BenchmarkResult r : results)
        {
            System.out.printf(" %-28s %9d %9d %10.2f %14.0f%n", r.getWorkload(), r.getCharsIn(), r.getCharsOut(), r.getMeanMillis(), r.getCharsPerSecond());
        }
        System.out.println(" (each iteration retrains the neural network on the full corpus, as continuous refinement demands)");
        System.out.println("+-------------------------------------------------------------------+");
    }

    private static void writeCsv(List<BenchmarkResult> results, File file) throws IOException
    {
        File parent = file.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) throw new IOException("cannot create " + parent);
        try (PrintWriter writer = new PrintWriter(file, "UTF-8"))
        {
            writer.println("workload,chars_in,chars_out,iterations,mean_ms,chars_per_sec");
            for (BenchmarkResult r : results)
            {
                writer.println(String.format(Locale.ROOT, "%s,%d,%d,%d,%.3f,%.0f", r.getWorkload(), r.getCharsIn(), r.getCharsOut(), r.getIterations(), r.getMeanMillis(), r.getCharsPerSecond()));
            }
        }
    }

    private static List<Path> javaFiles(File root) throws IOException
    {
        try (Stream<Path> paths = Files.walk(root.toPath()))
        {
            return paths.filter(p -> p.toString().endsWith(".java")).sorted().collect(Collectors.toList());
        }
    }
}
