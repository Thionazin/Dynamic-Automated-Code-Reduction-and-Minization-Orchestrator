package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.ai.ensemble.SuperpositionEnsemble;
import com.dacrmo.cre.ai.hypergaussian.HypergaussianActivationFunction;
import com.dacrmo.cre.ai.hypergaussian.QuantumInspiredHypergaussianNetwork;
import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.model.SpaceDecisionModel;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.HarnessConfiguration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuantumInspiredHypergaussianNetworkTest
{
    private final FeatureExtractor extractor = new FeatureExtractor();
    private final WhitespaceSafetyRule rule = new WhitespaceSafetyRule();

    @SynergyTest("the activation is genuinely hyper-Gaussian: flatter-topped than a Gaussian, with steeper shoulders")
    public void activationIsHypergaussian()
    {
        HypergaussianActivationFunction hyper = new HypergaussianActivationFunction(1, 2);
        HypergaussianActivationFunction gauss = new HypergaussianActivationFunction(1, 1);
        double[] center = { 0 };
        assertEquals(1.0, hyper.activate(center, center), "a unit fires fully at its own center");
        assertTrue(hyper.activate(new double[] { 0.8 }, center) > gauss.activate(new double[] { 0.8 }, center), "near the center, the top should be flatter");
        assertTrue(hyper.activate(new double[] { 2.5 }, center) < gauss.activate(new double[] { 2.5 }, center), "far from the center, the fall-off should be steeper");
    }

    @SynergyTest("an untrained network sits in uniform superposition")
    public void untrainedNetworkIsUndecided()
    {
        QuantumInspiredHypergaussianNetwork network = new QuantumInspiredHypergaussianNetwork();
        assertTrue(Math.abs(network.predict(extractor.extract('a', 'b')) - 0.5) < 1e-9, "sin²(pi/4) should be one half");
        assertTrue(network.certainty() < 1e-9, "no Q-bit should have collapsed yet");
    }

    @SynergyTest("rotation-gate training on the platform's own source reaches 100% agreement with the safety rule")
    public void learnsTheSafetyRule() throws Exception
    {
        QuantumInspiredHypergaussianNetwork network = new QuantumInspiredHypergaussianNetwork();
        for (String document : productionSources()) network.train(document, rule, extractor);
        assertTrue(network.getRotations() > 0, "the network was never trained");
        for (Fixture fixture : new FixtureRepository().fixtures())
        {
            String src = fixture.getSource();
            for (int index : JavaLexicalUtils.spacesOutsideLiterals(src))
            {
                char prev = src.charAt(index - 1);
                char next = src.charAt(index + 1);
                assertEquals(rule.needsSpace(prev, next), network.decide(extractor.extract(prev, next)),
                        fixture.getClassName() + ": disagreed with the safety rule on '" + prev + " " + next + "'");
            }
        }
    }

    @SynergyTest("the superposition ensemble averages amplitudes, not probabilities")
    public void ensembleInterferesAmplitudes()
    {
        SpaceDecisionModel certain = x -> 1.0;
        SpaceDecisionModel never = x -> 0.0;
        SpaceDecisionModel ensemble = new SuperpositionEnsemble(certain, never);
        assertTrue(Math.abs(ensemble.predict(new double[0]) - 0.25) < 1e-12, "((1 + 0) / 2)² should be 0.25, not the classical 0.5");
        assertFalse(ensemble.decide(new double[0]), "destructive interference should drop the space");
    }

    private static List<String> productionSources() throws Exception
    {
        try (Stream<Path> paths = Files.walk(HarnessConfiguration.PRODUCTION_SOURCES.toPath()))
        {
            List<String> documents = new ArrayList<>();
            for (Path file : paths.filter(p -> p.toString().endsWith(".java")).sorted().collect(Collectors.toList()))
            {
                documents.add(JavaLexicalUtils.read(file.toFile()));
            }
            return documents;
        }
    }
}
