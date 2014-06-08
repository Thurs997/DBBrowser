package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

import java.sql.SQLException;
import java.util.List;

@ViewHandler(path="/tables")
public class Tables implements View{

    ViewUtils viewUtils = new ViewUtils();
    @Override
    public String getView(HttpRequest request, DBConnectionManager connection) throws SQLException {
        String dbName = request.getParameter("dbName");
        String panelName = "Tables in " + dbName;
        int panelWidth = 60;
        List<String> tablesList = connection.executeListTables(dbName);

        String content = viewUtils.appendList(tablesList);
        return viewUtils.generateHtml(panelName, panelWidth, content);
    }
}
