package com.dacrmo.cre.api;

import java.io.PrintStream;

public interface ReductionOrchestrator
{
    String run(String src, PrintStream log);
}
