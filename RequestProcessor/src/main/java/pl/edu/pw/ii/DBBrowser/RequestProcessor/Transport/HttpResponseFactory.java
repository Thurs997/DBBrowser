package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

public class HttpResponseFactory {
    public static HttpResponse authorizationRequest() {
        HttpResponse response = new HttpResponse();
        response.addHeader("WWW-Authenticate", "BASIC realm=\"DBBrowser\"");
        response.setStatus(HttpResponse.Status.UNAUTHORIZED);
        return response;
    }
}
