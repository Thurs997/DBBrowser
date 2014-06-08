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
public class MainView implements View {

    private String newListLink(String name, String ref) {
        return "      <a href=\"" + ref + "\" class=\"list-group-item\">" + name + "</a>\n";
    }

    private String appendList(List<String> dataList) {
        String htmlListString = "";
        for (int i=0;i<5;i++)
            htmlListString += newListLink(dataList.get(i), dataList.get(i));
        //@TODO: powyzej zakladam na razie, ze nazwa bazy danych/tabeli na liscie == nazwa sciezki

        return htmlListString;
    }

    private String appendTable(String[][] tableContent) {
        String table = "      <table class=\"table table-hover\">\n";

        //add headers
        table += "       <thead>\n        <tr>\n";
        for (int j=0;j<tableContent[0].length;j++)
        {
            table += "         <th>" + tableContent[0][j] + "</th>\n";
        }
        table += "       </thead>\n      <tbody>\n";


        //add body
        for (int i=1;i<tableContent.length;i++)
        {
            table += "       <tr>\n";

            for (int j=0; j<tableContent[i].length;j++)
                table += "        <td>" + tableContent[i][j] + "</td>\n";

            table += "       </tr>\n";

        }

        table += "      </tbody>\n      </table>\n";

        return table;

    }


    private String generateHtml(String panelName, int panelWidth, String content) {
        String dbUrl = "DatabaseUrl"; //@TODO: change it
        String dbUser = "User"; //@TODO: change it

        String header= "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <title>DBBManager</title>\n" +
                "\n" +
                "    <!-- Bootstrap core CSS -->\n" +
                "    <link href=\"bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "    <!-- css style -->\n" +
                "    <style type=\"text/css\">\n" +
                "    body {\n" +
                "     padding-top: 70px;\n" +
                "    }\n" +
                "    .index-databases {\n" +
                "    padding: 40px 15px;\n" +
                "    text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .panel {\n" +
                "      width: " + panelWidth + "%;\n" +
                "      margin-left: auto;\n" +
                "      margin-right: auto;\n" +
                "    }\n" +
                "    </style>\n" +
                "\n" +
                "\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "\n" +
                "    <div class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">\n" +
                "      <div class=\"container\">\n" +
                "        <p class=\"navbar-text\">You are signed as " + dbUser + " to " + dbUrl + " database.</p>\n" +
                "      </div>\n" +
                "    </div>";

        String panelStart = "    <div class=\"container\">\n" +
                "    <div class=\"panel panel-default\">\n" +
                "     <div class=\"panel-heading\">\n" +
                "      <h3 class=\"panel-title\">"+ panelName + "</h3>\n" +
                "     </div>\n" +
                "    <div class=\"panel-body\">\n" +
                "    <div class=\"list-group\">\n";

        String footer = "     </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "\n" +
                "    </div><!-- /.container -->\n" +
                "\n" +
                "  </body>\n" +
                "</html>";

        return header + panelStart + content + footer;
    }

    private String createPage(int viewType, String parameter, DBConnectionManager connManager) throws SQLException {
        String panelName = "" , content = "";
        int panelWidth = 0;
        if (viewType == 0) //list databases
        {
            panelName = "Databases in " + parameter;
            panelWidth = 60;
            List<String> databasesList = connManager.executeListDatabases();

            content = appendList(databasesList);
        }
        else if (viewType == 1) //list tables in database
        {
            panelName = "Tables in " + parameter;
            panelWidth = 60;
            List<String> tablesList = connManager.executeListTables(parameter);

            content = appendList(tablesList);
        }
        else if (viewType==2)
        {
            panelName = "Table " + parameter;
            panelWidth = 100;
            String[][] tableContent = connManager.executeListTableContent(parameter);

            content = appendTable(tableContent);

        }

        return generateHtml(panelName, panelWidth, content);

    }

    @Override
    public String getView(HttpRequest request, DBConnectionManager DBConnection) throws SQLException {
        String rPath = request.getPath();
        int type = 0;
        String parameter = "dbName";


        return createPage(type, parameter, DBConnection);
    }
}
