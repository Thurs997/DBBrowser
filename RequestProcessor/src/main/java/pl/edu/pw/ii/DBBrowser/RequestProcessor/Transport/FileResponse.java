package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

/**
 * Created by kokoss on 5/31/14.
 */
public class FileResponse {
    String name;
    byte[] content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
