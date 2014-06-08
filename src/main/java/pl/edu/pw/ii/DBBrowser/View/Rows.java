package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

import java.sql.SQLException;

@ViewHandler(path="/rows")
public class Rows implements View {

    ViewUtils viewUtils = new ViewUtils();

    @Override
    public String getView(HttpRequest request, DBConnectionManager connection) {
        String dbName = request.getParameter("dbName");
        String tableName = request.getParameter("tableName");
        String panelName = "Table " + connection;
        int panelWidth = 100;
        String[][] tableContent = new String[0][];
        try {
            tableContent = connection.executeListTableContent(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String content = viewUtils.appendTable(tableContent);
        return viewUtils.generateHtml(panelName, panelWidth, content);
    }
}
