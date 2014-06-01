package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

/**
 * Created by kokoss on 5/31/14.
 */
public class FileResponse {
    String contentType;
    byte[] content;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
