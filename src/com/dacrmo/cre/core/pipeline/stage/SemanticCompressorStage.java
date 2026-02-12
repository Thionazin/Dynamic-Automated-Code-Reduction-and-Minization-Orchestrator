package com.dacrmo.cre.core.pipeline.stage;

import com.dacrmo.cre.core.pipeline.AbstractTransformerStage;

// removes comments and collapses all whitespace, putting the whole program on one line
public class SemanticCompressorStage extends AbstractTransformerStage
{
    protected String doTransform(String src)
    {
        StringBuilder result = new StringBuilder();
        boolean pendingSpace = false;
        int i = 0;
        while (i < src.length())
        {
            char c = src.charAt(i);
            char next = i + 1 < src.length() ? src.charAt(i + 1) : '\0';

            // comments become whitespace, otherwise a // would eat the rest of the program
            if (c == '/' && next == '/')
            {
                while (i < src.length() && src.charAt(i) != '\n') i++;
                pendingSpace = true;
            }
            else if (c == '/' && next == '*')
            {
                int end = src.indexOf("*/", i + 2);
                i = end == -1 ? src.length() : end + 2;
                pendingSpace = true;
            }
            else if (Character.isWhitespace(c))
            {
                pendingSpace = true;
                i++;
            }
            else
            {
                if (pendingSpace && result.length() > 0) result.append(' ');
                pendingSpace = false;
                if (c == '"' || c == '\'')
                {
                    // copy string and char literals untouched, including escapes
                    result.append(c);
                    i++;
                    while (i < src.length() && src.charAt(i) != c)
                    {
                        if (src.charAt(i) == '\\' && i + 1 < src.length()) result.append(src.charAt(i++));
                        result.append(src.charAt(i++));
                    }
                    if (i < src.length()) result.append(src.charAt(i++));
                }
                else
                {
                    result.append(c);
                    i++;
                }
            }
        }
        return result.toString();
    }
}
