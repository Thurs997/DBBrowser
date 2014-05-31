package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by kokoss on 5/31/14.
 */
public class HttpRequestTest {
    private static final String TEST_PATH = "/";
    private static final HttpRequest.HttpMethod TEST_METHOD = HttpRequest.HttpMethod.POST;
    private static final String TEST_HEADER_KEY = "Content-Length";
    private static final String TEST_HEADER_VALUE = "0";
    private static final String TEST_PARAMETER_KEY = "test";
    private static final String TEST_PARAMETER_VALUE = "1";

    @Test
    public void HttpRequestApi(){
        HttpRequest testRequest = createTestHttpRequest();
        readTestHttpRequest(testRequest);
    }

    private HttpRequest createTestHttpRequest() {
        HttpRequest testRequest = new HttpRequest();
        testRequest.setPath(TEST_PATH);
        testRequest.setMethod(TEST_METHOD);
        testRequest.setHeaderMap(Collections.singletonMap(TEST_HEADER_KEY, TEST_HEADER_VALUE));
        testRequest.setParameterMap(Collections.singletonMap(TEST_PARAMETER_KEY, TEST_PARAMETER_VALUE));
        return testRequest;
    }

    private void readTestHttpRequest(HttpRequest testRequest) {
        assertEquals(TEST_PATH, testRequest.getPath());
        assertEquals(TEST_METHOD, testRequest.getMethod());
        assertEquals(TEST_HEADER_VALUE, testRequest.getHeaderMap().get(TEST_HEADER_KEY));
        assertEquals(TEST_HEADER_VALUE, testRequest.getHeader(TEST_HEADER_KEY));
        assertEquals(TEST_PARAMETER_VALUE, testRequest.getParameterMap().get(TEST_PARAMETER_KEY));
        assertEquals(TEST_PARAMETER_VALUE, testRequest.getParameter(TEST_PARAMETER_KEY));
    }


}
