package pl.edu.pw.ii.DBBrowser;

import org.apache.log4j.Logger;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.View.ViewManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by lucas on 31.05.14.
 */
public class ThreadManager {
    public static final int MAX_CONNECTIONS_IN_ONE_MINUTE = 6;
    private static ThreadManager instance = null;
    private ServerSocket serverSocket;
    private List<Client> clientList;
    private Logger logger = Logger.getLogger(ThreadManager.class);
    public Map<String, Integer> clientStatistics = new HashMap<String, Integer>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                ThreadManager.getInstance().shutdown();
            }
        });
        new Thread(){

            @Override
            public void run()
            {
                while(true){
                    try {
                        ThreadManager.getInstance().clientStatistics.clear();
                        sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
        ThreadManager.getInstance().acceptClients();
    }

    public static ThreadManager getInstance() {
        if(instance == null){
            synchronized (ThreadManager.class){
                if(instance == null)
                    instance = new ThreadManager();
            }
        }
        return instance;
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
            System.exit(1);
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

    private void createAndRegisterNewClient(Socket socket) throws IOException {
        String address = getClientAddress(socket);
        if(isPotentialAttack(address)){
            logger.info("Potential DoS from address "+address+" prevented!");
            socket.close();
            return;
        }
        clientList.add(new Client(socket));
        clientList.get(clientList.size()-1).start();
    }

    private boolean isPotentialAttack(String address) {
        if(clientStatistics.get(address) == null)
            clientStatistics.put(address, 0);
        else{
            Integer connectionCount = clientStatistics.get(address);
            if(connectionCount > Configuration.getInstance().getPropertyAsInt("maxConnectionsInMinute"))
                return true;
            clientStatistics.remove(address);
            clientStatistics.put(address, connectionCount+1);
        }
        return false;
    }

    private String getClientAddress(Socket socket) {
        String address = socket.getRemoteSocketAddress().toString();
        return address.substring(1, address.indexOf(":"));
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

}
