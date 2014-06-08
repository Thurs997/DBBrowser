package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

@ViewHandler(path ="/")
public class Databases implements View{

    @Override
    public String getView(HttpRequest request, DBConnectionManager connection) {
        //Ta metoda ma zwrócić listę baz danych (już gotowego htmla)
        return "";
    }
}
