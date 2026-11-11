package com.dacrmo.cre.core.engine;

import com.dacrmo.cre.ai.arbitration.DecisionArbitrationService;
import com.dacrmo.cre.ai.hypergaussian.QuantumInspiredHypergaussianNetwork;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.ai.training.ReinforcementLearningTrainer;
import com.dacrmo.cre.ai.training.TrainingCorpusProvider;
import com.dacrmo.cre.api.ReductionEngine;
import com.dacrmo.cre.api.ReductionOrchestrator;
import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.core.factory.CodeReductionEngineFactory;
import com.dacrmo.cre.observability.KpiReportWriter;
import com.dacrmo.cre.observability.ObservabilityDashboard;
import com.dacrmo.cre.observability.RunMetrics;
import com.dacrmo.cre.persistence.ModelRepository;
import com.dacrmo.cre.persistence.ValueStreamHistoryRepository;
import com.dacrmo.cre.quantum.QuantumInspiredOptimizer;
import com.dacrmo.cre.synergy.SynergyEnabler;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class ReductionOrchestratorImpl implements ReductionOrchestrator
{
    public String run(String src, PrintStream log)
    {
        long cpuStart = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        ModelRepository modelRepository = new ModelRepository(CreConfiguration.MODEL_FILE);
        SpacePerceptron model = modelRepository.load();
        Random rng = new Random(CreConfiguration.RANDOM_SEED);
        ReinforcementLearningTrainer trainer = new ReinforcementLearningTrainer(model, rng);
        QuantumInspiredHypergaussianNetwork hypergaussianNetwork = new QuantumInspiredHypergaussianNetwork();
        SynergyEnabler synergy = new SynergyEnabler(
                model,
                hypergaussianNetwork,
                trainer,
                new DecisionArbitrationService(),
                new QuantumInspiredOptimizer(),
                new TrainingCorpusProvider(CreConfiguration.TRAINING_CORPUS_ROOT),
                new WhitespaceSafetyRule(),
                new FeatureExtractor(),
                rng);

        ReductionEngine engine = new CodeReductionEngineFactory().create(model, synergy, log);
        String result = engine.reduce(src, log);

        modelRepository.save(model);
        long cpuNanos = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - cpuStart;
        RunMetrics metrics = new RunMetrics(src, result, cpuNanos, trainer.getRewardRate(), trainer.getEpisodes(),
                synergy.getAcceptedDecisions(), synergy.getOverruledDecisions(),
                hypergaussianNetwork.getUnitCount(), hypergaussianNetwork.certainty());
        new ObservabilityDashboard(
                new ValueStreamHistoryRepository(CreConfiguration.HISTORY_FILE),
                new KpiReportWriter(CreConfiguration.KPI_REPORT_FILE)).render(metrics, log);
        return result;
    }
}
