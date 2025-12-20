package com.dacrmo.cre.lexical;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class JavaLexicalUtils
{
    public static final String OPERATORS = "+-*/<>=&|!%^~";

    private JavaLexicalUtils()
    {
    }

    public static String read(File file) throws IOException
    {
        return new String(Files.readAllBytes(file.toPath()), "UTF-8");
    }

    public static boolean isIdent(char c)
    {
        return Character.isJavaIdentifierPart(c);
    }

    public static boolean isOperator(char c)
    {
        return OPERATORS.indexOf(c) >= 0;
    }

    // positions of every space that sits outside a string or char literal
    public static List<Integer> spacesOutsideLiterals(String src)
    {
        List<Integer> spaces = new ArrayList<>();
        int i = 0;
        while (i < src.length())
        {
            char c = src.charAt(i);
            if (c == '"' || c == '\'')
            {
                i++;
                while (i < src.length() && src.charAt(i) != c)
                {
                    if (src.charAt(i) == '\\') i++;
                    i++;
                }
            }
            else if (c == ' ' && i > 0 && i + 1 < src.length())
            {
                spaces.add(i);
            }
            i++;
        }
        return spaces;
    }
}
