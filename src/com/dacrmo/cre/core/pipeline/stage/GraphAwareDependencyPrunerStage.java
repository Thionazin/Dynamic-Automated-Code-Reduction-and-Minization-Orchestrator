package com.dacrmo.cre.core.pipeline.stage;

import com.dacrmo.cre.core.pipeline.AbstractTransformerStage;
import java.util.*;
import java.util.regex.*;

// builds a graph from each single-type import to the places its simple name is used,
// then prunes imports with no incoming edges. Wildcard and static imports are left alone.
public class GraphAwareDependencyPrunerStage extends AbstractTransformerStage
{
    private static final Pattern IMPORT = Pattern.compile("(?<![\\w$.])import\\s+(?!static\\s)[\\w$.]*?([\\w$]+)\\s*;\\s?");

    protected String doTransform(String src)
    {
        Map<String, List<String>> importsByName = new LinkedHashMap<>();
        Matcher m = IMPORT.matcher(src);
        while (m.find()) importsByName.computeIfAbsent(m.group(1), k -> new ArrayList<>()).add(m.group());

        String body = IMPORT.matcher(src).replaceAll("");
        String result = src;
        for (Map.Entry<String, List<String>> node : importsByName.entrySet())
        {
            Matcher edges = Pattern.compile("(?<![\\w$])" + Pattern.quote(node.getKey()) + "(?![\\w$])").matcher(body);
            int inDegree = 0;
            while (edges.find()) inDegree++;
            if (inDegree == 0)
            {
                for (String statement : node.getValue()) result = result.replace(statement, "");
            }
        }
        return result;
    }
}
