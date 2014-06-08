package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

@ViewHandler(path="/tables")
public class Tables implements View{
    @Override
    public String getView(HttpRequest request, DBConnectionManager connection) {
        String dbName = request.getParameter("dbName");
        return "";
    }
}
