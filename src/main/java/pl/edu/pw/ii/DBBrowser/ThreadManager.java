package pl.edu.pw.ii.DBBrowser;


import pl.edu.pw.ii.DBBrowser.RequestProcessor.RequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lucas on 31.05.14.
 */
public class ThreadManager {
    private static ThreadManager instance = null;
    private int port;
    private ServerSocket serverSocket;
    private List<Client> clientList;

    public static ThreadManager getInstance() {
        return instance == null ? instance = new ThreadManager() : instance;
    }

    public void init(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientList = new LinkedList<Client>();
    }

    public void shutdown() {
        System.out.println("Server shutting down...");
        System.out.println("Closing socket.");
        try {
            serverSocket.close();
        } catch(IOException e) {
            System.out.println("An error occurred while closing socket, reason: " + e.getMessage());
        }
        System.out.println("Deregistering clients.");
        deregisterClients();
        System.out.println("Bye!");
    }

    private void deregisterClients() {
        while(!clientList.isEmpty()) {
            deregister(clientList.get(0));
        }
    }

    public synchronized void deregister(Client client) {
        client.close();
        clientList.remove(client);
    }



    public void acceptClients() {
        try {
            while(true) {
                acceptClient();
            }
        } catch (IOException e) {
            System.out.println("Server socket closed, reason: " + e.getMessage());
        }
    }

    private void acceptClient() throws IOException {
            Socket socket = serverSocket.accept();
            createAndRegisterNewClient(socket);
    }

    private void createAndRegisterNewClient(Socket socket) {
        clientList.add(new Client(socket));
        clientList.get(clientList.size()-1).start();
    }

    private ThreadManager() {

    }

    public static void main(String[] args) {
        RequestProcessor rp = new RequestProcessor();
        rp.processRequest("bla");
        System.out.print("bla");
        if(1==1)
            return;
        final int DEFAULT_PORT = 80;
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                ThreadManager.getInstance().shutdown();
            }
        });

        int port = DEFAULT_PORT;
        if(args.length != 0 && args.length >= 2) {
            if(args[0].equals("-p")) {
                port = Integer.parseInt(args[1]);
            } else {
                System.out.print("Args");
            }
        }

        try {
            ThreadManager.getInstance().init(port);
        } catch(IOException e) {
            System.out.println("An error occurred while binding to port " + port + ", error message: " + e.getMessage());
        }

        ThreadManager.getInstance().acceptClients();
    }

}
