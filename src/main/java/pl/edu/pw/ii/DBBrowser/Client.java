package pl.edu.pw.ii.DBBrowser;

import pl.edu.pw.ii.DBBrowser.RequestProcessor.RequestProcessor;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


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

    private void processRequest() throws IOException {

        PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //set timeout for IO operations
        socket.setSoTimeout(10000);

        String inputLine, outputLine;
        HttpResponse response;
        RequestProcessor rProcessor = new RequestProcessor();


        while((inputLine = inputStream.readLine())!= null && !finished) {
            System.out.println(inputLine);

            //???
            rProcessor.processRequest(inputLine);
            response = rProcessor.getResponse();
            if (response.equals(HttpResponse.incomplete()))
                continue;

            outputStream.print(response.toString());
        }

        //no need to close input and output streams as they will be automatically closed by socket.close()
    }
}
