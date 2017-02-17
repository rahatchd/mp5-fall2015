package ca.ece.ubc.cpen221.mp5.query;

import ca.ece.ubc.cpen221.mp5.RestaurantDB;

import java.util.Set;

import ca.ece.ubc.cpen221.mp5.Restaurant;

public interface Query {
    /**
     * The evaluate method evaluates a query/request
     * @param DB the restaurant dataBase
     * @return the string that the server is supposed to return in response to the query,
     * or the empty string if nothing needs to be returned. 
     */
    public Set<Restaurant> evaluate(RestaurantDB db);
}
