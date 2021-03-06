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
        int start = Integer.valueOf(request.getParameter("start"));
        int count = Integer.valueOf(request.getParameter("count"));
        String panelName = "Table " + tableName;
        int panelWidth = 100;
        String[][] tableContent = new String[0][];
        try {
            tableContent = connection.executeListTableContent(dbName, tableName, start, count);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringBuffer content = new StringBuffer();
        content.append(viewUtils.appendTableNextPrev("/rows?dbName="+dbName+"&tableName="+tableName, start, count));
        content.append(viewUtils.appendTable(tableContent));
        content.append(viewUtils.appendTableNextPrev("/rows?dbName="+dbName+"&tableName="+tableName, start, count));
        return viewUtils.generateHtml(connection, panelName, panelWidth, content.toString());
    }
}
