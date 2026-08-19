package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;

public class GraphAwareDependencyPruningTest
{
    private static String reduce(String src)
    {
        return ReductionFixtureFactory.reduce(src).getOutput();
    }

    @SynergyTest("imports with no incoming edges are pruned")
    public void unusedImportIsPruned()
    {
        assertNotContains(reduce("import java.util.HashMap; class A { }"), "HashMap", "an unused import survived");
    }

    @SynergyTest("imports that are referenced are kept")
    public void usedImportIsKept()
    {
        assertContains(reduce("import java.util.List; class A { List<String> xs; }"), "import java.util.List;", "a used import was pruned");
    }

    @SynergyTest("wildcard and static imports are never pruned")
    public void wildcardAndStaticImportsAreKept()
    {
        String output = reduce("import java.util.*; import static java.lang.Math.max; class A { }");
        assertContains(output, "import java.util.*;", "a wildcard import was pruned");
        assertContains(output, "import static java.lang.Math.max;", "a static import was pruned");
    }

    @SynergyTest("a name that only appears in a string still counts as used (the safe direction)")
    public void nameInStringKeepsImport()
    {
        assertContains(reduce("import java.util.Map; class A { String s = \"Map\"; }"), "import java.util.Map;", "pruning was not conservative");
    }
}
