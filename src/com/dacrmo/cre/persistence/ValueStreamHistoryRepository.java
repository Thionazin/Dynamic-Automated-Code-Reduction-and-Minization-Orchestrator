package com.dacrmo.cre.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

public class ValueStreamHistoryRepository
{
    private final File file;

    public ValueStreamHistoryRepository(File file)
    {
        this.file = file;
    }

    // appends this run to the history and compares it with the previous run
    public String recordAndCompare(int linesIn, int charsIn, int charsOut, double reduction)
    {
        String result = "first recorded run";
        try
        {
            if (file.exists())
            {
                List<String> rows = Files.readAllLines(file.toPath());
                String[] last = rows.get(rows.size() - 1).split(",");
                double delta = Math.round((reduction - Double.parseDouble(last[4])) * 10) / 10.0 + 0.0;
                result = String.format("%+.1f points vs previous run (run #%d)", delta, rows.size() + 1);
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true)))
            {
                writer.println(System.currentTimeMillis() + "," + linesIn + "," + charsIn + "," + charsOut + "," + String.format(Locale.ROOT, "%.2f", reduction));
            }
        }
        catch (IOException | RuntimeException e)
        {
            result = "history unavailable";
        }
        return result;
    }
}
