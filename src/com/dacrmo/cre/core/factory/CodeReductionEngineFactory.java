package com.dacrmo.cre.core.factory;

import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.api.ReductionEngine;
import com.dacrmo.cre.api.Transformer;
import com.dacrmo.cre.core.engine.CodeReductionEngineImpl;
import com.dacrmo.cre.core.pipeline.stage.ComplianceHarmonizerStage;
import com.dacrmo.cre.core.pipeline.stage.GraphAwareDependencyPrunerStage;
import com.dacrmo.cre.core.pipeline.stage.QuantumWhitespaceCollapserStage;
import com.dacrmo.cre.core.pipeline.stage.SemanticCompressorStage;
import com.dacrmo.cre.synergy.SynergyEnabler;
import java.io.PrintStream;
import java.util.Arrays;

public class CodeReductionEngineFactory
{
    public ReductionEngine create(SpacePerceptron model, SynergyEnabler synergy, PrintStream log)
    {
        return new CodeReductionEngineImpl(Arrays.<Transformer>asList(
                new ComplianceHarmonizerStage(model, log),
                new SemanticCompressorStage(),
                new GraphAwareDependencyPrunerStage(),
                new QuantumWhitespaceCollapserStage(synergy)));
    }
}
