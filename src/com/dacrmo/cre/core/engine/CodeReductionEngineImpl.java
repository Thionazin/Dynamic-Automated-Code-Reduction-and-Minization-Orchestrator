package com.dacrmo.cre.core.engine;

import com.dacrmo.cre.api.ReductionEngine;
import com.dacrmo.cre.api.Transformer;
import java.io.PrintStream;
import java.util.List;

// a multi-layered pipeline of transformers
public class CodeReductionEngineImpl implements ReductionEngine
{
    private final List<Transformer> stages;

    public CodeReductionEngineImpl(List<Transformer> stages)
    {
        this.stages = stages;
    }

    public String reduce(String src, PrintStream log)
    {
        String current = src;
        for (Transformer stage : stages)
        {
            // operational resilience: a failing stage is skipped instead of taking the pipeline down
            try
            {
                current = stage.transform(current);
            }
            catch (RuntimeException e)
            {
                log.println("[CRE] " + stage.getClass().getSimpleName() + " failed, keeping previous result: " + e);
            }
        }
        return current;
    }
}
