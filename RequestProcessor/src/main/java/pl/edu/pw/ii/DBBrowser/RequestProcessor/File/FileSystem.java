package pl.edu.pw.ii.DBBrowser.RequestProcessor.File;

import org.apache.commons.io.IOUtils;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.FileResponse;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Scanner;

/**
 * Created by kokoss on 5/31/14.
 */
public class FileSystem {

    public FileResponse getFile(String path) {
        try {
            return tryGetFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FileResponse tryGetFile(String path) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("fs/"+path);
        if(is == null)
            return null;
        byte[] fileContent = IOUtils.toByteArray(is);
        String fileContentType = resolveMimeType(path);
        FileResponse response = new FileResponse();
        response.setContent(fileContent);
        response.setContentType(fileContentType);
        return response;
    }

    private String resolveMimeType(String path) {
        String ext = extractFileExt(path);
        return "text/plain";
    }

    private String extractFileExt(String path) {
        String[] pathExploded = path.split("/");
        String fileName = pathExploded[pathExploded.length-1];
        String[] fileNameExploded = fileName.split(".");
        return fileNameExploded[fileNameExploded.length-1];
    }

    public String getFileAsString(String path) throws IOException {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("fs/"+path);
        StringWriter writer = new StringWriter();
        IOUtils.copy(file, writer, "utf-8");
        return writer.toString();
    }
}
