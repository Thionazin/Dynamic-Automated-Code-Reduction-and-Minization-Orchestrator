package com.dacrmo.cre.qa.framework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// finds every compiled *Test class under the suites package of the harness output directory
public class TestDiscoveryEngine
{
    public static final String SUITES_PACKAGE = "com.dacrmo.cre.qa.suites";

    public List<Class<?>> discover(File classesRoot) throws IOException, ClassNotFoundException
    {
        Path suitesDir = classesRoot.toPath().resolve(SUITES_PACKAGE.replace('.', File.separatorChar));
        List<Class<?>> suites = new ArrayList<>();
        if (!Files.isDirectory(suitesDir)) return suites;
        try (Stream<Path> paths = Files.list(suitesDir))
        {
            for (Path path : paths.map(Path::getFileName).map(Path::toString)
                    .filter(name -> name.endsWith("Test.class") && !name.contains("$"))
                    .sorted()
                    .map(suitesDir::resolve)
                    .collect(Collectors.toList()))
            {
                String simpleName = path.getFileName().toString().replace(".class", "");
                suites.add(Class.forName(SUITES_PACKAGE + "." + simpleName));
            }
        }
        return suites;
    }
}
