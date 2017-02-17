package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;
import ca.ece.ubc.cpen221.mp5.query.Query;
import ca.ece.ubc.cpen221.mp5.query.QueryFactory;

public class QueryFactoryTest {
    
    QueryFactory qF;
    RestaurantDB db;
    
    private void init(){
        qF = new QueryFactory();
        db = new RestaurantDB("data/restaurants.json", "data/reviews.json","data/users.json");;
    }
    
    @Test
    public void test() {
        init();
        String testString = "in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(1..2)";
        Query q = QueryFactory.parse(testString);
        Set<Restaurant> queryResponses = q.evaluate(db);
        
        for (Restaurant r: queryResponses) {
            assertEquals(true, r.neighborhoods.contains("Telegraph Ave"));
            assertEquals(true, r.categories.contains("Chinese") || r.categories.contains("Italian"));
            assertEquals(true, r.price >= 1 && r.price <=2);
        }
        
    }

}
