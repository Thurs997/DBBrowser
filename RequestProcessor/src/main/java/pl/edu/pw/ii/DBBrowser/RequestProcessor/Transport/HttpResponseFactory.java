package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

public abstract class HttpResponseFactory {
    public static final String CONTENT_TYPE_HTML = "text/html; charset=utf-8";

    public static HttpResponse authorizationRequest() {
        HttpResponse response = new HttpResponse();
        response.addHeader("WWW-Authenticate", "BASIC realm=\"DBBrowser\"");
        response.setStatus(HttpResponse.Status.UNAUTHORIZED);
        return response;
    }

    public static HttpResponse internalServerError(Throwable e) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpResponse.Status.INTERNAL_SERVER_ERROR);
        response.setMimeType(CONTENT_TYPE_HTML);
        response.setContent(("<b>Application error!</b><br />Please, come back later<br />Message: " + e.getMessage()).getBytes());
        return response;
    }

    public static HttpResponse viewResponse(String content){
        HttpResponse response = new HttpResponse();
        response.setContent(content.getBytes());
        response.setMimeType(CONTENT_TYPE_HTML);
        return response;
    }

    public static HttpResponse fileResponse(FileResponse file){
        if(file == null)
            return error(HttpResponse.Status.NOT_FOUND);
        HttpResponse response = new HttpResponse();
        response.setContent(file.getContent());
        response.setMimeType(file.getContentType());
        return response;
    }

    public static HttpResponse error(HttpResponse.Status status) {
        HttpResponse response = new HttpResponse();
        response.setStatus(status);
        response.setMimeType(CONTENT_TYPE_HTML);
        response.setContent(("<b>Request invalid!</b><br />Error: " + status.getName()).getBytes());
        return response;
    }
}
