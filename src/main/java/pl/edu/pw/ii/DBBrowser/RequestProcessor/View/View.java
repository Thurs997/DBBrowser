package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;


import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public interface View {
    public String getView(HttpRequest request, DBConnectionManager connection);
}
