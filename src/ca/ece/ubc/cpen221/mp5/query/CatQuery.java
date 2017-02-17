package ca.ece.ubc.cpen221.mp5.query;

import java.util.HashSet;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class CatQuery implements Query {
    
    String category;
    
    public CatQuery(String string) {
        category = string;
    }

    @Override
    public Set<Restaurant> evaluate(RestaurantDB db) {
        Set<Restaurant> set = new HashSet<Restaurant>();
        
        for (Restaurant restaurant : db.restaurants()){
            if (restaurant.categories.contains(category)){
                set.add(restaurant);
            }
        }
        return set;
    }

}
