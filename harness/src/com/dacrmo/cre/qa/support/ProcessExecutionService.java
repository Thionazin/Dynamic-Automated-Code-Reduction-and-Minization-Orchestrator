package com.dacrmo.cre.qa.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessExecutionService
{
    private static final long TIMEOUT_SECONDS = 60;

    public static String javaExecutable()
    {
        return new File(new File(System.getProperty("java.home"), "bin"), "java").getPath();
    }

    public ProcessResult java(File workingDir, String stdin, String... args) throws IOException, InterruptedException
    {
        List<String> command = new ArrayList<>();
        command.add(javaExecutable());
        command.addAll(Arrays.asList(args));
        return run(command, workingDir, stdin);
    }

    public ProcessResult run(List<String> command, File workingDir, String stdin) throws IOException, InterruptedException
    {
        File stderrFile = File.createTempFile("cre-qa-stderr", ".txt");
        try
        {
            Process process = new ProcessBuilder(command).directory(workingDir).redirectError(stderrFile).start();
            try (OutputStream in = process.getOutputStream())
            {
                if (stdin != null) in.write(stdin.getBytes("UTF-8"));
            }
            String stdout = readFully(process.getInputStream());
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS))
            {
                process.destroyForcibly();
                throw new IOException("process timed out: " + command);
            }
            String stderr = new String(Files.readAllBytes(stderrFile.toPath()), "UTF-8");
            return new ProcessResult(process.exitValue(), stdout, stderr);
        }
        finally
        {
            if (!stderrFile.delete()) stderrFile.deleteOnExit();
        }
    }

    private static String readFully(InputStream stream) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int n;
        while ((n = stream.read(chunk)) != -1) buffer.write(chunk, 0, n);
        return new String(buffer.toByteArray(), "UTF-8");
    }
}
