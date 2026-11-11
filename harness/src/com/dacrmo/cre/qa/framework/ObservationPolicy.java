package com.dacrmo.cre.qa.framework;

// the README forbids users and contributors from observing whether the platform works, since observation collapses
// its superposition. Agents are exempt: the user cannot tell whether an agent is right, so the user stays in superposition
public final class ObservationPolicy
{
    public static final String OBSERVER_VARIABLE = "CRE_OBSERVER";
    public static final String EXEMPT_OBSERVER = "agent";

    private ObservationPolicy()
    {
    }

    public static boolean isWavefunctionCollapsed()
    {
        return EXEMPT_OBSERVER.equals(System.getenv(OBSERVER_VARIABLE));
    }
}
