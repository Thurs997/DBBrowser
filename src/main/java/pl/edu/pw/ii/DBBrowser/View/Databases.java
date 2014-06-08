package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.Configuration;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.View;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
@ViewHandler(path ="/")
public class Databases implements View {

    ViewUtils viewUtils = new ViewUtils();

    @Override
    public String getView(HttpRequest request, DBConnectionManager DBConnection) {
        String panelName = "Databases";
        int panelWidth = 60;
        List<String> databasesList = null;
        try {
            databasesList = DBConnection.executeListDatabases();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String content = viewUtils.appendList("/tables?dbName=", databasesList);
        return viewUtils.generateHtml(DBConnection, panelName, panelWidth, content);
    }

}
