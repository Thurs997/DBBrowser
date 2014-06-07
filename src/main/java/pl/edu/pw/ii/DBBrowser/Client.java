package pl.edu.pw.ii.DBBrowser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.io.HttpMessageParser;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.RequestProcessor;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class Client extends Thread {
    Socket socket;
    volatile boolean finished = false; //guard for stopping thread

    public Client(Socket s) {
        this.socket = s;
    }

    @Override
    public void run()
    {
        try {
            processRequest();
        } catch (SocketException e)
        {
            System.out.println("socket timeout");
            ThreadManager.getInstance().deregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            finished = true; //not sure if necessary - see below
            this.socket.close(); //this closes input and output streams and therefore unblocks thread - check for stability
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void processRequest() throws IOException, HttpException {

        PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
        SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8 * 1024);
        sessionInputBuffer.bind(socket.getInputStream());
        //set timeout for IO operations
        socket.setSoTimeout(10000);

        HttpResponse response;
        RequestProcessor rProcessor = new RequestProcessor();
        HttpMessageParser<HttpRequest> httpMessageParser = new DefaultHttpRequestParser(sessionInputBuffer);

        HttpRequest request = httpMessageParser.parse();
        while(request != null) {
            System.out.println(request.getRequestLine().getUri());

            response = rProcessor.processRequest(request);

            outputStream.print(response.toBytes());
            outputStream.flush();
            request = httpMessageParser.parse();
        }

        //no need to close input and output streams as they will be automatically closed by socket.close()
    }
}
