package pl.edu.pw.ii.DBBrowser.RequestProcessor.Parser;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class RequestParser {
    private Lexer lexer;
    private boolean inTerminalState;
    public HttpRequest parse(String requestText) {
        return null;
    }

    public boolean isInTerminalState() {
        return inTerminalState;
    }

    public HttpRequest getRequest() {
        return null;
    }
}
