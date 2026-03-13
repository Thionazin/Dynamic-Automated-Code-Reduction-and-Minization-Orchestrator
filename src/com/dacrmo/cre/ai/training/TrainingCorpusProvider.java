package com.dacrmo.cre.ai.training;

import com.dacrmo.cre.core.pipeline.stage.SemanticCompressorStage;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// domain-specific training data: every .java file in this codebase, compressed onto one line
public class TrainingCorpusProvider
{
    private final File root;

    public TrainingCorpusProvider(File root)
    {
        this.root = root;
    }

    public List<String> corpus()
    {
        List<String> documents = new ArrayList<>();
        if (!root.isDirectory()) return documents;
        SemanticCompressorStage compressor = new SemanticCompressorStage();
        try (Stream<Path> paths = Files.walk(root.toPath()))
        {
            for (Path path : paths.filter(p -> p.toString().endsWith(".java")).sorted().collect(Collectors.toList()))
            {
                documents.add(compressor.transform(JavaLexicalUtils.read(path.toFile())));
            }
        }
        catch (IOException e)
        {
            // own source not shipped alongside the jar, train on the input alone
        }
        return documents;
    }
}
