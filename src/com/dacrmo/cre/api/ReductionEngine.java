package com.dacrmo.cre.api;

import java.io.PrintStream;

// the Code Reduction Engine™ (CRE)
public interface ReductionEngine
{
    String reduce(String src, PrintStream log);
}
