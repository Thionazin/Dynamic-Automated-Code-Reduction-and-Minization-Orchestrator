package com.dacrmo.cre.qa.support;

import com.dacrmo.cre.qa.framework.AssertionViolationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

// compiles and runs throwaway Java programs, for proving behaviour is preserved
public class JavaCompilationService
{
    private final ProcessExecutionService processes = new ProcessExecutionService();

    // compiles every .java file under sourceRoot into a sibling "classes" directory and returns it
    public Path compile(Path sourceRoot, String classpath) throws IOException
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) throw new AssertionViolationException("no system Java compiler; run the harness on a JDK, not a JRE");

        Path classes = sourceRoot.resolveSibling(sourceRoot.getFileName() + "-classes");
        Files.createDirectories(classes);
        List<String> args = new ArrayList<>();
        args.add("-encoding");
        args.add("UTF-8");
        args.add("-nowarn");
        args.add("-d");
        args.add(classes.toString());
        if (classpath != null)
        {
            args.add("-cp");
            args.add(classpath);
        }
        try (Stream<Path> paths = Files.walk(sourceRoot))
        {
            args.addAll(paths.filter(p -> p.toString().endsWith(".java")).map(Path::toString).collect(Collectors.toList()));
        }

        ByteArrayOutputStream diagnostics = new ByteArrayOutputStream();
        int status = compiler.run(null, null, diagnostics, args.toArray(new String[0]));
        if (status != 0) throw new AssertionViolationException("compilation failed:\n" + new String(diagnostics.toByteArray(), "UTF-8"));
        return classes;
    }

    public String compileAndRun(TemporaryWorkspace workspace, String label, String className, String source) throws IOException, InterruptedException
    {
        Path sourceRoot = workspace.resolve(label);
        workspace.write(label + "/" + className + ".java", source);
        Path classes = compile(sourceRoot, null);
        ProcessResult result = processes.java(workspace.getRoot().toFile(), null, "-Dfile.encoding=UTF-8", "-cp", classes.toString(), className);
        if (result.getExitCode() != 0) throw new AssertionViolationException(label + " " + className + " exited " + result.getExitCode() + ": " + result.getStderr());
        return result.getStdout();
    }
}
