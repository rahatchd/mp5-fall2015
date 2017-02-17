package grading.staff;

import java.util.List;

import org.json.simple.JSONObject;

public class TriggeredQuery extends TriggeredCallable<List<JSONObject>> {

    RestaurantDBClient client;
    String queryStr;
    
    public TriggeredQuery(Trigger trigger, RestaurantDBClient client, String queryStr) {
        super(trigger);
        this.client = client;
        this.queryStr = queryStr;
    }
    
    @Override
    public List<JSONObject> callTriggered() throws Exception {
        while (!client.isConnected()) {
            client.connect();
            Thread.yield();
        }
        List<JSONObject> output = client.query(queryStr);
        client.disconnect();
        return output;
    }

}
