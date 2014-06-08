package pl.edu.pw.ii.DBBrowser.View;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.DBConnectionManager;

import java.util.List;

public class ViewUtils {
    private String newListLink(String name, String ref) {
        return "      <a href=\"" + ref + "\" class=\"list-group-item\">" + name + "</a>\n";
    }

    protected String appendList(String linkPrefix, List<String> dataList) {
        String htmlListString = "";
        for(String link : dataList)
            htmlListString += newListLink(link, linkPrefix+link);
        return htmlListString;
    }

    protected String appendTable(String[][] tableContent) {
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


    protected String generateHtml(DBConnectionManager connection, String panelName, int panelWidth, String content) {

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
                "        <p class=\"navbar-text\">You are signed as " + connection.getUser() + " to " + connection.getUrl() + " database.</p>\n" +
                "      </div>\n" +
                "    </div>";

        String panelStart = "    <div class=\"container\">\n" +
                "    <div class=\"panel panel-default\">\n" +
                "     <div class=\"panel-heading\">\n" +
                "      <h3 class=\"panel-title\">"+ panelName + "</h3> "
                +(panelName.equals("Databases") ? "" : "<a style\"cursor: pointer\" onclick=\"window.history.back()\">Back</a>\n") +
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
}
