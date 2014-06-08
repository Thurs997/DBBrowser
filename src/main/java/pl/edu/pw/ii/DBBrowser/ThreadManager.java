package pl.edu.pw.ii.DBBrowser;

import org.apache.log4j.Logger;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;
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
    private Logger logger = Logger.getLogger(ThreadManager.class);
    public static ThreadManager getInstance() {
        return instance == null ? instance = new ThreadManager() : instance;
    }

    public void shutdown() {
        logger.info("Server shutting down...");
        logger.info("Closing socket.");
        try {
            serverSocket.close();
        } catch(IOException e) {
            logger.error("An error occurred while closing socket, reason: " + e.getMessage());
        }
        logger.info("Deregistering clients.");
        deregisterClients();
        logger.info("Bye!");
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
            logger.info("Server socket closed");
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
        logger.info("");
        logger.info("/******************************************************\\");
        logger.info("|********************  DB BROWSER  ********************|");
        logger.info("\\******************************************************/");
        logger.info("Server is starting...");
        logger.info("Loading configuration...");
        try {
            Configuration.getInstance().load(Configuration.getInstance().getProperty("configFile"));
        } catch (IOException e) {
            logger.error("An error occurred while loading config file: "
                    + Configuration.getInstance().getProperty("configFile")
                    + ", error message: "
                    + e.getMessage());
            //TODO uncomment below
            //System.exit(1);
        }
        logger.info("Configuration loaded!");
        logger.info("Binding to port "+Configuration.getInstance().getPropertyAsInt("port")+"...");
        try {
            serverSocket = new ServerSocket(Configuration.getInstance().getPropertyAsInt("port"));
        } catch (IOException e) {
            logger.error("An error occurred while binding to port "
                    + Configuration.getInstance().getPropertyAsInt("port")
                    + ", error message: "
                    + e.getMessage());
            System.exit(1);
        }
        logger.info("Binding complete!");
        try {
            ViewManager.getInstance();
        } catch (Throwable e) {
            logger.error("An error occurred while scanning for views"
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
