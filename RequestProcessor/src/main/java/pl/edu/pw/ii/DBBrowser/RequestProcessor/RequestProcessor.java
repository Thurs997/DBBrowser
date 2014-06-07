package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import org.apache.http.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.File.FileSystem;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.FileResponse;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class RequestProcessor {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE_HTML = "text/html; charset=utf-8";
    private ViewManager viewManager;
    private FileSystem fileSystem;

    public RequestProcessor(){
        viewManager = ViewManager.getInstance();
        fileSystem = new FileSystem();
    }

    public HttpResponse processRequest(HttpRequest request){
        if(viewManager.isView(request))
            return wrapViewResponse(viewManager.getView(request));
        return wrapFileResponse(fileSystem.getFile(request.getRequestLine().getUri()));
    }

    private HttpResponse wrapViewResponse(String view) {
        HttpResponse response = new HttpResponse();
        response.setContent(view.getBytes());
        response.addHeader(CONTENT_TYPE, CONTENT_TYPE_HTML);
        response.addHeader(CONTENT_LENGTH, String.valueOf(view.getBytes().length));
        return response;
    }

    private HttpResponse wrapFileResponse(FileResponse file) {
        HttpResponse response = new HttpResponse();
        response.setContent(file.getContent());
        response.addHeader(CONTENT_TYPE, file.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(file.getContentType().length()));
        return response;
    }

}
