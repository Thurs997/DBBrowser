package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;


import org.apache.http.HttpRequest;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public interface View {
    public String getView(HttpRequest request);
}
