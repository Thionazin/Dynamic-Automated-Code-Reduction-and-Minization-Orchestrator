package com.dacrmo.cre.qa.framework;

import java.io.File;
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
        TestReportRenderer renderer = new TestReportRenderer(System.out);
        System.out.println("Enterprise Test Orchestration Framework: discovering suites in " + classesRoot);

        List<Class<?>> suites = new TestDiscoveryEngine().discover(classesRoot);
        List<TestOutcome> outcomes = new TestExecutionOrchestrator().execute(suites, renderer);
        renderer.renderSummary(outcomes);
        renderer.writeJUnitXml(outcomes, new File("harness/reports/TEST-cre-qa.xml"));

        boolean allPassed = !outcomes.isEmpty() && outcomes.stream().allMatch(o -> o.getStatus() == TestOutcome.Status.PASSED);
        System.exit(allPassed ? 0 : 1);
    }
}
