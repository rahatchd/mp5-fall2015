package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.User;

public class UserTest {
	private String testString;
	private String testString2;
	private User u;
	private User uEqual;
	private User uAlsoEqual;
	private User uNotEqual;
	
	private final double TOLERANCE = 0.00000000001;
	
	private void init() {
		testString = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, "
				+ "\"review_count\": 29, "
				+ "\"type\": \"user\", "
				+ "\"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"name\": \"Chris M.\", "
				+ "\"average_stars\": 3.89655172413793}";
		testString2 = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", "
				+ "\"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, "
				+ "\"review_count\": 29, "
				+ "\"type\": \"user\", "
				+ "\"user_id\": \"_NH7Cpq3qZkBYP5xR4gXog\", "
				+ "\"name\": \"Chris B.\", "
				+ "\"average_stars\": 3.89655172413793}";
		u = new User(testString);
		uEqual = new User(testString);
		uAlsoEqual = new User(testString);
		uNotEqual = new User(testString2);
	}

	@Test
	public void test_url() {
		init();
		assertEquals("http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog", u.url);
	}
	
	@Test
	public void test_votes() {
		init();
		assertEquals(35, u.votes.funny);
		assertEquals(21, u.votes.useful);
		assertEquals(14, u.votes.cool);
	}
	
	@Test
	public void test_review_count() {
		init();
		assertEquals(29, u.review_count);
	}
	
	@Test
	public void test_user_id() {
		init();
		assertEquals("_NH7Cpq3qZkByP5xR4gXog", u.user_id);
	}
	
	@Test
	public void test_name() {
		init();
		assertEquals("Chris M.", u.name);
	}
	
	@Test
	public void test_average_stars() {
		init();
		assertEquals(3.89655172413793, u.average_stars, TOLERANCE);
	}
	
	@Test
	public void testEquals() {
		init();
		assertEquals(true, u.equals(u));
		assertEquals(true, u.equals(uEqual));
		assertEquals(true, uEqual.equals(u));
		assertEquals(true, uEqual.equals(uAlsoEqual));
		assertEquals(false, u.equals(uNotEqual));
		assertEquals(false, uNotEqual.equals(u));
	}
}
