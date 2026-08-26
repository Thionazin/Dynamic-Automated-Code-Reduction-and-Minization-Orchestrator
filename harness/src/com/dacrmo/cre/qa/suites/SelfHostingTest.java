package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.HarnessConfiguration;
import com.dacrmo.cre.qa.support.JavaCompilationService;
import com.dacrmo.cre.qa.support.ProcessExecutionService;
import com.dacrmo.cre.qa.support.ProcessResult;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;
import com.dacrmo.cre.qa.support.TemporaryWorkspace;
import java.nio.file.Path;

// the self-hosting ceremony: the platform reduces its own source, and the reduced platform still works identically
public class SelfHostingTest
{
    @SynergyTest("the one-lined platform compiles and produces byte-identical output")
    public void reducedPlatformIsEquivalent() throws Exception
    {
        FixtureRepository repository = new FixtureRepository();
        ProcessExecutionService processes = new ProcessExecutionService();
        try (TemporaryWorkspace workspace = new TemporaryWorkspace())
        {
            Path sourcesRoot = HarnessConfiguration.PRODUCTION_SOURCES.toPath();
            for (Path path : repository.productionSources())
            {
                String reduced = ReductionFixtureFactory.reduce(JavaLexicalUtils.read(path.toFile())).getOutput();
                workspace.write("mirror/" + sourcesRoot.relativize(path).toString().replace('\\', '/'), reduced);
            }
            Path mirrorClasses = new JavaCompilationService().compile(workspace.resolve("mirror"), null);

            Fixture fixture = repository.fixture("Hello");
            ProcessResult original = processes.java(workspace.getRoot().toFile(), fixture.getSource(), "-cp", HarnessConfiguration.MAIN_CLASSES.getPath(), "oneLiner", "-");
            ProcessResult mirrored = processes.java(workspace.getRoot().toFile(), fixture.getSource(), "-cp", mirrorClasses.toString(), "oneLiner", "-");
            assertEquals(0, mirrored.getExitCode(), "the one-lined platform crashed: " + mirrored.getStderr());
            assertEquals(original.getStdout(), mirrored.getStdout(), "the one-lined platform produced different output");
        }
    }
}
