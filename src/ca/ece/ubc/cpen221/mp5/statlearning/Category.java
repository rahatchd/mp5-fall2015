package ca.ece.ubc.cpen221.mp5.statlearning;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class Category implements MP5Function{
	
	// default constructor is used since object is not required, only f() is
	
	/**
	 * Maps the category of a given restaurant in a given database to a number and returns the number.
	 * 
	 * @param yelpRestaurant the given restaurant
	 * @param db the given database 
	 * @return number mapping of the category of the given restaurant 
	 */
	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		// We decided that the most useful information we could get from mapping category to 
		// a number is how diverse a given restaurant is (how many categories it falls under).
		return (double) yelpRestaurant.categories.size();
	}

}
