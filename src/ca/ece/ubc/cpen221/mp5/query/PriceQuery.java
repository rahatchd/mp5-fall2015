package ca.ece.ubc.cpen221.mp5.query;

import java.util.HashSet;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class PriceQuery implements Query {

    int low, high;
    
    public PriceQuery(int low, int high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public Set<Restaurant> evaluate(RestaurantDB db) {
        Set<Restaurant> set = new HashSet<Restaurant>();
        
        for (Restaurant restaurant : db.restaurants()){
            if (restaurant.price >= low && restaurant.price <= high){
                set.add(restaurant);
            }
        }
        return set;
    }

}
