package pl.edu.pw.ii.DBBrowser.View;

import org.apache.http.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
@ViewHandler(path ="/")
public class MainView implements View {

    @Override
    public String getView(HttpRequest request) {
        return "x";
    }
}
