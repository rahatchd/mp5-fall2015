package ca.ece.ubc.cpen221.mp5.statlearning;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.*;

/**
 * Run this main to visualize the kMeans clustering algorithm.
 */
public class VisualizeMain {
	
	public static void main(String[] args) {
		// initialize database
		System.out.println("Initializing database...");
		RestaurantDB db = new RestaurantDB("data/restaurants.json", "data/reviews.json","data/users.json");
		
		System.out.println("Enter the number of clusters: ");
		Scanner a = new Scanner(System.in);
		int clusterCount = Integer.parseInt(a.nextLine());
		a.close();
		
		// compute the clusters
		List<Set<Restaurant>> clusters = Algorithms.kMeansClustering(clusterCount, db);
		String JSONString = Algorithms.convertClustersToJSON(clusters);
		
		try {
			FileWriter outputFile = new FileWriter("visualize/voronoi.json");
			outputFile.write(JSONString);
			outputFile.close();
		} catch (IOException e) {
			System.out.println("Could not voronoi.json file");
			e.printStackTrace();
		}
		System.out.println("Open voronoi.html to view the results.");
		System.out.println("Enjoy your visualization!");
	}
}
