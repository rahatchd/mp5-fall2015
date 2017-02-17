package grading.staff;

import org.json.simple.JSONObject;

/**
 * Wrapper around a client request to provide a uniform interface
 * @author Antonio
 *
 */
public class ClientRequestWrapper extends TriggeredCallable<Object> {

    public static enum RequestType {
        GET_RESTAURANT,
        RANDOM_REVIEW,
        ADD_RESTAURANT,
        ADD_REVIEW,
        ADD_USER
    }
    
    RestaurantDBClient client;
    RequestType type;
    Object input;
    
    public ClientRequestWrapper(Trigger trigger, RestaurantDBClient client, RequestType type, Object input) {
        super(trigger);
        this.client = client;
        this.type = type;
        this.input = input;
    }
    
    @Override
    public Object callTriggered() throws Exception {
        while (!client.isConnected()) {
            client.connect();
        }
        
        Object output = null;
        switch(type) {
        case ADD_RESTAURANT:
            output = client.addRestaurant((JSONObject)input);
            break;
        case ADD_REVIEW:
            output = client.addReview((JSONObject)input);
            break;
        case ADD_USER:
            output = client.addUser((JSONObject)input);
            break;
        case GET_RESTAURANT:
            output = client.getRestaurant((String)input);
            break;
        case RANDOM_REVIEW:
            output = client.getRandomReview((String)input);
            break;
        default:
            break;
        }
        
        client.disconnect();
        return output;
    }

    
    
}
