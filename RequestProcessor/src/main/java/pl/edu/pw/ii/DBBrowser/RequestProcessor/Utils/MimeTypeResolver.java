package pl.edu.pw.ii.DBBrowser.RequestProcessor.Utils;

/**
 * Created by lucas on 07.06.14.
 */
public class MimeTypeResolver {

    public static String getFileMimeType(String fileName) {

        if (fileName == null || fileName.equals("")) return null;

        String ext = fileExt(fileName);
        String type = "text/plain";

        if("txt".equals(ext)) type = "text/plain";
        else if ("xls".equals(ext)) type = "application/vnd.ms-excel";
        else if ("doc".equals(ext)) type = "application/msword";
        else if ("html".equals(ext) || "htm".equals(ext)) type = "text/html";
        else if ("jpg".equals(ext) || "jpeg".equals(ext)) type = "image/jpeg";
        else if ("bmp".equals(ext)) type = "image/bmp";
        else if ("pdf".equals(ext)) type = "application/pdf";
        else if ("ppt".equals(ext)) type = "application/vnd.ms-powerpoint";
        else if ("xml".equals(ext)) type = "text/xml";
        else if ("zip".equals(ext)) type = "application/vnd.ms-zip";
        else if ("gif".equals(ext)) type = "image/gif";
        else if ("png".equals(ext)) type = "image/png";
        else if ("tif".equals(ext) || "tiff".equals(ext)) type = "image/tiff";
        else if ("csv".equals(ext)) type = "text/csv";
        else if ("odt".equals(ext)) type = "application/vnd.oasis.opendocument.text";
        else if ("ods".equals(ext)) type = "application/vnd.oasis.opendocument.spreadsheet";
        else if ("rtf".equals(ext)) type = "application/rtf";
        else if ("docx".equals(ext)) type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        else if ("xlsx".equals(ext)) type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        else if ("pptx".equals(ext)) type = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

        return type;
    }

    public static String fileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.')+1,fileName.length());
    }
}