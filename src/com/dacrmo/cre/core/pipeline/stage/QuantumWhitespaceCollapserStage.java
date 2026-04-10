package com.dacrmo.cre.core.pipeline.stage;

import com.dacrmo.cre.core.pipeline.AbstractTransformerStage;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.synergy.SynergyEnabler;
import java.util.List;

// removes every space the arbitrated decisions say is unnecessary, as chosen by the quantum-inspired optimizer
public class QuantumWhitespaceCollapserStage extends AbstractTransformerStage
{
    private final SynergyEnabler synergy;

    public QuantumWhitespaceCollapserStage(SynergyEnabler synergy)
    {
        this.synergy = synergy;
    }

    protected String doTransform(String src)
    {
        List<Integer> spaces = JavaLexicalUtils.spacesOutsideLiterals(src);
        boolean[] keep = synergy.optimize(src, spaces);
        StringBuilder result = new StringBuilder(src);
        for (int i = spaces.size() - 1; i >= 0; i--)
        {
            if (!keep[i]) result.deleteCharAt(spaces.get(i));
        }
        return result.toString();
    }
}
