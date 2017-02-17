package ca.ece.ubc.cpen221.mp5;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class User {
	//all these fields are immutable, hence they are safe to be shared
    public final String user_id;
    public final String name;
    public final long review_count;
    public final double average_stars;
    public final Votes votes;
	public final String url;
	public final String userDetails;
	
	public User(String userDetails) {
		this.userDetails = userDetails;
		JSONParser parser = new JSONParser();
		Object obj;
		try {
		    obj = parser.parse(userDetails);
        }
       catch(Exception e) {
           obj = null;
           throw new RuntimeException();
       }
			
		JSONObject jsonObject = (JSONObject) obj;
			
		this.user_id = (String) jsonObject.get("user_id");
		this.name = (String) jsonObject.get("name");
		this.review_count = (long) (Long) jsonObject.get("review_count");
		this.average_stars = (double) (Double) jsonObject.get("average_stars");
		
		JSONObject v = (JSONObject) jsonObject.get("votes");
		this.votes = new Votes(
				(long) (Long) v.get("funny"), (long) (Long) v.get("useful"), (long) (Long) v.get("cool"));
		
		this.url = (String) jsonObject.get("url");
	}
	
	/**
	 * Indicate if a given object is equal to this user. Two users are considered equal
	 * if they have the same userID, and name.
	 * The conditions for equality are symmetry, transitivity and reflexivity.
	 * 
	 * @param other object to compare to this user
	 * @return true if other object is equal to this user, false otherwise
	 */
	public boolean equals(Object other) {
		if (!(other instanceof User)) {
			return false;
		}
		
		User otherU = (User) other;
		if (otherU.user_id.equals(this.user_id)
				&& otherU.name.equals(this.name))
				return true;
		else return false;
	}
}