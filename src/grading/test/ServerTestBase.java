package grading.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;

import ca.ece.ubc.cpen221.mp5.RestaurantDBServer;
import grading.staff.FreePortUtility;
import grading.staff.RestaurantDBClient;
import grading.staff.StreamForwarder;

public class ServerTestBase {
    
    public static final int CONNECTION_TIMEOUT_MS = 10000;
    public static boolean SERVER_USE_THREAD = false;    // change this to true if you want to use a Thread instead of a process (perhaps for debugging)
    
    private static interface ServerRunner {
        public void start();
        public void shutdown();
        public int getPort();
        public boolean isAlive();
    }

    private static class ServerThread extends Thread implements ServerRunner {
        int port;
        String restaurants;
        String reviews;
        String users;

        public ServerThread(int port, String restaurants, String reviews, String users) {
            this.port = port;
            this.restaurants = restaurants;
            this.reviews = reviews;
            this.users = users;
        }

        @Override
        public void run() {
            try {
				RestaurantDBServer.main(new String[] { Integer.toString(port), restaurants, reviews, users });
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        @SuppressWarnings("deprecation")
        public void shutdown() {
            try {
                stop();
            } catch (Exception e) {}
        }
        
        public int getPort() {
            return this.port;
        }
        
    }

    private static class ServerProcess implements ServerRunner {
        Process process = null;
        ProcessBuilder builder = null;
        ExecutorService executor;
        int port;

        public ServerProcess(int port, String restaurants, String reviews, String users) {

            List<String> params = new ArrayList<String>();
            String separator = System.getProperty("file.separator");
            String java = System.getProperty("java.home") + separator + "bin" + separator + "java";

            params.add(java);
            params.add("-cp");
            params.add(System.getProperty("java.class.path"));
            params.add("ca.ece.ubc.cpen221.mp5.RestaurantDBServer");
            params.add(Integer.toString(port));
            params.add(restaurants);
            params.add(reviews);
            params.add(users);

            builder = new ProcessBuilder(params);
            executor = Executors.newFixedThreadPool(2);
            this.port = port;

        }

        @Override
        public void start() {
            // forward server's messages
            try {
                process = builder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            executor.execute(new StreamForwarder(process.getInputStream(), System.out));
            executor.execute(new StreamForwarder(process.getErrorStream(), System.err));            
        }

        public void shutdown() {
            if (process != null) {
                process.destroy();
                executor.shutdown();
            }
        }

        public int getPort() {
            return port;
        }
        
        @Override
        public boolean isAlive() {
            return process.isAlive();
        }
        
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            shutdown();
        }
    }

    static String restaurantFileName = "restaurants.json";
    static String reviewFileName = "reviews.json";
    static String userFileName = "users.json";
    
    // hold a list of clients
    ServerRunner server;
    ArrayList<RestaurantDBClient> clients;

    /**
     * Find a file somewhere in the directory tree starting at the parent of the working directory
     * @param name name of file
     * @return the file if found, null otherwise
     */
    public static File findFile(String name) {
        File f = new File(name);
        if (!f.exists()) {
            File parent = new File(".");

            // recursively find file?
            Queue<File> files = new LinkedList<>();
            files.add(parent);

            while (!files.isEmpty()) {
                parent = files.poll();
                f = new File(parent, name);
                if (f.exists()) {
                    return f;
                }
                if (parent.isDirectory()) {
                    for (File child : parent.listFiles()) {
                        files.add(child);
                    }
                }
            }

            return null;
        }

        return f;
    }
    
    public static ServerRunner launchServerProcess() {

        // start a server
        // find files somewhere in the directory tree
        File restaurantFile = findFile(restaurantFileName);
        File reviewFile = findFile(reviewFileName);
        File userFile = findFile(userFileName);

        if (restaurantFile == null || reviewFile == null || userFile == null) {
            throw new RuntimeException("Cannot find one of the data files");
        }

        ServerRunner server = null;

        synchronized (FreePortUtility.getLock()) {
            int port = FreePortUtility.getFreePort();
            // System.out.println("Starting server on port " + port);
            if (SERVER_USE_THREAD) {
                server = new ServerThread(port, restaurantFile.getAbsolutePath(), reviewFile.getAbsolutePath(),
                        userFile.getAbsolutePath());
            } else {
                server = new ServerProcess(port, restaurantFile.getAbsolutePath(), reviewFile.getAbsolutePath(),
                        userFile.getAbsolutePath());
            }
            server.start();

        }

        return server;
    }

    @Before
    public void setup() {
        startServer();
        clients = new ArrayList<>();
    }

    public void startServer() {
        server = launchServerProcess();
    }

    @After
    public void shutdown() {
        closeClients();
        server.shutdown();
    }

    public void closeClients() {
        for (RestaurantDBClient client : clients) {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        }
    }

    protected RestaurantDBClient createConnectedClient() {
        
        RestaurantDBClient client = createClient();
        long start = System.currentTimeMillis();
        while (!client.isConnected()) {
            client.connect();
            Thread.yield();
            if (!server.isAlive()) {
                throw new RuntimeException("Server is dead.");
            }
            if (System.currentTimeMillis()-start > CONNECTION_TIMEOUT_MS) {
                throw new RuntimeException("Server is not responding");
            }
        }
        return client;
    }

    protected RestaurantDBClient createClient() {
        RestaurantDBClient client = new RestaurantDBClient("localhost", server.getPort());
        clients.add(client);
        return client;
    }

}
