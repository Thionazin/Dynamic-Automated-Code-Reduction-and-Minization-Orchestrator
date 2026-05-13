package com.dacrmo.cre.observability;

import com.dacrmo.cre.persistence.ValueStreamHistoryRepository;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// AI Observability Dashboard™: four 90-degree quadrants of insight, 360 degrees in total
public class ObservabilityDashboard
{
    private static final String[] MATURITY_LEVELS = { "Initial", "Managed", "Defined", "Quantitatively Managed", "Optimizing" };

    private final ValueStreamHistoryRepository history;
    private final KpiReportWriter kpiReportWriter;

    public ObservabilityDashboard(ValueStreamHistoryRepository history, KpiReportWriter kpiReportWriter)
    {
        this.history = history;
        this.kpiReportWriter = kpiReportWriter;
    }

    public void render(RunMetrics metrics, PrintStream log)
    {
        int linesIn = metrics.getBefore().split("\r\n|\r|\n", -1).length;
        int charsIn = metrics.getBefore().length();
        int charsOut = metrics.getAfter().length();
        double reduction = charsIn == 0 ? 0 : 100.0 * (charsIn - charsOut) / charsIn;
        int tokensIn = (charsIn + 3) / 4;
        int tokensOut = (charsOut + 3) / 4;
        int level = (int) Math.min(4, Math.max(0, reduction / 100 * 8));
        int decisions = metrics.getAcceptedDecisions() + metrics.getOverruledDecisions();
        double autonomy = decisions == 0 ? 0 : 100.0 * metrics.getAcceptedDecisions() / decisions;

        List<String> report = new ArrayList<>();
        report.add("+------------------ AI Observability Dashboard ------------------+");
        report.add(" [  0- 90] Code-reduction efficacy:  " + linesIn + " lines -> 1 line, " + charsIn + " -> " + charsOut + " chars (" + String.format("%.1f", reduction) + "% reduction)");
        report.add(" [ 90-180] Developer productivity uplift:  " + Math.max(0, linesIn - 1) + " fewer lines to scroll");
        report.add(" [180-270] Sustainability:  " + (charsIn - charsOut) + " bytes saved, " + metrics.getCpuNanos() / 1000000 + " ms CPU");
        report.add("           Carbon-neutral compute pathway:  0 network calls; runs on local power only (carbon-neutral where your grid is)");
        report.add(" [270-360] Value-stream optimization trend:  " + history.recordAndCompare(linesIn, charsIn, charsOut, reduction));
        report.add(" AI readiness maturity:  ~" + tokensIn + " -> ~" + tokensOut + " LLM tokens, level " + (level + 1) + " (" + MATURITY_LEVELS[level] + ")");
        report.add(" Neural network decisions:  " + metrics.getAcceptedDecisions() + " accepted, " + metrics.getOverruledDecisions() + " overruled by safety rule (" + String.format("%.1f", autonomy) + "% autonomy)");
        report.add(" Neural network reward rate:  " + String.format("%.1f", metrics.getRewardRate() * 100) + "% over " + metrics.getEpisodes() + " episodes");
        report.add("+----------------------------------------------------------------+");

        for (String line : report) log.println(line);
        kpiReportWriter.write(report);
    }
}
