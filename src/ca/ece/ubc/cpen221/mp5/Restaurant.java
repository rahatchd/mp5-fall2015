package ca.ece.ubc.cpen221.mp5;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Restaurant {
	/*
	 * Rep invariant:
	 * 		- none of the fields (ex business_id, name, city etc.) are null
	 */
    
    /*
     * Abstraction Function:
     *      - maps the details given in the restaurantDetails string to fields, where the fields
     *      collectively represent a restaurant
     */
	
	//all these fields are immutable, hence they are safe to be shared
	public final String business_id;
	public final String name;
	public final List<String> neighborhoods;
	public final String full_address;
	public final String city;
	public final String state;
	public final double latitude;
	public final double longitude;
	public final double stars;
	public final long review_count;
	public final String photo_url;
	public final List<String> categories;
	public final boolean open;
	public final List<String> schools;
	public final String url;
	public final long price;
	public final String restaurantDetails;
	
	private static final double TOLERANCE = 0.0000001;
	
	/**
	 * Constructs a Restaurant object as per the specifications in the given restaurantDetails String.
	 * 
	 * @param restaurantDetails String containing the details of the restaurant
	 */
	public Restaurant(String restaurantDetails) {
		this.restaurantDetails = restaurantDetails;
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(restaurantDetails);
		} catch (ParseException e) {
            obj = null;
            throw new RuntimeException();
        }
			
			JSONObject jsonObject = (JSONObject) obj;
			
			this.business_id = (String) jsonObject.get("business_id");
			this.name = (String) jsonObject.get("name");
			
			JSONArray n = (JSONArray) jsonObject.get("neighborhoods");
			List<String> neighborhoods = new LinkedList<String>();
			for (Object neighborhood: n)
				neighborhoods.add(neighborhood.toString());
			this.neighborhoods = Collections.unmodifiableList(neighborhoods);
			
			this.full_address = (String) jsonObject.get("full_address");
			this.city = (String) jsonObject.get("city");
			this.state = (String) jsonObject.get("state");
			this.latitude = (double) (Double) jsonObject.get("latitude");
			this.longitude = (double) (Double) jsonObject.get("longitude");
			this.stars = (double) (Double) jsonObject.get("stars");
			this.review_count = (long) (Long) jsonObject.get("review_count");
			this.photo_url = (String) jsonObject.get("photo_url");
			
			JSONArray c = (JSONArray) jsonObject.get("categories");
			List<String> categories = new LinkedList<String>();
			for (Object category: c)
				categories.add(category.toString());
			this.categories = Collections.unmodifiableList(categories);
			
			this.open = (boolean) jsonObject.get("open");
			
			JSONArray s = (JSONArray) jsonObject.get("schools");
			List<String> schools = new LinkedList<String>();
			for (Object school: s)
				schools.add(school.toString());
			this.schools = Collections.unmodifiableList(schools);
			
			this.url = (String) jsonObject.get("url");
			this.price = (long) (Long) jsonObject.get("price");
	}
	
	/**
	 * Indicate if a given object is equal to this restaurant. Two restaurants are considered equal
	 * if they have the same name, businessID, and are in the same location (latitude and longitude).
	 * The conditions for equality are symmetry and reflexivity.  This method is not entirely
	 * transitive, since floating point equality is only approximate.
	 * 
	 * @param other object to compare to this
	 * @return true if other object is equal to this restaurant, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Restaurant)) {
			return false;
		}
		
		Restaurant otherR = (Restaurant) other;
		if (otherR.business_id.equals(this.business_id)
				&& (Math.abs(otherR.latitude - this.latitude)) < TOLERANCE
				&& (Math.abs(otherR.longitude - this.longitude)) < TOLERANCE
				&& otherR.name.equals(this.name))
				return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
        return this.business_id.hashCode() + this.name.hashCode();
	}
}
