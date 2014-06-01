package pl.edu.pw.ii.DBBrowser.RequestProcessor.Parser;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class RequestParser {
    private boolean inTerminateState;

    public HttpRequest parse(String requestText) {
        return null;
    }

    public boolean isInTerminateState() {
        return inTerminateState;
    }

    public HttpRequest getRequest() {
        return null;
    }
}
