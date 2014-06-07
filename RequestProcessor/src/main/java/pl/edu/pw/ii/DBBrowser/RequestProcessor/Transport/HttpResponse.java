package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.Utils.MimeTypeResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class HttpResponse {
    Map<String, String> headerMap = new HashMap<String, String>();

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    String mimeType;
    byte[] content;

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public String getHeader(String key){
        return headerMap.get(key);
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void addHeader(String key, String value){
        headerMap.put(key, value);
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String toBytes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\r\n")
                     .append("Content-Type: " + "application/json" + "\r\n")
                     .append("Content-Length: " + 1 + "\r\n\r\n")
                    .append("x");

        return stringBuilder.toString();
    }
}
