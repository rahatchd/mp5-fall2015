package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The Restaurant Database Server is a server that can be started from the command line.
 * It contains three sets of data: restaurants, reviews and users.
 * 
 *
 */
public class RestaurantDBServer {
    private RestaurantDB restaurantDB;
    
    private static final String RANDOM = "randomReview";
    private static final String GET_RES = "getRestaurant";
    private static final String ADD_RES = "addRestaurant";
    private static final String ADD_REV = "addReview";
    private static final String ADD_USER = "addUser";
    
    private static final String IN = "in";
    private static final String NAME = "name";
    private static final String RATING = "rating";
    private static final String CATEGORY = "category";
    private static final String PRICE = "price";
	
	/*
     * RepInvariant:
     * 		resaurantDB != null
     */

    /**
     * Constructs a RestaurantDBServer that contains:
     * <ul>
     *      <li>the restaurants specified in the restaurantFile</li>
     *      <li>the users specified in the userFile</li>
     *      <li>the reviews specified in the reviewFile</li>
     * </ul>
     * 
     * 
     * @param port
     * @param restaurantFile the address of a file with restaurants in JSON format
     * @param reviewFile the address of a file with reviews in JSON format
     * @param userFile the address of a file with users in JSON format
     * @throws IOException 
     */
	public RestaurantDBServer(int port, String restaurantFile, String reviewFile, String userFile) throws IOException {
	    restaurantDB = new RestaurantDB(restaurantFile, reviewFile, userFile);
	    this.serve(port);
	    
	}
	
	// this constructor is for testing purposes only
	public RestaurantDBServer(String restaurantFile, String reviewFile, String userFile) throws IOException {
		restaurantDB = new RestaurantDB(restaurantFile, reviewFile, userFile);
	}
	
	public void serve(int portNumber) throws IOException {
	    @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(portNumber);
	    
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        // this exception wouldn't terminate serve(),
                        // since we're now on a different thread, but
                        // we still need to handle it
                        ioe.printStackTrace();
                    }
                }
            });
            // start the thread
            handler.start();
        }
        
    }
	
	/**
     * Handle one client connection. Returns when client disconnects.
     * 
     * @param socket
     *            socket where client is connected
     * @throws IOException
     *             if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
        System.err.println("client connected");

        // get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        // similarly, wrap character=>bytestream converter around the
        // socket output stream, and wrap a PrintWriter around that so
        // that we have more convenient ways to write Java primitive
        // types to it.
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        try {
            while (true){
                String query = in.readLine();
                if (query != null){
                    String outString = handleQuery(query);
                    out.println(outString);
                    out.flush();
                }
            }      
        } finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Handles a given query by delegating it to the database's methods.
     * 
     * @param query
     * @return the return value of the query
     */
    public String handleQuery(String query){
    	try {
    		String type = query.substring(0, query.indexOf("("));
    		String details = query.substring(query.indexOf("(\"") + 2, query.indexOf("\")"));
    		String result = new String();
    		switch (type) {
    		case RANDOM :{
    			result = restaurantDB.randomReview(details);
    			break;
    		}
    		case GET_RES :{
    			result = restaurantDB.getRestaurant(details);
    			break;
    		}
    		case ADD_RES :{
    			if (restaurantDB.addRestaurant(details))
    				result = "Restaurant added successfully.";
    			else result = "Restaurant was already in database.";
    			break;
    		}
    		case ADD_REV :{
    			if (restaurantDB.addReview(details))
    				result = "Review added successfully.";
    			else result = "Review was already in database.";
    			break;
    		}
    		case ADD_USER :{
    			if (restaurantDB.addUser(details))
    				result = "User added successfully.";
    			else result = "User was already in database.";
    			break;
    		}
    		}
    	
    		if (result.isEmpty())
    		    if (query.contains(IN) || query.contains(NAME) || query.contains(RATING) || query.contains(CATEGORY)
                        || query.contains(PRICE)) {
                    return convertToJSONString(restaurantDB.query(query));
                }
    		return result;
    	
    		} catch(IndexOutOfBoundsException e) {
    		    if (query.contains(IN) || query.contains(NAME) || query.contains(RATING) || query.contains(CATEGORY)
                        || query.contains(PRICE)) {
                    return convertToJSONString(restaurantDB.query(query));
                }
    		return "Invalid query: Check your formatting.";
    	}
    	
    }
    
    private String convertToJSONString(Set<Restaurant> set) {
    	StringBuffer JSONString = new StringBuffer();
    	JSONString.append("[");
    	for (Restaurant r: set) {
    		JSONString.append("\"" + r.name + "\", ");
    	}
    	if (!set.isEmpty())
    	    JSONString.delete(JSONString.length() - 1, JSONString.length());
    	JSONString.append("]");
    	return new String(JSONString);
    }
    
    // Used for debugging.
    public static void main(String args[]) throws IOException {
    	RestaurantDBServer server =
    			new RestaurantDBServer("data/restaurants.json", "data/reviews.json","data/users.json");
    	String result1 = server.handleQuery("randomReview(\"Cafe 3\")");
    	String result2 = server.handleQuery("getRestaurant(\"gclB3ED6uk6viWlolSb_uA\")");
    	String result3 = server.handleQuery("addRestaurant(\"{\"open\": true, "
		        + "\"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", "
		        + "\"longitude\": -122.260408, "
		        + "\"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], "
		        + "\"business_id\": \"fake id\", "
		        + "\"name\": \"fake name\", "
		        + "\"categories\": [\"Cafes\", \"Restaurants\"], "
		        + "\"state\": \"CA\", "
		        + "\"type\": \"business\", "
		        + "\"stars\": 2.0, "
		        + "\"city\": \"Berkeley\", "
		        + "\"full_address\": \"2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701\", "
		        + "\"review_count\": 9, "
		        + "\"photo_url\":\"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", "
		        + "\"schools\": [\"University of California at Berkeley\"], "
		        + "\"latitude\": 37.867417, "
		        + "\"price\": 1}\")");
    	String result4 = server.handleQuery("addUser(\"{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, "
				+ "\"review_count\": 29, "
				+ "\"type\": \"user\", "
				+ "\"user_id\": \"fake id\", "
				+ "\"name\": \"fake name\", "
				+ "\"average_stars\": 3.89655172413793}\")");
    	String result5 = server.handleQuery("addReview(\"{\"type\": \"review\", "
				+ "\"business_id\": \"fake id\", "
				+ "\"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, "
				+ "\"review_id\": \"fake id\", "
				+ "\"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.\", "
				+ "\"stars\": 2, "
				+ "\"user_id\": \"fake id\", "
				+ "\"date\": \"2006-07-26\"}\")");
    	String result6 = server.handleQuery("ThIS AiN't NO QueRy");
    	String result7 = server.handleQuery("addRestaurant(\"{\"open\": true, "
		        + "\"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", "
		        + "\"longitude\": -122.260408, "
		        + "\"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], "
		        + "\"business_id\": \"fake id\", "
		        + "\"name\": \"fake name\", "
		        + "\"categories\": [\"Cafes\", \"Restaurants\"], "
		        + "\"state\": \"CA\", "
		        + "\"type\": \"business\", "
		        + "\"stars\": 2.0, "
		        + "\"city\": \"Berkeley\", "
		        + "\"full_address\": \"2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701\", "
		        + "\"review_count\": 9, "
		        + "\"photo_url\":\"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", "
		        + "\"schools\": [\"University of California at Berkeley\"], "
		        + "\"latitude\": 37.867417, "
		        + "\"price\": 1}\")");
    	String result8 = server.handleQuery("addUser(\"{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, "
				+ "\"review_count\": 29, "
				+ "\"type\": \"user\", "
				+ "\"user_id\": \"fake id\", "
				+ "\"name\": \"fake name\", "
				+ "\"average_stars\": 3.89655172413793}\")");
    	String result9 = server.handleQuery("addReview(\"{\"type\": \"review\", "
				+ "\"business_id\": \"fake id\", "
				+ "\"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, "
				+ "\"review_id\": \"fake id\", "
				+ "\"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.\", "
				+ "\"stars\": 2, "
				+ "\"user_id\": \"fake id\", "
				+ "\"date\": \"2006-07-26\"}\")");
    	String result10 = server.handleQuery("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(1..2)");
    	System.out.println(result1);
    	System.out.println(result2);
    	System.out.println(result3);
    	System.out.println(result4);
    	System.out.println(result5);
    	System.out.println(result6);
    	System.out.println(result7);
    	System.out.println(result8);
    	System.out.println(result9);
    	System.out.println(result10);
    }

}
