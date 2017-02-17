package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;
import ca.ece.ubc.cpen221.mp5.Review;

public class RestaurantDBTest {
	private RestaurantDB testDB;
	private Restaurant testRes;
	private Restaurant testRes2;
	private String newResDetails;
	private String newUserDetails;
	private String newReviewDetails;
	
	private void init() {
		testDB = new RestaurantDB("data/restaurants.json", "data/reviews.json","data/users.json");
		testRes = new Restaurant("{\"open\": true, "
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
		testRes2 = new Restaurant("{\"open\": true, "
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
		
		newResDetails = new String("{\"open\": true, "
		        + "\"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", "
		        + "\"longitude\": -122.260408, "
		        + "\"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], "
		        + "\"business_id\": \"fake id\", "
		        + "\"name\": \"fake name\", "
		        + "\"categories\": [\"Cafes\", \"Restaurants\"], "
		        + "\"state\": \"CA\", "
		        + "\"type\": \"business\", "
		        + "\"stars\": 2.0, "
		        + "\"city\": \"Berkeley\", "
		        + "\"full_address\": \"2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701\", "
		        + "\"review_count\": 9, "
		        + "\"photo_url\":\"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", "
		        + "\"schools\": [\"University of California at Berkeley\"], "
		        + "\"latitude\": 37.867417, "
		        + "\"price\": 1}");
		
		newUserDetails = new String("{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, "
				+ "\"review_count\": 29, "
				+ "\"type\": \"user\", "
				+ "\"user_id\": \"fake id\", "
				+ "\"name\": \"fake name\", "
				+ "\"average_stars\": 3.89655172413793}");
		
		newReviewDetails = new String("{\"type\": \"review\", "
				+ "\"business_id\": \"fake id\", "
				+ "\"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, "
				+ "\"review_id\": \"fake id\", "
				+ "\"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.\", "
				+ "\"stars\": 2, "
				+ "\"user_id\": \"fake id\", "
				+ "\"date\": \"2006-07-26\"}");
	}

	// query() is tested in the main function in the RestaruantDBServer file

	@Test
	public void testRandomReview() {
		init();
		String revDetails = testDB.randomReview("Cafe 3");
		assertEquals(true, revDetails.contains("gclB3ED6uk6viWlolSb_uA"));
		
		String revDetails2 = testDB.randomReview("Peppermint Grill");
		assertEquals (true, revDetails2.contains("FWadSZw0G7HsgKXq7gHTnw"));
		
		assertEquals(new String(), testDB.randomReview("This restaurant does not exist"));
	}

	@Test
	public void testGetRestaurant() {
		init();
		String resDetails = testDB.getRestaurant("gclB3ED6uk6viWlolSb_uA");
		assertEquals(true, resDetails.contains("gclB3ED6uk6viWlolSb_uA"));
		
		String resDetails2 = testDB.getRestaurant("FWadSZw0G7HsgKXq7gHTnw");
		assertEquals(true, resDetails2.contains("FWadSZw0G7HsgKXq7gHTnw"));
		
		assertEquals(new String(), testDB.getRestaurant("This restaurant does not exist"));
	}

	@Test
	public void testAddRestaurant() {
		init();
		assertEquals(true, testDB.addRestaurant(newResDetails));
		String resDetails = testDB.getRestaurant("fake id");
		assertEquals(true, resDetails.contains("fake id"));
		assertEquals(false, testDB.addRestaurant(newResDetails));
	}

	@Test
	public void testAddUser() {
		init();
		assertEquals(true, testDB.addUser(newUserDetails));
		assertEquals(false, testDB.addUser(newUserDetails));
	}

	@Test
	public void testAddReview() {
		init();
		assertEquals(true, testDB.addReview(newReviewDetails));
		assertEquals(false, testDB.addReview(newReviewDetails));
	}

}
