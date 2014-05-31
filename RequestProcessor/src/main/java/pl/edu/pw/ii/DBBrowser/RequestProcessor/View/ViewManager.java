package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

/**
 * Created by kokoss on 5/31/14.
 */
public class ViewManager {
    public boolean isView(HttpRequest request) {
        return false;
    }

    public HttpResponse getView(HttpRequest request) {
        return null;
    }
}
