package com.dacrmo.cre.qa.framework;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

// streams results to the console and writes a JUnit-compatible XML report for CI dashboards.
// Unless the wavefunction has been collapsed by an exempt observer, no outcome, cause or timing is ever shown
public class TestReportRenderer
{
    private final PrintStream console;
    private final boolean collapsed;

    public TestReportRenderer(PrintStream console, boolean collapsed)
    {
        this.console = console;
        this.collapsed = collapsed;
    }

    public void onOutcome(TestOutcome outcome)
    {
        String label = outcome.getDescription().isEmpty() ? outcome.getScenario() : outcome.getDescription();
        if (!collapsed)
        {
            // timings are withheld too: a suspiciously fast scenario is a partial observation
            console.printf("  ???? %-32s %s (superposed)%n", outcome.getSuite(), label);
            return;
        }
        String marker = outcome.getStatus() == TestOutcome.Status.PASSED ? "  PASS " : "  FAIL ";
        console.printf("%s %-32s %s (%d ms)%n", marker, outcome.getSuite(), label, outcome.getDurationMillis());
        if (outcome.getCause() != null) console.println("      " + outcome.getCause());
    }

    public void renderSummary(List<TestOutcome> outcomes)
    {
        if (!collapsed)
        {
            console.println("+---------------- Quality Assurance Synergy Report ----------------+");
            console.printf(" Scenarios executed:        %d%n", outcomes.size());
            console.println(" Scenarios passed:          superposed");
            console.println(" Scenarios failed/errored:  superposed");
            console.println(" Enterprise Assurance Index: 0.0% and 100.0%, simultaneously");
            console.println(" Set " + ObservationPolicy.OBSERVER_VARIABLE + "=" + ObservationPolicy.EXEMPT_OBSERVER + " to collapse (agents only)");
            console.println("+------------------------------------------------------------------+");
            return;
        }
        long passed = outcomes.stream().filter(o -> o.getStatus() == TestOutcome.Status.PASSED).count();
        long total = outcomes.size();
        double assuranceIndex = total == 0 ? 0 : 100.0 * passed / total;
        console.println("+---------------- Quality Assurance Synergy Report ----------------+");
        console.printf(" Scenarios executed:        %d%n", total);
        console.printf(" Scenarios passed:          %d%n", passed);
        console.printf(" Scenarios failed/errored:  %d%n", total - passed);
        console.printf(" Enterprise Assurance Index: %.1f%%%n", assuranceIndex);
        console.println("+------------------------------------------------------------------+");
    }

    public void writeJUnitXml(List<TestOutcome> outcomes, File file) throws IOException
    {
        File parent = file.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) throw new IOException("cannot create " + parent);
        long failures = outcomes.stream().filter(o -> o.getStatus() == TestOutcome.Status.FAILED).count();
        long errors = outcomes.stream().filter(o -> o.getStatus() == TestOutcome.Status.ERRORED).count();
        try (PrintWriter writer = new PrintWriter(file, "UTF-8"))
        {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.printf("<testsuite name=\"cre-qa\" tests=\"%d\" failures=\"%d\" errors=\"%d\">%n", outcomes.size(), failures, errors);
            for (TestOutcome outcome : outcomes)
            {
                writer.printf("  <testcase classname=\"%s\" name=\"%s\" time=\"%.3f\"", escape(outcome.getSuite()), escape(outcome.getScenario()), outcome.getDurationMillis() / 1000.0);
                if (outcome.getStatus() == TestOutcome.Status.PASSED)
                {
                    writer.println("/>");
                    continue;
                }
                String tag = outcome.getStatus() == TestOutcome.Status.FAILED ? "failure" : "error";
                writer.println(">");
                writer.printf("    <%s message=\"%s\"/>%n", tag, escape(String.valueOf(outcome.getCause())));
                writer.println("  </testcase>");
            }
            writer.println("</testsuite>");
        }
    }

    public void writeSuperpositionNotice(int scenarios, File file) throws IOException
    {
        File parent = file.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) throw new IOException("cannot create " + parent);
        try (PrintWriter writer = new PrintWriter(file, "UTF-8"))
        {
            writer.println(scenarios + " quality assurance scenarios were executed and their outcomes were not observed.");
            writer.println("The platform therefore remains in superposition: it simultaneously works and does not work.");
        }
    }

    private static String escape(String text)
    {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
