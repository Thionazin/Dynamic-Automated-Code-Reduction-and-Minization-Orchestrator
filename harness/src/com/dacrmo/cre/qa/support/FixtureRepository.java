package com.dacrmo.cre.qa.support;

import com.dacrmo.cre.lexical.JavaLexicalUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FixtureRepository
{
    public List<Fixture> fixtures() throws IOException
    {
        List<Fixture> fixtures = new ArrayList<>();
        for (Path path : javaFiles(HarnessConfiguration.FIXTURES))
        {
            String className = path.getFileName().toString().replace(".java", "");
            fixtures.add(new Fixture(className, JavaLexicalUtils.read(path.toFile())));
        }
        return fixtures;
    }

    public Fixture fixture(String className) throws IOException
    {
        for (Fixture fixture : fixtures()) if (fixture.getClassName().equals(className)) return fixture;
        throw new IOException("no fixture named " + className);
    }

    public List<Path> productionSources() throws IOException
    {
        return javaFiles(HarnessConfiguration.PRODUCTION_SOURCES);
    }

    private static List<Path> javaFiles(File root) throws IOException
    {
        try (Stream<Path> paths = Files.walk(root.toPath()))
        {
            return paths.filter(p -> p.toString().endsWith(".java")).sorted().collect(Collectors.toList());
        }
    }
}
