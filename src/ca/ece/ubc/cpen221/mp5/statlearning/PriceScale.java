package ca.ece.ubc.cpen221.mp5.statlearning;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class PriceScale implements MP5Function {
	
	// default constructor is used since object is not required, only f() is
	
	/**
	 * Computes the price scale of a given restaurant in a given database.
	 * 
	 * @param yelpRestaurant the given restaurant
	 * @param db the given database
	 * @return the price scale of the given restaurant
	 */
	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		return (double) yelpRestaurant.price;
	}

}
