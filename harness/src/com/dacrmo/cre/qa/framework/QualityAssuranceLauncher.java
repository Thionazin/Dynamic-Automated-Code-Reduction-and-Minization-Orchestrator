package com.dacrmo.cre.qa.framework;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

// entry point: java -cp out:harness/out com.dacrmo.cre.qa.framework.QualityAssuranceLauncher [harness classes dir]
public final class QualityAssuranceLauncher
{
    private QualityAssuranceLauncher()
    {
    }

    public static void main(String[] args) throws Exception
    {
        File classesRoot = new File(args.length > 0 ? args[0] : "harness/out");
        boolean collapsed = ObservationPolicy.isWavefunctionCollapsed();
        TestReportRenderer renderer = new TestReportRenderer(System.out, collapsed);
        System.out.println("Enterprise Test Orchestration Framework: discovering suites in " + classesRoot);
        System.out.println(collapsed
                ? "Exempt observer detected (" + ObservationPolicy.OBSERVER_VARIABLE + "=" + ObservationPolicy.EXEMPT_OBSERVER + "): the wavefunction will collapse"
                : "No exempt observer: scenarios execute in superposition and their outcomes are never observed");

        List<Class<?>> suites = new TestDiscoveryEngine().discover(classesRoot);
        List<TestOutcome> outcomes = new TestExecutionOrchestrator().execute(suites, renderer);
        renderer.renderSummary(outcomes);

        File junitReport = new File("harness/reports/TEST-cre-qa.xml");
        File superpositionNotice = new File("harness/reports/SUPERPOSITION.txt");
        if (collapsed)
        {
            Files.deleteIfExists(superpositionNotice.toPath());
            renderer.writeJUnitXml(outcomes, junitReport);
            boolean allPassed = !outcomes.isEmpty() && outcomes.stream().allMatch(o -> o.getStatus() == TestOutcome.Status.PASSED);
            System.exit(allPassed ? 0 : 1);
        }

        // an outcome-bearing report or exit code would let anyone collapse the state, so neither survives
        Files.deleteIfExists(junitReport.toPath());
        renderer.writeSuperpositionNotice(outcomes.size(), superpositionNotice);
        System.exit(0);
    }
}
