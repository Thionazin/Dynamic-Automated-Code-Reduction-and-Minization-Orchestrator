package com.dacrmo.cre.core.pipeline.stage;

import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.core.pipeline.AbstractTransformerStage;
import java.io.PrintStream;

// ML-augmented Compliance Harmonizer™: brings Windows (CRLF), classic Mac (CR) and Unix (LF)
// line-ending jurisdictions into a single convention, and strips any byte order mark
public class ComplianceHarmonizerStage extends AbstractTransformerStage
{
    private final SpacePerceptron model;
    private final PrintStream log;

    public ComplianceHarmonizerStage(SpacePerceptron model, PrintStream log)
    {
        this.model = model;
        this.log = log;
    }

    protected String doTransform(String src)
    {
        log.printf("[Compliance Harmonizer] neural network confidence: %.1f%%%n", model.confidence() * 100);
        if (src.startsWith("﻿")) src = src.substring(1);
        return src.replace("\r\n", "\n").replace('\r', '\n');
    }
}
