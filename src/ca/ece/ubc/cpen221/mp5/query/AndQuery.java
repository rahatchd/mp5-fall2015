package ca.ece.ubc.cpen221.mp5.query;

import java.util.List;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class AndQuery implements Query {

    List<Query> queries;

    public AndQuery(List<Query> andExprs) {
        queries = andExprs;
    }
    
    @Override
    public Set<Restaurant> evaluate(RestaurantDB db) {
        Set<Restaurant> intersection = queries.get(0).evaluate(db);
        for (Query query : queries){
            Set<Restaurant> set = query.evaluate(db);
            intersection.retainAll(set);
        }
        return intersection;
    }

}
