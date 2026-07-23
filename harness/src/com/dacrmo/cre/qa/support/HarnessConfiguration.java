package com.dacrmo.cre.qa.support;

import java.io.File;

public final class HarnessConfiguration
{
    public static final File REPO_ROOT = new File(System.getProperty("cre.repo.root", ".")).getAbsoluteFile();
    public static final File PRODUCTION_SOURCES = new File(REPO_ROOT, "src");
    public static final File FIXTURES = new File(REPO_ROOT, "fixtures/java");
    public static final File MAIN_CLASSES = new File(System.getProperty("cre.main.classes", new File(REPO_ROOT, "out").getPath())).getAbsoluteFile();

    private HarnessConfiguration()
    {
    }
}
