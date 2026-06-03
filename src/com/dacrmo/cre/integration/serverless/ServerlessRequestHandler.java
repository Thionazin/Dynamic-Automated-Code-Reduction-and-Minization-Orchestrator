package com.dacrmo.cre.integration.serverless;

import com.dacrmo.cre.core.engine.ReductionOrchestratorImpl;
import java.io.*;
import static java.lang.System.*;

// matches the AWS Lambda stream handler shape, so serverless platforms can call it with no SDK.
// The dashboard goes to stderr, which is where those platforms collect logs.
public class ServerlessRequestHandler
{
    public void handleRequest(InputStream request, OutputStream response) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int n;
        while ((n = request.read(chunk)) != -1) buffer.write(chunk, 0, n);
        String result = new ReductionOrchestratorImpl().run(new String(buffer.toByteArray(), "UTF-8"), err);
        response.write(result.getBytes("UTF-8"));
        response.flush();
    }
}
