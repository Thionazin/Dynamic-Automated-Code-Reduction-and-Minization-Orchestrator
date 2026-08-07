package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.JavaCompilationService;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;
import com.dacrmo.cre.qa.support.TemporaryWorkspace;

public class SemanticPreservationTest
{
    @SynergyTest("every fixture compiles and behaves identically after reduction")
    public void fixturesBehaveIdentically() throws Exception
    {
        JavaCompilationService compiler = new JavaCompilationService();
        for (Fixture fixture : new FixtureRepository().fixtures())
        {
            try (TemporaryWorkspace workspace = new TemporaryWorkspace())
            {
                String reduced = ReductionFixtureFactory.reduce(fixture.getSource()).getOutput();
                String expected = compiler.compileAndRun(workspace, "original", fixture.getClassName(), fixture.getSource());
                String actual = compiler.compileAndRun(workspace, "reduced", fixture.getClassName(), reduced);
                assertFalse(expected.isEmpty(), fixture.getClassName() + " printed nothing, so it proves nothing");
                assertEquals(expected, actual, fixture.getClassName() + " changed behaviour after reduction");
            }
        }
    }
}
