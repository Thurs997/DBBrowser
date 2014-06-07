package pl.edu.pw.ii.DBBrowser;

import org.apache.http.*;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.io.HttpMessageParser;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.RequestProcessor;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

import java.io.*;
import java.net.*;


public class Client extends Thread {
    Socket socket;
    int id;
    volatile boolean finished = false; //guard for stopping thread

    public Client(Socket s) {
        this.socket = s;
        this.id = (int) (Math.random()*1000);
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
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        SessionInputBufferImpl sessionInputBuffer = new SessionInputBufferImpl(new HttpTransportMetricsImpl(), 8 * 1024);
        sessionInputBuffer.bind(socket.getInputStream());
        //set timeout for IO operations
        socket.setSoTimeout(60000);

        HttpResponse response;
        RequestProcessor rProcessor = new RequestProcessor();
        HttpMessageParser<HttpRequest> httpMessageParser = new DefaultHttpRequestParser(sessionInputBuffer);
        HttpRequest request = null;
        try{
            while(true) {
                try{
                    request = httpMessageParser.parse();
                } catch(ConnectionClosedException e){
                    continue;
                }
                System.out.println(id+": "+request.getRequestLine().getUri());

                response = rProcessor.processRequest(request);
                out.write(response.toBytes());
                out.flush();
            }
        } catch(SocketTimeoutException e){
            System.out.println("soTimeout - closing");
            socket.close();
            //System.exit(0);
        }

        //no need to close input and output streams as they will be automatically closed by socket.close()
    }
}
