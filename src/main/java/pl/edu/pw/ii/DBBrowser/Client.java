package pl.edu.pw.ii.DBBrowser;

import org.apache.http.*;
import org.apache.http.impl.io.*;
import org.apache.http.io.HttpMessageParser;
import org.apache.log4j.Logger;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.RequestProcessor;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.MessageFormat;


public class Client extends Thread {
    private Socket socket;
    private volatile boolean finished = false; //guard for stopping thread
    private RequestProcessor requestProcessor = null;
    private Logger logger = Logger.getLogger("RequestLogger");
    private MessageFormat logEntry = new MessageFormat("{0} GET {1} - {2} {3} Response length {4}B");
    public Client(Socket s) {
        this.socket = s;
    }

    @Override
    public void run()
    {
        try {
            processRequest();
        } catch (Throwable e) {
            if(requestProcessor != null)
                requestProcessor.closeDBConnection();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            ThreadManager.getInstance().deregister(this);
        }
    }

    public void close()
    {
        try {
            finished = true;
            this.requestProcessor.closeDBConnection();
            this.socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void processRequest() throws IOException {
        SessionInputBufferImpl sessionInputBuffer = null;
        DataOutputStream out = null;
        out = new DataOutputStream(socket.getOutputStream());
        sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8 * 1024);
        sessionInputBuffer.bind(socket.getInputStream());
        //set timeout for IO operations
        socket.setSoTimeout(10000);

        HttpResponse response;
        requestProcessor = new RequestProcessor();
        HttpMessageParser<HttpRequest> httpMessageParser = new DefaultHttpRequestParser(sessionInputBuffer);
        HttpRequest request = null;
        while(!finished) {
            try{
                request = httpMessageParser.parse();
            } catch (ConnectionClosedException e){
                continue;
            } catch (HttpException e) {
                e.printStackTrace();
            }
            response = requestProcessor.processRequest(request);
            Object[] logEntryData = {
                    socket.getRemoteSocketAddress().toString().substring(1, socket.getRemoteSocketAddress().toString().indexOf(":")),
                    request.getRequestLine().getUri(),
                    response.getStatus().code,
                    response.getStatus().getName(),
                    (response.getContent() == null ? 0 : response.getContent().length)
            };
            logger.info(logEntry.format(logEntryData));
            out.write(response.toBytes());
            out.flush();
        }
    }
}
