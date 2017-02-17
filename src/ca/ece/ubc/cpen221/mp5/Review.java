package ca.ece.ubc.cpen221.mp5;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Review {
	//all these fields are immutable, hence they are safe to be shared
	public final String business_id;
	public final String user_id;
	public final String review_id;
	public final long stars;
	public final String text;
	public final String date;
	public final Votes votes;
	public final String reviewDetails;
	
	public Review(String reviewDetails) {
		this.reviewDetails = reviewDetails;
		JSONParser parser = new JSONParser();
		Object obj;
        try {
            obj = parser.parse(reviewDetails);
        } catch (ParseException e) {
            obj = null;
            throw new RuntimeException();
        }
			
		JSONObject jsonObject = (JSONObject) obj;
			
		this.business_id = (String) jsonObject.get("business_id");
		this.user_id = (String) jsonObject.get("user_id");
		this.review_id = (String) jsonObject.get("review_id");
		this.stars = (long) (Long) jsonObject.get("stars");
		this.text = (String) jsonObject.get("text");
		this.date = (String) jsonObject.get("date");
			
		JSONObject v = (JSONObject) jsonObject.get("votes");
		this.votes = new Votes(
				(long) (Long) v.get("funny"), (long) (Long) v.get("useful"),(long) (Long) v.get("cool"));
	}
	
	/**
	 * Indicate if a given object is equal to this review. Two review are considered equal
	 * if they have the same businessID, userID, and reviewID.
	 * The conditions for equality are symmetry, transitivity and reflexivity.
	 * 
	 * @param other object to compare to this review
	 * @return true if other object is equal to this review, false otherwise
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Review)) {
			return false;
		}
		
		Review otherR = (Review) other;
		if (otherR.business_id.equals(this.business_id)
				&& otherR.user_id.equals(this.user_id)
				&& otherR.review_id.equals(this.review_id))
				return true;
		else return false;
	}
}
