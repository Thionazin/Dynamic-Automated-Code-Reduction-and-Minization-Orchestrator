package com.dacrmo.cre.qa.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class TemporaryWorkspace implements AutoCloseable
{
    private final Path root;

    public TemporaryWorkspace() throws IOException
    {
        root = Files.createTempDirectory("cre-qa-");
    }

    public Path getRoot()
    {
        return root;
    }

    public Path resolve(String relative)
    {
        return root.resolve(relative);
    }

    public Path write(String relative, String content) throws IOException
    {
        Path path = root.resolve(relative);
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes("UTF-8"));
        return path;
    }

    public void close() throws IOException
    {
        try (Stream<Path> paths = Files.walk(root))
        {
            for (Path path : (Iterable<Path>) paths.sorted(Comparator.reverseOrder())::iterator) Files.deleteIfExists(path);
        }
    }
}
