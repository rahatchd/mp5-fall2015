package ca.ece.ubc.cpen221.mp5.query;

import java.util.HashSet;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class NameQuery implements Query {

    private String name;
    
    public NameQuery(String string) {
        name = string;
    }

    @Override
    public Set<Restaurant> evaluate(RestaurantDB db) {
        Set<Restaurant> set = new HashSet<Restaurant>();
        
        for (Restaurant restaurant : db.restaurants()){
            if (restaurant.name.equals(name)){
                set.add(restaurant);
            }
        }
        return set;

    }

}
