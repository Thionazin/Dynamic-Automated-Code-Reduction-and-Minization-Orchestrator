package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;

public class LiteralIntegrityTest
{
    @SynergyTest("comment markers inside string literals are not treated as comments")
    public void commentMarkersInStringsSurvive()
    {
        String output = ReductionFixtureFactory.reduce("class A { String a = \"x // y\"; String b = \"/* z */\"; }").getOutput();
        assertContains(output, "\"x // y\"", "a // inside a string was stripped");
        assertContains(output, "\"/* z */\"", "a /* */ inside a string was stripped");
    }

    @SynergyTest("whitespace inside string literals is preserved exactly")
    public void whitespaceInStringsSurvives()
    {
        String output = ReductionFixtureFactory.reduce("class A { String s = \"  a   b  \"; }").getOutput();
        assertContains(output, "\"  a   b  \"", "spaces inside a string were collapsed");
    }

    @SynergyTest("escaped quotes and tricky char literals do not end a literal early")
    public void escapesAndCharLiteralsSurvive()
    {
        String src = "class A { char q = '\"'; char s = '\\''; char b = '\\\\'; String e = \"say \\\"hi\\\" // still\"; }";
        String output = ReductionFixtureFactory.reduce(src).getOutput();
        assertContains(output, "'\"'", "a double-quote char literal was damaged");
        assertContains(output, "'\\''", "an escaped apostrophe was damaged");
        assertContains(output, "'\\\\'", "an escaped backslash was damaged");
        assertContains(output, "\"say \\\"hi\\\" // still\"", "a string with escaped quotes was damaged");
    }

    @SynergyTest("comments are removed entirely")
    public void commentsAreRemoved()
    {
        String output = ReductionFixtureFactory.reduce("// lead\nclass A { /* block */ int x; // trail\n /** doc */ }").getOutput();
        assertEquals("class A{int x;}", output, "comments were not fully removed");
    }
}
