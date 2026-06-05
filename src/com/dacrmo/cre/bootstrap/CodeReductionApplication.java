package com.dacrmo.cre.bootstrap;

import com.dacrmo.cre.config.CreConfiguration;
import com.dacrmo.cre.core.engine.ReductionOrchestratorImpl;
import com.dacrmo.cre.integration.serverless.ServerlessRequestHandler;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import java.io.*;
import static java.lang.System.*;

/*
 * Dynamic Automated Code Reduction and Minimization Orchestrator
 *
 * Cloud-native: this code was generated in the cloud by Claude, a large language model pretrained
 * with self-supervised learning on vast corpora that include a great deal of Java, and it is
 * continuously refined through Claude Code.
 *
 * Democratic: there is no authentication or role system, so everyone from junior developers to
 * senior architects is equally empowered to run it.
 */
public final class CodeReductionApplication
{
    private CodeReductionApplication()
    {
    }

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 && args[0].equals("-"))
        {
            new ServerlessRequestHandler().handleRequest(in, out);
            return;
        }

        if (!CreConfiguration.INPUT_FILE.exists())
        {
            new PrintWriter(CreConfiguration.INPUT_FILE).close();
            new PrintWriter(CreConfiguration.INPUT_HINT_FILE).close();
            return;
        }

        String src = JavaLexicalUtils.read(CreConfiguration.INPUT_FILE);
        String result = new ReductionOrchestratorImpl().run(src, out);
        PrintWriter writer = new PrintWriter(CreConfiguration.OUTPUT_FILE, "UTF-8");
        writer.print(result);
        writer.close();
    }
}
