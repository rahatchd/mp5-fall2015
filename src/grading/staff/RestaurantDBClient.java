package grading.staff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import grading.student.RestaurantDBServerUtil;

public class RestaurantDBClient {
    
    public static int CLIENT_TIMEOUT = 10000; // 10 second socket timeout

    private static final int MAX_RETRIES = 2;
    String host;
    int port;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    JSONInterpreter json;

    public RestaurantDBClient(String host, int port) {
        this.host = host;
        this.port = port;
        json = new JSONInterpreter();
        socket = null;
        reader = null;
        writer = null;
    }    

    /**
     * Initialize connection to server
     */
    public void connect() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(CLIENT_TIMEOUT);  // set timeout
            socket.setKeepAlive(true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (UnknownHostException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
            disconnect();
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e1) {
        }
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
        }
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public String maybeAddQuotes(String str, boolean quote) {
        if (quote) {
            return "\"" + str + "\"";
        }
        return str;
    }
    
    private List<JSONObject> tryQuery(String queryStr, int retryCount)
            throws IOException, QueryException {

        Object response = null;

        try {
            // try query
            writer.println(queryStr);
            writer.flush();
            response = json.readResponse(reader);
        } catch (Exception e) {
             // ignore exception
             // e.printStackTrace();
        }

        // failed due to server disconnect. Reconnect and try again
        if (response == null && retryCount < MAX_RETRIES) {
            reconnect();
            System.out.println("client reconnecting...");
            response = tryQuery(queryStr, retryCount + 1);
        }

        if (response == null) {
            throw new IOException("Failed to obtain a response from server");
        }
        if (!(response instanceof List)) {
            throw new QueryException("Unknown response from query: " + response.toString(), response);
        }

        List<?> responseList = (List<?>)response;
        ArrayList<JSONObject> result = new ArrayList<JSONObject>( responseList.size() );
        
        for (Object o : responseList ) {
            if (o instanceof JSONObject) {
                result.add((JSONObject)o);
            } else {
                throw new QueryException("Unknown response from query: " + response.toString(), response);
            }
        }
        
        return result;
    }

    /**
     * Send query to server
     * 
     * @param queryStr
     * @return
     * @throws ServerResponseException
     */
    public List<JSONObject> query(String queryStr) throws QueryException, IOException {
        return tryQuery(queryStr, 0);
    }

    /**
     * Try a request with multiple retries
     * @param request
     * @param retryCount
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws QueryException
     */
    private JSONObject tryRequest(String request, int retryCount) throws IOException, RequestException {
        
        Object response = null;
        try {
            writer.println(request);
            writer.flush();
    
            response = json.readResponse(reader);
        } catch (Exception e) {
            // ignore exception
            // System.out.println(e.getMessage());
        }
        
        if (response == null && retryCount < MAX_RETRIES) {
            reconnect();
            response = tryRequest(request, retryCount + 1);
        }

        if (response == null) {
            throw new IOException("Failed to obtain a response from server");
        }
        
        if (response instanceof List) {
            List<?> list = (List<?>)response;
            if (list.size() == 1) {
                // extract first element
                response = list.get(0);
            } else if (list.size() > 1) {
                // hmm... multiple responses?
                throw new RequestException("Multiple responses received: " + JSONInterpreter.toJSON(response, false), response );
            }
        }
        if (!(response instanceof Map)) {
            throw new RequestException("Unknown response from query", response);
        }

        JSONObject mresponse = (JSONObject)response;
        return mresponse;

    }
    
    public JSONObject addRestaurant(JSONObject r) throws IOException, RequestException {
        String request = RestaurantDBServerUtil.createAddRestaurantRequest(r);
        JSONObject response = tryRequest(request, 0);
        return response;
    }

    public JSONObject getRestaurant(String id) throws IOException, RequestException {
        String request = RestaurantDBServerUtil.createGetRestaurantRequest(id);
        JSONObject response = tryRequest(request, 0);
        if (!response.containsKey("business_id")) {
            throw new RequestException("Failed to get restaurant with ID: " + id, response);
        }
        return response;
    }

    public JSONObject addUser(JSONObject u) throws IOException, RequestException {
        String request = RestaurantDBServerUtil.createAddUserRequest(u);
        JSONObject response = tryRequest(request, 0);
        return response;
    }

    public JSONObject addReview(JSONObject r) throws IOException, RequestException {
        String request = RestaurantDBServerUtil.createAddReviewRequest(r);
        JSONObject response = tryRequest(request, 0);
        return response;
    }

    public JSONObject getRandomReview(String name) throws IOException, RequestException {

        String request = RestaurantDBServerUtil.createRandomReviewRequest(name);
        JSONObject response = tryRequest(request, 0);
        if (!response.containsKey("review_id")) {
            throw new RequestException("Failed to get review for restaurant with ID: " + name, response);
        }

        return response;
    }
    
    /**
     * @return whether or not that client is connected
     */
    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

}
