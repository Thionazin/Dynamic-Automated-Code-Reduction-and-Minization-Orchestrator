package com.dacrmo.cre.ai.rules;

import com.dacrmo.cre.lexical.JavaLexicalUtils;

// the deterministic safety rule: a space only matters if removing it would merge two tokens
public class WhitespaceSafetyRule
{
    public boolean needsSpace(char prev, char next)
    {
        return (JavaLexicalUtils.isIdent(prev) && JavaLexicalUtils.isIdent(next))
                || (JavaLexicalUtils.isOperator(prev) && JavaLexicalUtils.isOperator(next));
    }
}
