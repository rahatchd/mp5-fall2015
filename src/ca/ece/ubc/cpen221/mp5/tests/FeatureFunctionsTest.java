package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;
import ca.ece.ubc.cpen221.mp5.statlearning.*;

public class FeatureFunctionsTest {
	private RestaurantDB db;
	private Restaurant yelpRestaurant;
	private Restaurant yelpRestaurant2;
	private static final double TOLERANCE = 0.0000001;
	
	public void init() {
		db = new RestaurantDB("data/restaurants.json", "data/reviews.json","data/users.json");
		yelpRestaurant = new Restaurant("{\"open\": true, "
		        + "\"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", "
		        + "\"longitude\": -122.260408, "
		        + "\"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], "
		        + "\"business_id\": \"gclB3ED6uk6viWlolSb_uA\", "
		        + "\"name\": \"Cafe 3\", "
		        + "\"categories\": [\"Cafes\", \"Restaurants\"], "
		        + "\"state\": \"CA\", "
		        + "\"type\": \"business\", "
		        + "\"stars\": 2.0, "
		        + "\"city\": \"Berkeley\", "
		        + "\"full_address\": \"2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701\", "
		        + "\"review_count\": 9, "
		        + "\"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", "
		        + "\"schools\": [\"University of California at Berkeley\"], "
		        + "\"latitude\": 37.867417, "
		        + "\"price\": 1}");
		yelpRestaurant2 = new Restaurant("{\"open\": true, "
				+ "\"url\": \"http://www.yelp.com/biz/peppermint-grill-berkeley\", "
				+ "\"longitude\": -122.2598181, "
				+ "\"neighborhoods\": [\"UC Campus Area\"], "
				+ "\"business_id\": \"FWadSZw0G7HsgKXq7gHTnw\", "
				+ "\"name\": \"Peppermint Grill\", "
				+ "\"categories\": [\"American (Traditional)\", \"Restaurants\"], "
				+ "\"state\": \"CA\", "
				+ "\"type\": \"business\", "
				+ "\"stars\": 2.5, "
				+ "\"city\": \"Berkeley\", "
				+ "\"full_address\": \"2505 Hearst Ave\nSte B\nUC Campus Area\nBerkeley, CA 94709\", "
				+ "\"review_count\": 16, "
				+ "\"photo_url\": \"http://s3-media1.ak.yelpcdn.com/assets/2/www/img/924a6444ca6c/gfx/blank_biz_medium.gif\", "
				+ "\"schools\": [\"University of California at Berkeley\"], "
				+ "\"latitude\": 37.8751965, "
				+ "\"price\": 2}");
	}
	
	@Test
	public void testPriceScale() {
		init();
		MP5Function priceScale = new PriceScale();
		assertEquals(1.0, priceScale.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(2.0, priceScale.f(yelpRestaurant2, db), TOLERANCE);
		}
	
	@Test
	public void testMeanRating() {
		init();
		MP5Function meanRating = new MeanRating();
		assertEquals(2.0, meanRating.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(2.5, meanRating.f(yelpRestaurant2, db), TOLERANCE);
	}
	
	@Test
	public void testLatitude() {
		init();
		MP5Function latitude = new Latitude();
		assertEquals(37.867417, latitude.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(37.8751965, latitude.f(yelpRestaurant2, db), TOLERANCE);
	}
	
	@Test
	public void testLongitude() {
		init();
		MP5Function longitude = new Longitude();
		assertEquals(-122.260408, longitude.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(-122.2598181, longitude.f(yelpRestaurant2, db), TOLERANCE);
	}
	
	@Test
	public void testCategory() {
		init();
		MP5Function category = new Category();
		assertEquals(2.0, category.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(2.0, category.f(yelpRestaurant2, db), TOLERANCE);
		}
	
	@Test
	public void testPredictor() {
		init();
		fail("Not yet implemnted");
	}
}
