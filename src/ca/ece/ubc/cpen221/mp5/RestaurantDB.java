package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.query.QueryFactory;

// TODO: This class represents the Restaurant Database.
// Define the internal representation and 
// state the rep invariant and the abstraction function.

public class RestaurantDB {
    
    /*
     * RepInvariant:
     * 		users, reviews, restaurants != null and do not contain duplicates
     * 		duplicates are assessed based on equality (the conditions for equality for each of
     * 		user, review, and restaurant can be found in their respective class files
     */
    
    /*
     * Abstraction Function:
     * 		data in the list of users, reviews, and restaurants represents a database
     */
    
    /*
     * Thread Safety Argument:
     * 		The class is thread safe because all mutations to the lists are atomic and we use a thread
     * safe data type (DBList) which we implemented.  For the thread safety argument of DBList, consult
     * the DBList class file.
     */
    
    private DBList<User> users;
    private DBList<Review> reviews;
    private DBList<Restaurant> restaurants;
        
	/**
	 * Create a database from the Yelp dataset given the names of three files:
	 * <ul>
	 * <li>One that contains data about the restaurants;</li>
	 * <li>One that contains reviews of the restaurants;</li>
	 * <li>One that contains information about the users that submitted reviews.
	 * </li>
	 * </ul>
	 * The files contain data in JSON format.
	 * 
	 * @param restaurantJSONfilename
	 *            the filename for the restaurant data
	 * @param reviewsJSONfilename
	 *            the filename for the reviews
	 * @param usersJSONfilename
	 *            the filename for the users
	 */
	public RestaurantDB(String restaurantJSONfilename, String reviewsJSONfilename, String usersJSONfilename) {
		// TODO: Implement this method (buffered reader in here parsing the files)
		users = new DBList<User>();
		reviews = new DBList<Review>();
		restaurants = new DBList<Restaurant>();
		
		try {
			
			// Read and store restaurant data
			FileReader restaurantFile = new FileReader(restaurantJSONfilename);
			BufferedReader restaurantBuffer = new BufferedReader(restaurantFile);
			Iterator<String> restaurantIterator = restaurantBuffer.lines().iterator();
			
			while (restaurantIterator.hasNext()) {
				this.restaurants.add(new Restaurant(restaurantIterator.next()));
			}
			
			restaurantFile.close();
			restaurantBuffer.close();
			
			//Read and store review data
			FileReader reviewFile = new FileReader(reviewsJSONfilename);
			BufferedReader reviewBuffer = new BufferedReader(reviewFile);
			Iterator<String> reviewIterator = reviewBuffer.lines().iterator();
			
			while (reviewIterator.hasNext()) {
				this.reviews.add(new Review(reviewIterator.next()));
			}
			
			reviewFile.close();
			reviewBuffer.close();
			
			//Read and store user data
			FileReader userFile = new FileReader(usersJSONfilename);
			BufferedReader userBuffer = new BufferedReader(userFile);
			Iterator<String> userIterator = userBuffer.lines().iterator();
			
			while (userIterator.hasNext()) {
				this.users.add(new User(userIterator.next()));				
			}
			
			userFile.close();
			userBuffer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a set of restaurants that match a given query string.
	 * 
	 * @param queryString the given query string
	 * @return the set of restaurants that match the query string
	 */
	public Set<Restaurant> query(String queryString) {
		return QueryFactory.parse(queryString).evaluate(this);
	}

	
	/**
	 * Given a restaurant name, returns the details of a random review of
	 * a matching restaurant in JSON format 
	 * 
	 * @param restaurantName the given restaurant name
	 * 			requires that restaurantName refers to at least one Restaurant in the database
	 * @return String that contains the details of a random review in
	 * 			of a restaurant that matches the restaurant name
	 */
	public String randomReview(String restaurantName) {
		Restaurant res = null;
	    boolean restaurantFound = false;
		Iterator<Restaurant> restaurantIter = restaurants.iterator();
	    while (restaurantIter.hasNext()) {
	    	res = restaurantIter.next();
	    	if (res.name.equals(restaurantName)) {
	    		restaurantFound = true;
	    		break;
	    	}
	    }
	    
	    if (restaurantFound) {
	    	Review rev;
	    	DBList<Review> possibleReviews = new DBList<Review>();
	    	Iterator<Review> reviewIter = reviews.iterator();
	    	while (reviewIter.hasNext()) {
	    		rev = reviewIter.next();
	    		if (rev.business_id.equals(res.business_id))
	    			possibleReviews.add(rev);
	    	}
	    	Random random = new Random();
	    	return new String(possibleReviews.get(random.nextInt(possibleReviews.size())).reviewDetails);
	    }
	    
        return new String(); //TODO
	}
	
	/**
	 * Given a businessID, returns the matching restaurant's details in JSON format
	 * 
	 * @param buisnessID  the given businessID
	 * 			requires that businessID refers to a Restaurant in the database
	 * @return String that contains the details of a restaurant that matches the given businessID
	 */
	public String getRestaurant(String businessID){
	    Restaurant res = null;
	    boolean restaurantFound = false;
		Iterator<Restaurant> restaurantIter = restaurants.iterator();
	    while (restaurantIter.hasNext()) {
	    	res = restaurantIter.next();
	    	if (res.business_id.equals(businessID)) {
	    		restaurantFound = true;
	    		break;
	    	}
	    }
	    
	    if (restaurantFound) {
	    	return new String(res.restaurantDetails);
	    }
	    
        return new String(); //TODO
	}
	
	/**
	 * Given details of a restaurant in JSON format, adds a new Restaurant object to the database
	 * iff the given details do not match those of an existing Restaurant already in the database.
	 * 
	 * @param restaurantDetails given details of a restaurant in JSON format
	 * @return true if given restaurantDetails did not match an existing restaurant and new Restaurant was
	 * 			successfully added, false if given restaurantDetails matched those of an existing Restaurant
	 */
	public boolean addRestaurant(String restaurantDetails){
	    synchronized(restaurants){
	    	Restaurant toAdd = new Restaurant(restaurantDetails);
	    	Iterator<Restaurant> it = restaurants.iterator();
	    	while (it.hasNext()) {
	    		Restaurant r = it.next();
	    		if (toAdd.equals(r))
	    			return false;
	    	}
	        this.restaurants.add(toAdd);
	        return true;
	    }
	}
	
	/**
	 * Given details of a User in JSON format, adds a new User object to the database
	 * iff the given details do not match those of an existing User already in the database.
	 * 
	 * @param userDetails given details of a user in JSON format
	 * @return true if given userDetails did not match an existing User and new User was
	 * 			successfully added, false if given userDetails matched those of an existing User
	 */
    public boolean addUser(String userDetails){
        synchronized(users){
            User toAdd = new User(userDetails);
        	Iterator<User> it = users.iterator();
        	while (it.hasNext()) {
        		User u = it.next();
        		if (toAdd.equals(u))
        			return false;
        	}
        	this.users.add(new User(userDetails));
        	return true;
        }
    }
	
    /**
	 * Given details of a Review in JSON format, adds a new Review object to the database
	 * iff the given details do not match those of an existing Review already in the database.
	 * 
	 * @param reviewDetails given details of a Review in JSON format
	 * @return true if given reviewDetails did not match an existing Review and new Review was
	 * 			successfully added, false if given reviewDetails matched those of an existing Review
	 */
    public boolean addReview(String reviewDetails){
        synchronized(reviews){
        	Review toAdd = new Review(reviewDetails);
        	Iterator<Review> it = reviews.iterator();
        	while (it.hasNext()) {
        		Review r = it.next();
        		if (toAdd.equals(r))
        			return false;
        	}
            this.reviews.add(new Review(reviewDetails));
            return true;
        }
    }
	
    /**
     * Returns a list of all the Restaurants in the database.
     * 
     * @return a list of all the Restaurants in the database
     */
    public List<Restaurant> restaurants() {
    	List<Restaurant> restaurantList = new LinkedList<Restaurant>();
    	
    	Iterator<Restaurant> it = restaurants.iterator();
    	while (it.hasNext()) {
    		restaurantList.add(new Restaurant(it.next().restaurantDetails));
    	}
    	return restaurantList;
    }
    
    /**
     * Returns a list of all the Reviews in the database.
     * 
     * @return a list of all the Reviews in the database
     */
    public List<Review> reviews() {
    	List<Review> reviewList = new LinkedList<Review>();
    	
    	Iterator<Review> it = reviews.iterator();
    	while (it.hasNext()) {
    		reviewList.add(new Review(it.next().reviewDetails));
    	}
    	return reviewList;
    }
    
    /**
     * Returns a list of all the Users in the database.
     * 
     * @return a list of all the Users in the database
     */
    public List<User> users() {
    	List<User> userList = new LinkedList<User>();
    	
    	Iterator<User> it = users.iterator();
    	while (it.hasNext()) {
    		userList.add(new User(it.next().userDetails));
    	}
    	return userList;
    }
}
