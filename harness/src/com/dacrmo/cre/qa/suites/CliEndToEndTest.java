package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.HarnessConfiguration;
import com.dacrmo.cre.qa.support.ProcessExecutionService;
import com.dacrmo.cre.qa.support.ProcessResult;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;
import com.dacrmo.cre.qa.support.TemporaryWorkspace;
import java.io.File;
import java.nio.file.Files;

// drives the real oneLiner entry point in a scratch directory, exactly as a user would
public class CliEndToEndTest
{
    private final ProcessExecutionService processes = new ProcessExecutionService();

    private ProcessResult runCli(File workingDir, String stdin, String... args) throws Exception
    {
        String[] command = new String[args.length + 3];
        command[0] = "-cp";
        command[1] = HarnessConfiguration.MAIN_CLASSES.getPath();
        command[2] = "oneLiner";
        System.arraycopy(args, 0, command, 3, args.length);
        return processes.java(workingDir, stdin, command);
    }

    @SynergyTest("first run without changeThis.dat bootstraps the input and hint files")
    public void bootstrapCreatesInputFiles() throws Exception
    {
        try (TemporaryWorkspace workspace = new TemporaryWorkspace())
        {
            ProcessResult result = runCli(workspace.getRoot().toFile(), null);
            assertEquals(0, result.getExitCode(), "bootstrap run failed: " + result.getStderr());
            assertTrue(Files.exists(workspace.resolve("changeThis.dat")), "changeThis.dat was not created");
            assertTrue(Files.exists(workspace.resolve("Copy paste code to changeThis")), "the hint file was not created");
        }
    }

    @SynergyTest("file mode writes the one-liner, the dashboard and all runtime artifacts")
    public void fileModeProducesOutputAndArtifacts() throws Exception
    {
        Fixture fixture = new FixtureRepository().fixture("Hello");
        try (TemporaryWorkspace workspace = new TemporaryWorkspace())
        {
            workspace.write("changeThis.dat", fixture.getSource());
            ProcessResult result = runCli(workspace.getRoot().toFile(), null);
            assertEquals(0, result.getExitCode(), "file mode failed: " + result.getStderr());

            String output = JavaLexicalUtils.read(workspace.resolve("output.txt").toFile());
            assertSingleLine(output, "output.txt is not one line");
            assertEquals(ReductionFixtureFactory.reduce(fixture.getSource()).getOutput(), output, "the CLI disagrees with the in-memory engine");
            assertContains(result.getStdout(), "AI Observability Dashboard", "the dashboard was not rendered");

            String[] weights = JavaLexicalUtils.read(workspace.resolve("cre-model.dat").toFile()).trim().split("\\s+");
            assertEquals(9, weights.length, "cre-model.dat should hold the 9 perceptron weights");
            assertTrue(Files.exists(workspace.resolve("cre-history.csv")), "value-stream history was not recorded");
            assertTrue(Files.exists(workspace.resolve("cre-kpi-report.txt")), "the KPI report was not written");
        }
    }

    @SynergyTest("stdin mode streams the one-liner to stdout and the dashboard to stderr")
    public void stdinModeStreams() throws Exception
    {
        Fixture fixture = new FixtureRepository().fixture("OperatorGauntlet");
        try (TemporaryWorkspace workspace = new TemporaryWorkspace())
        {
            ProcessResult result = runCli(workspace.getRoot().toFile(), fixture.getSource(), "-");
            assertEquals(0, result.getExitCode(), "stdin mode failed: " + result.getStderr());
            assertEquals(ReductionFixtureFactory.reduce(fixture.getSource()).getOutput(), result.getStdout(), "stdout should be exactly the one-liner");
            assertContains(result.getStderr(), "AI Observability Dashboard", "the dashboard should go to stderr");
        }
    }
}
