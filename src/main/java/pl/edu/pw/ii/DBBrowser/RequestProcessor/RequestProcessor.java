package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.log4j.Logger;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.File.FileSystem;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.*;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class RequestProcessor {
    private ViewManager viewManager;
    private FileSystem fileSystem;
    private DBConnectionManager DBConnection;
    private Logger logger = Logger.getLogger(RequestProcessor.class);

    public RequestProcessor(){
        viewManager = ViewManager.getInstance();
        fileSystem = new FileSystem();
        DBConnection = new DBConnectionManager();
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
                return HttpResponseFactory.viewResponse(viewManager.getView(request, DBConnection));
            return HttpResponseFactory.fileResponse(fileSystem.getFile(request.getPath()));
        } catch (Throwable e){
            logger.error("Application error", e);
            return HttpResponseFactory.error(HttpResponse.Status.INTERNAL_SERVER_ERROR);
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
        //return userName.equals(password);
        //Will be:
        return DBConnection.connect(userName, password);
    }

    public void closeDBConnection(){
        DBConnection.close();
    }

}
