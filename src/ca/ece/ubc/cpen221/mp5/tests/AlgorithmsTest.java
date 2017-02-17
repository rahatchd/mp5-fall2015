package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.*;
import ca.ece.ubc.cpen221.mp5.statlearning.*;

public class AlgorithmsTest {
	// We only test the linear regression methods here, since kMeans can be tested via the voronoi.html
	// file.  Run VisualizeMain to see the output of the kMeans clustering algorithm.
	// **Note : Setting up the database takes some time so please be patient. **
	
	private static final double TOLERANCE = 0.00000001;
	private static RestaurantDB db;
	private static Restaurant yelpRestaurant;
	
	
	@BeforeClass
	public static void init() {
		db = new RestaurantDB("data/restaurants.json", "data/reviews.json","data/users.json");
		yelpRestaurant = db.restaurants().get(0);
	}

	@Test
	public void testGetPredictor() {
		User u = db.users().get(64);
		User u2 = db.users().get(86);
		User u3 = db.users().get(106);
		User u4 = db.users().get(51);
				
		Predictor predictor = Algorithms.getPredictor(u, db, new MeanRating());
		Predictor predictor2 = Algorithms.getPredictor(u2, db, new MeanRating());
		Predictor predictor3 = Algorithms.getPredictor(u3, db, new MeanRating());
		Predictor predictor4 = Algorithms.getPredictor(u4, db, new MeanRating());
		
		assertEquals(5.0, predictor.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(2.5, predictor2.f(yelpRestaurant, db), TOLERANCE);		
		assertEquals(5.1666666666666, predictor3.f(yelpRestaurant, db), TOLERANCE);
		assertEquals(1.0, predictor4.f(yelpRestaurant, db), TOLERANCE);
	}

	@Test
	public void testGetBestPredictor() {
		User u = db.users().get(106);
		List<MP5Function> featureFunctionList = new LinkedList<MP5Function>();
		
		featureFunctionList.add(new MeanRating());
		featureFunctionList.add(new PriceScale());
		featureFunctionList.add(new Latitude());
		featureFunctionList.add(new Longitude());
		
		Predictor bestPredictor = Algorithms.getBestPredictor(u, db, featureFunctionList);
		assertEquals(0.998944440451671, bestPredictor.getR_squared(), TOLERANCE);
	}

}
