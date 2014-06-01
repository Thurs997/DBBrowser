package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import java.util.Map;

/**
 * Created by kokoss on 5/31/14.
 */
public class HttpRequest {
    private String path;
    private String requestBody;
    private Map<String, String> headerMap;
    private Map<String, String> parameterMap;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public String getParameter(String key){
        return parameterMap.get(key);
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
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

}
