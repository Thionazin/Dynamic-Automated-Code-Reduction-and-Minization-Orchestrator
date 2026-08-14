package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;

public class WhitespaceSafetyRuleTest
{
    private final WhitespaceSafetyRule rule = new WhitespaceSafetyRule();

    @SynergyTest("spaces between two identifier characters are required")
    public void identifierPairsNeedSpace()
    {
        assertTrue(rule.needsSpace('t', 'x'), "int x would become intx");
        assertTrue(rule.needsSpace('1', 'L'), "identifier parts include digits");
        assertTrue(rule.needsSpace('_', '$'), "identifier parts include _ and $");
    }

    @SynergyTest("spaces between two operator characters are required")
    public void operatorPairsNeedSpace()
    {
        assertTrue(rule.needsSpace('-', '-'), "a - -b would become a--b");
        assertTrue(rule.needsSpace('+', '+'), "x + +y would become x++y");
        assertTrue(rule.needsSpace('/', '/'), "/ / would become a comment");
        assertTrue(rule.needsSpace('/', '*'), "/ * would become a comment");
        assertTrue(rule.needsSpace('>', '>'), "> > would become a shift");
    }

    @SynergyTest("every other pairing is safe to collapse")
    public void mixedPairsDoNotNeedSpace()
    {
        assertFalse(rule.needsSpace('a', '='), "identifier then operator is safe");
        assertFalse(rule.needsSpace('=', 'b'), "operator then identifier is safe");
        assertFalse(rule.needsSpace(')', '{'), "punctuation is safe");
        assertFalse(rule.needsSpace('n', '"'), "return \"x\" is valid as return\"x\"");
    }

    @SynergyTest("protected operator spacing survives a full reduction")
    public void operatorSpacingSurvivesReduction()
    {
        String output = ReductionFixtureFactory.reduce("class A { int f(int a, int b) { return a - -b + a + +b + a / -b - --a; } }").getOutput();
        assertContains(output, "a- -b", "a - -b lost its space");
        assertContains(output, "a+ +b", "a + +b lost its space");
        assertContains(output, "a/ -b", "a / -b lost its space");
        assertContains(output, "- --a", "- --a lost its space");
        assertContains(output, "int f(int a,int b)", "unneeded spaces were not removed");
    }
}
