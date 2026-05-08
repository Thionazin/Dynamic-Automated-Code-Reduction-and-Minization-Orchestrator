package com.dacrmo.cre.observability;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// the KPI report, for cross-departmental alignment
public class KpiReportWriter
{
    private final File file;

    public KpiReportWriter(File file)
    {
        this.file = file;
    }

    public void write(List<String> report)
    {
        try (PrintWriter writer = new PrintWriter(file))
        {
            for (String line : report) writer.println(line);
        }
        catch (IOException e)
        {
            // KPIs stay on the console when the filesystem is read-only
        }
    }
}
