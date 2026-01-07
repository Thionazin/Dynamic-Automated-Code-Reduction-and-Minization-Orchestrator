package com.dacrmo.cre.lexical;

public enum CharacterClass
{
    IDENT,
    OPERATOR,
    OTHER;

    public static CharacterClass of(char c)
    {
        if (JavaLexicalUtils.isIdent(c)) return IDENT;
        if (JavaLexicalUtils.isOperator(c)) return OPERATOR;
        return OTHER;
    }
}
