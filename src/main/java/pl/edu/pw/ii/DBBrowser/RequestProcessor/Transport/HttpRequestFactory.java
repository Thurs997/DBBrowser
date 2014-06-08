package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import com.google.common.base.Charsets;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public abstract class HttpRequestFactory {
    public static HttpRequest createHttpRequest(org.apache.http.HttpRequest source){
        HttpRequest destination = new HttpRequest();
        try {
            String uri = URLDecoder.decode(source.getRequestLine().getUri(), "UTF-8");

            destination.setPath(!uri.contains("?") ? uri : uri.substring(0, uri.indexOf("?")));
            if(uri.contains("?")){
                List<NameValuePair> params = URLEncodedUtils.parse(uri.substring(uri.indexOf("?") + 1, uri.length()), Charsets.UTF_8);
                for(NameValuePair param : params)
                    destination.addParameter(param.getName(), param.getValue());
            }
            Header[] headers = source.getAllHeaders();
            for(Header header : headers)
                destination.addHeader(header.getName(), header.getValue());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return destination;
    }
}
