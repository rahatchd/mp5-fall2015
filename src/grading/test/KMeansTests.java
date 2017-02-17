package grading.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.statlearning.Algorithms;
import grading.staff.JSONInterpreter;
import grading.student.RestaurantExtractor;

public class KMeansTests extends LocalTestBase {

    // 30-second timeout for K-means
    public static final int TIMEOUT_MS=30000;
    
    /**
     * Checks that the provided clusters have indeed converged to the K sets
     * @param K
     * @param clusters
     * @return
     */
    public static boolean checkConvergence(int K, List<Set<Restaurant>> clusters) {

        // cluster centers
        double[] longitudes = new double[K];
        double[] latitudes = new double[K];

        // re-compute centers
        int clusterId = 0;
        for (Set<Restaurant> cluster : clusters) {
            longitudes[clusterId] = 0;
            latitudes[clusterId] = 0;

            for (Restaurant r : cluster) {
                double lat = RestaurantExtractor.getLatitude(r);
                double lon = RestaurantExtractor.getLongitude(r);
                latitudes[clusterId] += lat;
                longitudes[clusterId] += lon;
            }

            latitudes[clusterId] /= (double)cluster.size();
            longitudes[clusterId] /= (double)cluster.size();

            ++clusterId;
        }

        // check that each restaurant is in the correct cluster
        clusterId = 0;
        for (Set<Restaurant> cluster : clusters) {

            for (Restaurant r : cluster) {
                double lat = RestaurantExtractor.getLatitude(r);
                double lon = RestaurantExtractor.getLongitude(r);

                int newClusterId = -1;
                double minDistanceSquared = Double.POSITIVE_INFINITY;
                for (int i=0; i<K; ++i) {
                    double dlat = lat-latitudes[i];
                    double dlon = lon-longitudes[i];
                    double distanceSquared = dlat*dlat + dlon*dlon;
                    if (distanceSquared < minDistanceSquared) {
                        minDistanceSquared = distanceSquared;
                        newClusterId = i;
                    }
                }

                if (newClusterId != clusterId) {
                    // check cluster centers are not within epsilon
                    // for convergence tolerance (in case two centers are equal)
                    double EPS = 1e-5;
                    double dlat = latitudes[newClusterId]-latitudes[clusterId];
                    double dlon = longitudes[newClusterId]-longitudes[clusterId];;
                    double ddist = dlat*dlat + dlon*dlon;

                    if (ddist > EPS) {
                        return false;
                    }
                }

            }

            ++clusterId;
        }
        return true;
    }

    /**
     * Applies a K-means test
     * @param K
     */
    public void meansTest(int K) {

        List<Set<Restaurant>> clusters = Algorithms.kMeansClustering(K, db);
        Assert.assertEquals("Number of clusters do not match", K, clusters.size());        

        // check all sets non-empty
        int totalSize = 0;
        HashSet<Restaurant> restaurants = new HashSet<>();

        for (Set<Restaurant> s : clusters) {
            Assert.assertNotEquals("Empty cluster found", s.size(), 0);
            restaurants.addAll(s);
            totalSize += s.size();
        }
        Assert.assertEquals("Duplicates found", allRestaurants.size(), totalSize);
        Assert.assertEquals("Not all restaurants present", allRestaurants.size(), restaurants.size());
        Assert.assertTrue("Means did not converge", checkConvergence(K, clusters));
    }

    @Test(timeout=TIMEOUT_MS)
    public void test1() {
        meansTest(1);
    }

    @Test(timeout=TIMEOUT_MS)
    public void test5() {
        meansTest(5);
    }

    @Test(timeout=TIMEOUT_MS)
    public void test30() {
        if (allRestaurants.size() < 30) {
            meansTest(30);
        } else {
            meansTest(allRestaurants.size());
        }
    }

    @Test(timeout=TIMEOUT_MS)
    public void testIndividual() {
        meansTest(allRestaurants.size());
    }

    @Test(timeout=TIMEOUT_MS)
    public void testJSON() throws IOException, ParseException {

        int K = 3;
        List<Set<Restaurant>> clusters = Algorithms.kMeansClustering(K, db);
        String jsonStr = Algorithms.convertClustersToJSON(clusters);
        JSONInterpreter json = new JSONInterpreter();

        Object obj = json.readResponse(jsonStr);
        if (! (obj instanceof List) ) {
            throw new IOException("Parsed json is not of List type");
        }
        JSONArray jarray = new JSONArray();
        jarray.addAll((List<?>)obj);

        // check that clusters match original sets
        // name-based clusters from Set
        HashSet<HashSet<String>> nameClusters = new HashSet<>(K);
        for (Set<Restaurant> cluster : clusters) {
            HashSet<String> nameCluster = new HashSet<>(cluster.size());
            for (Restaurant r : cluster) {
                String rname = RestaurantExtractor.getName(r);
                nameCluster.add(rname);
            }
            nameClusters.add(nameCluster);
        }

        // name-based clusters from JSON
        HashMap<Object,HashSet<String>> jsonClusterMap = new HashMap<>(K);
        for (Object elem : jarray) {

            JSONObject jobj = (JSONObject)elem;
            String name = (String)(jobj.get("name"));
            Object clusterId = jobj.get("cluster"); 

            HashSet<String> jsonCluster = jsonClusterMap.get(clusterId);
            if (jsonCluster == null) {
                jsonCluster = new HashSet<>();
                jsonClusterMap.put(clusterId, jsonCluster);
            }

            jsonCluster.add(name);
        }

        HashSet<HashSet<String>> jsonClusters = new HashSet<>(jsonClusterMap.values());
        if (!nameClusters.equals(jsonClusters)) {
            HashSet<HashSet<String>> diff = new HashSet<>(nameClusters);
            diff.removeAll(jsonClusters);
            System.out.println("In nameClusters: " + diff.toString());
            
            diff = new HashSet<>(jsonClusters);
            diff.removeAll(nameClusters);
            System.out.println("In jsonClusters: " + diff.toString());
        }
        Assert.assertEquals("JSON and Restaurant clusters do not match", nameClusters, jsonClusters);

    }

}
