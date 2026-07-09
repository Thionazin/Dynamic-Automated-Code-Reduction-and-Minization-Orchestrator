package com.dacrmo.cre.qa.framework;

import java.util.Objects;

public final class AssertionGovernanceService
{
    private AssertionGovernanceService()
    {
    }

    public static void assertTrue(boolean condition, String message)
    {
        if (!condition) throw new AssertionViolationException(message);
    }

    public static void assertFalse(boolean condition, String message)
    {
        assertTrue(!condition, message);
    }

    public static void assertEquals(Object expected, Object actual, String message)
    {
        if (!Objects.equals(expected, actual))
        {
            throw new AssertionViolationException(message + "\n      expected: " + expected + "\n      actual:   " + actual);
        }
    }

    public static void assertContains(String haystack, String needle, String message)
    {
        assertTrue(haystack.contains(needle), message + " (missing \"" + needle + "\")");
    }

    public static void assertNotContains(String haystack, String needle, String message)
    {
        assertFalse(haystack.contains(needle), message + " (found \"" + needle + "\")");
    }

    public static void assertSingleLine(String text, String message)
    {
        assertTrue(text.indexOf('\n') < 0 && text.indexOf('\r') < 0, message);
    }
}
