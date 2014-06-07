package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.File.FileSystem;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.*;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;
import org.apache.commons.codec.binary.Base64;

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
        if(!authorized(request))
            return HttpResponseFactory.authorizationRequest();
        if(viewManager.isView(request))
            return wrapViewResponse(viewManager.getView(request));
        return wrapFileResponse(fileSystem.getFile(request.getRequestLine().getUri()));
    }

    private boolean authorized(HttpRequest request) {
        Header auth = request.getFirstHeader("Authorization");
        if(auth == null)
            return false;
        String credentialsString = new String(Base64.decodeBase64(auth.getValue().substring(6).getBytes()));
        String[] credentials = credentialsString.split(":");
        String userName = credentials[0];
        String password = credentials[1];
        //Temporary check:
        return userName.equals(password);
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
        response.addHeader(CONTENT_LENGTH, String.valueOf(file.getContentType().length()));
        response.setMimeType(file.getContentType());
        return response;
    }

}
