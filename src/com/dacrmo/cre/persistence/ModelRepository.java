package com.dacrmo.cre.persistence;

import com.dacrmo.cre.ai.model.SpacePerceptron;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ModelRepository
{
    private final File file;

    public ModelRepository(File file)
    {
        this.file = file;
    }

    public SpacePerceptron load()
    {
        SpacePerceptron model = new SpacePerceptron();
        double[] weights = model.getWeights();
        try
        {
            String[] values = JavaLexicalUtils.read(file).trim().split("\\s+");
            if (values.length != weights.length) return model; // saved by an older architecture, start fresh
            for (int i = 0; i < weights.length; i++) weights[i] = Double.parseDouble(values[i]);
        }
        catch (IOException | NumberFormatException e)
        {
            // no saved model yet, start from zero
            return new SpacePerceptron();
        }
        return model;
    }

    public void save(SpacePerceptron model)
    {
        try (PrintWriter writer = new PrintWriter(file))
        {
            for (double w : model.getWeights()) writer.print(w + " ");
        }
        catch (IOException e)
        {
            // read-only filesystems (e.g. serverless) just don't keep learning
        }
    }
}
