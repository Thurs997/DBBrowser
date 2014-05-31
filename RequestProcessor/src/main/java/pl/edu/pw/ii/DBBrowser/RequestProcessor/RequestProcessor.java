package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.File.FileSystem;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Parser.RequestParser;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;

/**
 * Created by kokoss on 5/31/14.
 */
public class RequestProcessor {
    private RequestParser parser;
    private ViewManager viewManager;
    private FileSystem fileSystem;

    public RequestProcessor(){
        parser = new RequestParser();
        viewManager = new ViewManager();
        fileSystem = new FileSystem();
    }

    public void processRequest(String requestText){
        parser.parse(requestText);
    }

    public HttpResponse getResponse(){
        if(parser.isInTerminateState())
            return processRequest();
        else
            return HttpResponse.incomplete();
    }

    private HttpResponse processRequest() {
        HttpRequest request = parser.getRequest();
        if(viewManager.isView(request))
            return viewManager.getView(request);
        return fileSystem.getFile(request.getPath());
    }

}
