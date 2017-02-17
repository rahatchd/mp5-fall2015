package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Review;

public class ReviewTest {
	private String testString;
	private String testString2;
	private Review r;
	private Review rEqual;
	private Review rAlsoEqual;
	private Review rNotEqual;
	
	private void init() {
		testString = "{\"type\": \"review\", "
				+ "\"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", "
				+ "\"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, "
				+ "\"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", "
				+ "\"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.\", "
				+ "\"stars\": 2, "
				+ "\"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", "
				+ "\"date\": \"2006-07-26\"}";
		testString2 = "{\"type\": \"review\", "
				+ "\"business_id\": \"1CBS84C-a-cuA3vncXVSAw\", "
				+ "\"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, "
				+ "\"review_id\": \"0a-pCW4guXIlcNpVeBHChg\", "
				+ "\"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.\", "
				+ "\"stars\": 2, "
				+ "\"user_id\": \"90wm_01FAIqhcgV-mPON9Q\", "
				+ "\"date\": \"2006-07-26\"}";
		r = new Review(testString);
		rEqual = new Review(testString);
		rAlsoEqual = new Review(testString);
		rNotEqual = new Review(testString2);
	}
	
	@Test
	public void test_business_id() {
		init();
		assertEquals("1CBs84C-a-cuA3vncXVSAw", r.business_id);
	}
	
	@Test
	public void test_votes() {
		init();
		assertEquals(0, r.votes.cool);
		assertEquals(0, r.votes.useful);
		assertEquals(0, r.votes.funny);
	}
	
	@Test
	public void test_review_id() {
		init();
		assertEquals("0a-pCW4guXIlWNpVeBHChg", r.review_id);
	}
	
	@Test
	public void test_text() {
		init();
		assertEquals("The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\n\nOh, and the pasta is even worse than the pizza.",
				r.text);
	}
	
	@Test
	public void test_stars() {
		init();
		assertEquals(2, r.stars);
	}
	
	@Test
	public void test_user_id() {
		init();
		assertEquals("90wm_01FAIqhcgV_mPON9Q", r.user_id);
	}
	
	@Test
	public void test_date() {
		init();
		assertEquals("2006-07-26", r.date);
	}
	
	@Test
	public void testEquals(){
		init();
		assertEquals(true, r.equals(r));
		assertEquals(true, r.equals(rEqual));
		assertEquals(true, rEqual.equals(r));
		assertEquals(true, rEqual.equals(rAlsoEqual));
		assertEquals(false, r.equals(rNotEqual));
		assertEquals(false, rNotEqual.equals(r));
	}

}
