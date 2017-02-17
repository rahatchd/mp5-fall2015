package ca.ece.ubc.cpen221.mp5.statlearning;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class Predictor implements MP5Function {
	private double m;
	private double b;
	private double R_squared;
	private MP5Function featureFunction;
	
	/**
	 * Constructs a predictor given the values of a linear regression function and a feature function.
	 * The predictor predicts the rating that a user is likely to give to a restaurant based on a feature
	 * of the restaurant.  Here, user rating is the vertical (y) axis, and restaurant feature is the
	 * horizontal (x) axis.
	 *  
	 * @param m slope of the linear regression graph; computed using [meanRating - b * meanFeature]
	 * @param b y-intercept of the linear regression graph; computed using [S_{xy} / S_{xx}]
	 * @param R_squared a measure of the quality of the linear regression
	 * @param featureFunction the given feature function;
	 * can be one of: <ul>
	 * <li>PriceScale</li>
	 * <li>MeanRating</li>
	 * <li>Latitude</li>
	 * <li>Longitude</li>
	 * <li>Category</li> </ul>
	 */
	public Predictor(double m, double b , double R_squared, MP5Function featureFunction) {
		this.m = m;
		this.b = b;
		this.R_squared = R_squared;
		this.featureFunction = featureFunction;
	}

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		return m * featureFunction.f(yelpRestaurant, db) + b;
	}
	
	public double getR_squared() {
		return R_squared;
	}
}
