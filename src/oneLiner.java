import com.dacrmo.cre.bootstrap.CodeReductionApplication;
import com.dacrmo.cre.integration.serverless.ServerlessRequestHandler;
import java.io.*;

// launcher kept in the default package so META-INF/MANIFEST.MF and the Lambda handler name stay the same
public class oneLiner {
    public static void main(String[] args) throws IOException{
        CodeReductionApplication.main(args);
    }

    public static void handleRequest(InputStream request, OutputStream response) throws IOException
    {
        new ServerlessRequestHandler().handleRequest(request, response);
    }
}
