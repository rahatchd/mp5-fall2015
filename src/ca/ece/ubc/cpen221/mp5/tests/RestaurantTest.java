package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;

public class RestaurantTest {
    private String testString;
    private String testString2;
    private Restaurant r;
    private Restaurant rEqual;
    private Restaurant rNotEqual;
    
    private final double TOLERANCE = 0.00001;
    
	public void init() {
		testString = "{\"open\": true, "
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
		        + "\"price\": 1}";
		testString2 = "{\"open\": true, "
		        + "\"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", "
		        + "\"longitude\": -122.260808, "
		        + "\"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], "
		        + "\"business_id\": \"gclB3ED6u16viWlolSb_uA\", "
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
		        + "\"latitude\": 37.862417, "
		        + "\"price\": 1}";
		r = new Restaurant(testString);
		rEqual = new Restaurant(testString);
		rNotEqual = new Restaurant(testString2);
	}
	
	@Test
	public void test_business_id() {
	    init();
	    assertEquals("gclB3ED6uk6viWlolSb_uA", r.business_id);
	    
	}
	
	@Test
    public void test_name() {
        init();
        assertEquals("Cafe 3", r.name);
    }
	
	@Test
    public void test_neighborhoods() {
        init();
        List<String> expected = new LinkedList<String>();
        expected.add("Telegraph Ave");
        expected.add("UC Campus Area");
        
        assertEquals(expected, r.neighborhoods);
    }
	
	@Test
    public void test_full_address() {
        init();
        assertEquals("2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701", r.full_address);
    }
	
	@Test
    public void test_city() {
        init();
        assertEquals("Berkeley", r.city);
    }
	
	@Test
    public void test_state() {
        init();
        assertEquals("CA", r.state);
    }

	@Test
    public void test_latitude() {
        init();
        assertEquals(37.867417, r.latitude, TOLERANCE);
    }
	
	@Test
    public void test_longitude() {
        init();
        assertEquals(-122.260408, r.longitude, TOLERANCE);
    }
	
	@Test
    public void test_stars() {
        init();
        assertEquals(2.0, r.stars, TOLERANCE);
    }
	
	@Test
    public void test_review_count() {
        init();
        assertEquals(9, r.review_count);
    }
	
	@Test
    public void test_photo_url() {
        init();
        assertEquals("http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg", r.photo_url);
    }
	
	@Test
    public void test_categories() {
        init();
        List<String> expected = new LinkedList<String>();
        expected.add("Cafes");
        expected.add("Restaurants");
        
        assertEquals(expected, r.categories);
    }
	
	@Test
    public void test_open() {
        init();
        assertEquals(true, r.open);
    }
	
	@Test
    public void test_schools() {
        init();
        List<String> expected = new LinkedList<String>();
        expected.add("University of California at Berkeley");
        
        assertEquals(expected, r.schools);
    }
	
	@Test
    public void test_url() {
        init();
        assertEquals("http://www.yelp.com/biz/cafe-3-berkeley", r.url);
    }
	
	@Test
    public void test_price() {
        init();
        assertEquals(1, r.price);
    }
	
	@Test
	public void testEquals() {
		init();
		assertEquals(true, r.equals(r));
		assertEquals(true, r.equals(rEqual));
		assertEquals(true, rEqual.equals(r));
		assertEquals(false, r.equals(rNotEqual));
		assertEquals(false, rNotEqual.equals(r));
	}
}
