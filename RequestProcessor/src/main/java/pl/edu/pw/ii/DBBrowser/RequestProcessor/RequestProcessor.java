package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import org.apache.http.*;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.File.FileSystem;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequestFactory;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponseFactory;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class RequestProcessor {
    private ViewManager viewManager;
    private FileSystem fileSystem;

    public RequestProcessor(){
        viewManager = ViewManager.getInstance();
        fileSystem = new FileSystem();
    }

    public HttpResponse processRequest(org.apache.http.HttpRequest source){
        HttpResponse.Status initialResponseStatus = getInitialResponseStatus(source);
        if(initialResponseStatus != HttpResponse.Status.OK)
            return HttpResponseFactory.error(initialResponseStatus);
        try{
            HttpRequest request = HttpRequestFactory.createHttpRequest(source);
            if(!authorized(request))
                return HttpResponseFactory.authorizationRequest();
            if(viewManager.isView(request))
                return HttpResponseFactory.viewResponse(viewManager.getView(request));
            return HttpResponseFactory.fileResponse(fileSystem.getFile(request.getPath()));
        } catch (Throwable e){
            return HttpResponseFactory.internalServerError(e);
        }
    }

    private HttpResponse.Status getInitialResponseStatus(org.apache.http.HttpRequest source) {
        if(source.getRequestLine().getUri().length() > 2048)
            return HttpResponse.Status.URI_TOO_LONG;
        for(Header header : source.getAllHeaders())
            if(header.getValue().length() > 256)
                return HttpResponse.Status.REQUEST_HEADER_FIELDS_TOO_LARGE;
        if(!source.getRequestLine().getMethod().toLowerCase().equals("get"))
            return HttpResponse.Status.NOT_IMPLEMENTED;
        return HttpResponse.Status.OK;
    }

    private boolean authorized(HttpRequest request) {
        String auth = request.getHeader("Authorization");
        if(auth == null)
            return false;
        String credentialsString = new String(Base64.decodeBase64(auth.substring(6).getBytes()));
        String[] credentials = credentialsString.split(":");
        String userName = credentials[0];
        String password = credentials[1];
        //Temporary check:
        return userName.equals(password);
    }

}
