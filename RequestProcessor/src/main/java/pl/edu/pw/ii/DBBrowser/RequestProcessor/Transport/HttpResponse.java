package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kokoss on 5/31/14.
 */
public class HttpResponse {
    boolean complete;
    Map<String, String> headerMap = new HashMap<String, String>();
    byte[] content;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public static HttpResponse incomplete() {
        HttpResponse incomplete = new HttpResponse();
        incomplete.setComplete(false);
        return incomplete;
    }

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
}
