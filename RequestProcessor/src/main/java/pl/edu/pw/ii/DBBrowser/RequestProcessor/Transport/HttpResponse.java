package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class HttpResponse {

    private String LINE_DELIMETER = "\r\n";
    MessageFormat statusLine = new MessageFormat("HTTP/1.1 {0} {1}" + LINE_DELIMETER);
    MessageFormat headerFormat = new MessageFormat("{0}: {1}" + LINE_DELIMETER);

    Status status = Status.OK;
    String mimeType = null;
    byte[] content = null;
    Map<String, String> headerMap = new HashMap<String, String>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void addHeader(String key, String value){
        headerMap.put(key, value);
    }

    public byte[] toBytes() {
        addHeader("Content-Length", content == null ? "0" : String.valueOf(content.length));
        if(mimeType == null)
            mimeType = "text/plain";
        addHeader("Content-Type", mimeType);
        List<byte[]> responseLines = new ArrayList<byte[]>();
        responseLines.add(statusLine.format(new Object[]{ status.code, Status.getName(status)}).getBytes());
        for(Map.Entry<String, String> header : headerMap.entrySet())
            responseLines.add(headerFormat.format(new Object[]{ header.getKey(), header.getValue()}).getBytes());
        responseLines.add(LINE_DELIMETER.getBytes());
        if(content != null)
            responseLines.add(content);
        int size = 0;
        for(byte[] line : responseLines)
            size += line.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        for(byte[] line : responseLines)
            byteBuffer.put(line);
        return byteBuffer.array();
    }

    enum Status{
        SERVICE_UNAVAILABLE(503),
        URI_TOO_LONG(414),
        REQUEST_HEADER_FIELDS_TOO_LARGE(431),
        NOT_IMPLEMENTED(501),
        UNAUTHORIZED(401),
        INTERNAL_SERVER_ERROR(500),
        NOT_FOUND(404),
        OK(200);

        public int code;

        Status(int code){
            this.code = code;
        }

        static String getName(Status status){
            return WordUtils.capitalize(status.name().toLowerCase().replace("_", " "));
        }
    }
}
