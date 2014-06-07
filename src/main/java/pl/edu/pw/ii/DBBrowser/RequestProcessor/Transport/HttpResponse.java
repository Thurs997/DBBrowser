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
        addMandatoryHeaders();
        List<byte[]> responseLines = getResponseLines();
        ByteBuffer byteBuffer = getResponseByteBuffer(responseLines);
        return byteBuffer.array();
    }

    private ByteBuffer getResponseByteBuffer(List<byte[]> responseLines) {
        int size = 0;
        for(byte[] line : responseLines)
            size += line.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        for(byte[] line : responseLines)
            byteBuffer.put(line);
        return byteBuffer;
    }

    private List<byte[]> getResponseLines() {
        List<byte[]> responseLines = new ArrayList<byte[]>();
        Object[] statusObject = {status.code, status.getName()};
        responseLines.add(statusLine.format(statusObject).getBytes());
        for(Map.Entry<String, String> header : headerMap.entrySet()){
            Object[] headerObject = {header.getKey(), header.getValue()};
            responseLines.add(headerFormat.format(headerObject).getBytes());
        }
        responseLines.add(LINE_DELIMETER.getBytes());
        if(content != null)
            responseLines.add(content);
        return responseLines;
    }

    private void addMandatoryHeaders() {
        addHeader(Header.CONTENT_LENGTH.key, content == null ? "0" : String.valueOf(content.length));
        if(mimeType == null)
            mimeType = "text/plain";
        addHeader(Header.CONTENT_TYPE.key, mimeType);
    }

    public enum Status{
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

        String getName(){
            return WordUtils.capitalize(name().toLowerCase().replace("_", " "));
        }

    }

    public enum Header{
        CONTENT_LENGTH("Content-Length"),
        CONTENT_TYPE("Content-Type");

        public String key;
        Header(String key){
            this.key = key;
        }
    }
}
