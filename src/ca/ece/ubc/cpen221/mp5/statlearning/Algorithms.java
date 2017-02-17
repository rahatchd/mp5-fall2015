package ca.ece.ubc.cpen221.mp5.statlearning;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ca.ece.ubc.cpen221.mp5.*;

public class Algorithms {
	private static final int LONG = 0;
	private static final int LAT = 1;
	private static final double TOLERANCE = 0.0000000001;

	/**
	 * Use k-means clustering to compute k clusters for the restaurants in the
	 * database.
	 * 
	 * @param k
	 *            number of clusters
	 * @param db
	 *            the database
	 * @return List of set of restaurants, where each set represents all the
	 *         restaurants in a cluster
	 */
	public static List<Set<Restaurant>> kMeansClustering(int k, RestaurantDB db) {
		List<Set<Restaurant>> clusters = new ArrayList<Set<Restaurant>>();
		List<double[]> centroids = new ArrayList<double[]>();

		Random randomInt = new Random(69);
		// pick initial centroids at random restaurant locations
		for (int ii = 0; ii < k; ++ii) {
			int randomRestaurant = randomInt.nextInt(db.restaurants().size());
			double[] centroid = { db.restaurants().get(randomRestaurant).longitude,
					db.restaurants().get(randomRestaurant).latitude };
			centroids.add(centroid);
			clusters.add(new HashSet<Restaurant>());
		}

		// group restaurants into clusters based on the random centroids
		clusters = groupIntoClusters(centroids, db);

		// while the centroids are changing, keep computing new centroids and
		// rearranging clusters
		List<double[]> oldCentroids = centroids;
		while (!equalClusters(oldCentroids, computeNewCentroids(clusters, oldCentroids))) {
			// Update centroids until they no longer change
			oldCentroids = centroids;
			centroids = computeNewCentroids(clusters, oldCentroids);
			clusters = groupIntoClusters(centroids, db);
		}

		return new LinkedList<Set<Restaurant>>(clusters);
	}

	private static boolean equalClusters(List<double[]> centroids, List<double[]> otherCentroids) {
		for (int i = 0; i < centroids.size(); i++) {
			boolean b1 = Math.abs(centroids.get(i)[LONG] - otherCentroids.get(i)[LONG]) > TOLERANCE;
			boolean b2 = Math.abs(centroids.get(i)[LAT] - otherCentroids.get(i)[LAT]) > TOLERANCE;
			if (b1)
				return false;
			if (b2)
				return false;
		}
		return true;

	}

	/**
	 * Groups all the restaurants in a given database into clusters by mapping
	 * each restaurant to the closest centroid from a list of possible
	 * centroids.
	 * 
	 * @param centroids
	 *            list of possible centroids
	 * @param db
	 *            the given database
	 * @return a list of sets of restaurants, such that each set contains all
	 *         restaurants that are closest to the associated centroid
	 */
	private static List<Set<Restaurant>> groupIntoClusters(List<double[]> centroids, RestaurantDB db) {
		List<Set<Restaurant>> clusters = new ArrayList<Set<Restaurant>>();

		// initialize clusters with centroids
		for (int ii = 0; ii < centroids.size(); ++ii) {
			clusters.add(new HashSet<Restaurant>());
		}

		for (Restaurant restaurant : db.restaurants()) {
			// Distance of the restaurants to each centroid
			double[] distance = new double[centroids.size()];

			// Compute squared Euclidean distance form the restaurant to each
			// point
			for (int ii = 0; ii < distance.length; ++ii) {
				distance[ii] = ((centroids.get(ii)[LONG] - restaurant.longitude)
						* (centroids.get(ii)[LONG] - restaurant.longitude))
						+ ((centroids.get(ii)[LAT] - restaurant.latitude)
								* (centroids.get(ii)[LAT] - restaurant.latitude));
			}

			// find the closest centroid
			double minSoFar = distance[0];
			int closestCentroid = 0;
			for (int jj = 0; jj < distance.length; ++jj) {
				if (minSoFar > distance[jj]) {
					minSoFar = distance[jj];
					closestCentroid = jj;
				}
			}

			// add restaurant to the cluster corresponding to the closest
			// centroid
			clusters.get(closestCentroid).add(restaurant);
		}

		return clusters;
	}

	/**
	 * Given a cluster of restaurants, computes new a new centroid for each
	 * cluster.
	 * 
	 * @param clusters
	 *            given cluster of restaurants
	 * @return list of centroids, where each centroid corresponds to a cluster
	 */
	private static List<double[]> computeNewCentroids(List<Set<Restaurant>> clusters, List<double[]> oldCentroids) {
		List<double[]> centroids = new ArrayList<double[]>();

		for (int ii = 0; ii < clusters.size(); ++ii) {
			Set<Restaurant> cluster = clusters.get(ii);
			if (cluster.size() == 0) {
				centroids.add(oldCentroids.get(ii));
			}

			else {
				double meanLat = 0, meanLong = 0;
				for (Restaurant restaurant : cluster) {
					meanLat += restaurant.latitude;
					meanLong += restaurant.longitude;
				}

				meanLat /= cluster.size();
				meanLong /= cluster.size();
				double[] newCentroid = { meanLong, meanLat };
				centroids.add(newCentroid);
			}
		}

		return centroids;
	}

	/**
	 * Converts a given List of clusters, where each cluster is a set of
	 * restaurants, into a JSON formatted string representation of the list.
	 * 
	 * @param clusters
	 * @return the JSON formatted string representation of the list
	 */
	public static String convertClustersToJSON(List<Set<Restaurant>> clusters) {
		StringBuffer JSONformatClusters = new StringBuffer();

		JSONformatClusters.append("[");
		for (Set<Restaurant> cluster : clusters) {
			// if the cluster is empty, omit it from the output string
			if (cluster.size() == 0)
				continue;

			// find centroid and weight
			double x = 0, y = 0;
			double weight = 0;
			for (Restaurant restaurant : cluster) {
				x += restaurant.latitude;
				y += restaurant.longitude;
				weight += (double) restaurant.stars;
			}
			x /= cluster.size();
			y /= cluster.size();
			weight /= cluster.size();
			
			// round the weights
			if (weight % (int) weight < 0.25)
				weight = (int) weight;
			else if (weight % (int) weight >= 0.25 && weight % (int) weight <= 0.75)
				weight = (int) weight + 0.5;
			else
				weight = (int) weight + 1;

			// find restaurant closest to centroid
			double minSoFar = Integer.MAX_VALUE;
			Restaurant closestToCentroid = null;
			for (Restaurant restaurant : cluster) {
				double distance = Math.sqrt((y - restaurant.longitude) * (y - restaurant.longitude)
						+ (x - restaurant.latitude) * (x - restaurant.latitude));
				if (minSoFar > distance) {
					minSoFar = distance;
					closestToCentroid = restaurant;
				}
			}


			// finally, construct the string for this cluster
			JSONformatClusters.append("{\"x\": " + x + ", \"y\": " + y + ", \"name\": \"" + closestToCentroid.name
					+ "\", " + "\"cluster\": " + cluster.size() + ", \"weight\": " + weight + "},");
		}
		JSONformatClusters.setCharAt(JSONformatClusters.length() - 1, ']');
		return new String(JSONformatClusters);
	}

	/**
	 * Produces a predictor function that predicts a given user's rating of any restaurant in
	 * a given RestaurantDB, based on a particular feature of the restaurants specified by a given feature
	 * function.  The predictor function is linear with respect to the given feature function.
	 * 
	 * @param u the given user
	 * @param db the given restaurant database
	 * @param featureFunction an MP5Function that returns a specific feature of the restaurants in the database
	 * @return an MP5Function that predicts the given user's reviews of a restaurant based on
	 * 			the specified feature of the restaurant
	 */
	public static Predictor getPredictor(User u, RestaurantDB db, MP5Function featureFunction) {
		double m = 0, b = 0, meanRating = 0, meanFeature = 0, S_xx = 0, S_yy = 0, S_xy = 0, R_squared = 0;
		
		List<Double> userRatings = new LinkedList<Double>();
		List<Double> restaurantFeatures = new LinkedList<Double>();
		List<Review> reviews = db.reviews();

		for (Review review: reviews) {
			if (review.user_id.equals(u.user_id)) {
				userRatings.add((double) review.stars);
				meanRating += (double) review.stars;
				
				double restaurantFeature = featureFunction.f
						(new Restaurant(db.getRestaurant(review.business_id)), db);
				restaurantFeatures.add(restaurantFeature);
				meanFeature += restaurantFeature;
			}
		}
		
		// Since the database is decorated, user's review count is not the same as the actual number of
		// reviews in the database.  In the case that the actual number of reviews of a user stored in
		// the database is less than 2, we cannot perform linear regression - so we return values that
		// make some sense, though this is not desirable.
		if (userRatings.size() == 1) {
			m = 0;
			b = meanRating;
			// Mathematically, the linear regression function accounts for all variants.
			// There is no variance.  (This is not desirable however).
			R_squared = 1;
			return new Predictor(m, b, R_squared, featureFunction);
		}
		
		else {
			meanRating /= userRatings.size();
			meanFeature /= restaurantFeatures.size();
			for (int ii = 0; ii < restaurantFeatures.size(); ++ii) {
				S_xx += (restaurantFeatures.get(ii) - meanFeature) * (restaurantFeatures.get(ii) - meanFeature);
				S_yy += (userRatings.get(ii) - meanRating) * (userRatings.get(ii) - meanRating);
				S_xy += (restaurantFeatures.get(ii) - meanFeature) * (userRatings.get(ii) - meanRating); 
			}
			
			// If the feature values do not vary, then the linear regression graph we get is
			// a vertical line (varying user ratings, but just one restaurant feature).
			// The best prediction (at least in this mp) we can give is the average of the user's ratings.
			if (S_xx == 0) {
				b = meanRating;
				m = 0;
				// This is not a good prediction.
				R_squared = 0;
			}
			else {
				m = S_xy / S_xx;
				b = meanRating - m * meanFeature;
				R_squared = (S_xy * S_xy) / (S_xx * S_yy);
			}
		}
		
		return new Predictor(m, b, R_squared, featureFunction);
	}

	/**
	 * Finds the best predictor (the predictor with highest R^2 value) in a
	 * given list of predictor functions. The R^2 value is with respect to a
	 * given user's reviews of restaurants.
	 * 
	 * @param u
	 *            the given user
	 * @param db
	 *            the given restaurant database
	 * @param featureFunctionList
	 *            the list of MP5Functions
	 * @return the the best MP5Function based on R^2 value
	 */
	public static Predictor getBestPredictor(User u, RestaurantDB db, List<MP5Function> featureFunctionList) {
		List<Predictor> predictorList = new LinkedList<Predictor>();
		for (MP5Function featureFunction : featureFunctionList) {
			predictorList.add(getPredictor(u, db, featureFunction));
		}

		Predictor bestPredictor = predictorList.get(0);
		double maxSoFar = bestPredictor.getR_squared();

		for (Predictor predictor : predictorList) {
			Predictor p = predictor;
			if (maxSoFar < p.getR_squared()) {
				maxSoFar = p.getR_squared();
				bestPredictor = p;
			}
		}
		return bestPredictor;
	}
}