package pl.edu.pw.ii.DBBrowser;

import pl.edu.pw.ii.DBBrowser.Utils.ConfigLoader;

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
    private ServerSocket serverSocket;
    private List<Client> clientList;
    public static ThreadManager getInstance() {
        return instance == null ? instance = new ThreadManager() : instance;
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
        try {
            Configuration.getInstance().load(Configuration.getInstance().getProperty("configFile"));
        } catch (IOException e) {
            System.out.println("An error occurred while loading config file: "
                    + Configuration.getInstance().getProperty("configFile")
                    + ", error message: "
                    + e.getMessage());
            //TODO uncomment below
            //System.exit(1);
        }
        try {
            serverSocket = new ServerSocket(Configuration.getInstance().getPropertyAsInt("port"));
        } catch (IOException e) {
            System.out.println("An error occurred while binding to port "
                    + Configuration.getInstance().getPropertyAsInt("port")
                    + ", error message: "
                    + e.getMessage());
            System.exit(1);
        }
        clientList = new LinkedList<Client>();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                ThreadManager.getInstance().shutdown();
            }
        });
        ThreadManager.getInstance().acceptClients();
    }

}
