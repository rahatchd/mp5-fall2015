package grading.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import grading.staff.ClientRequestWrapper;
import grading.staff.ClientRequestWrapper.RequestType;
import grading.staff.QueryException;
import grading.staff.RequestException;
import grading.staff.RestaurantDBClient;
import grading.staff.TriggeredCallable;
import grading.staff.TriggeredCallable.Trigger;
import grading.staff.TriggeredQuery;

public class ServerTests extends ServerTestBase {
    
    public static final int TIMEOUT_MS = 30000;
    public static final int HEAVY_TIMEOUT_MS = 60000;  // for harder processes
    
    @Test(timeout=TIMEOUT_MS)
    public void testGetRestaurant() throws ParseException, IOException, RequestException {

        // get restaurant: qHmamQPCAKkia9X0uryA8g
        // Top dog
        String topDogStr = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/top-dog-berkeley\", \"longitude\": -122.2574328, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"qHmamQPCAKkia9X0uryA8g\", \"name\": \"Top Dog\", \"categories\": [\"Restaurants\", \"Hot Dogs\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 4.5, \"city\": \"Berkeley\", \"full_address\": \"2534 Durant Ave\nTelegraph Ave\nBerkeley, CA 94704\", \"review_count\": 1270, \"photo_url\": \"http://s3-media4.ak.yelpcdn.com/bphoto/nFfca2I2F0Az2JmGQmAipg/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8678945, \"price\": 1}";
        JSONParser parser = new JSONParser();
        JSONObject topDog = (JSONObject)(parser.parse(topDogStr));

        RestaurantDBClient client = createConnectedClient();
        Map<String,?>    received = client.getRestaurant((String)(topDog.get("business_id")));

        // close client
        client.disconnect();

        Assert.assertEquals("Received restaurant different from expected", topDog, received);
    }

    @Test(timeout=TIMEOUT_MS)
    public void testRandomReview() throws IOException, RequestException, ParseException {

        // Top Dog has 1000+ reviews, and there are two location with different reviews
        HashSet<String> topDogIdSet = new HashSet<>();
        topDogIdSet.add("65ltOonS7uaG12RRdn-W3Q");
        topDogIdSet.add("qHmamQPCAKkia9X0uryA8g");
        String topDogRestaurantName = "Top Dog";

        RestaurantDBClient client = createConnectedClient();

        Map<String,?> response = client.getRandomReview(topDogRestaurantName);
        Assert.assertTrue("Review restaurant ids do not match", topDogIdSet.contains(response.get("business_id")));

        client.disconnect();
    }

    @Test(timeout=TIMEOUT_MS)
    public void testRandomReviewRandomness() throws IOException, RequestException, ParseException {

        // Top Dog has 1000+ reviews, and there are two location with different reviews
        HashSet<String> topDogIdSet = new HashSet<>();
        topDogIdSet.add("65ltOonS7uaG12RRdn-W3Q");
        topDogIdSet.add("qHmamQPCAKkia9X0uryA8g");
        String topDogRestaurantName = "Top Dog";

        HashSet<Map<?,?>> responses = new HashSet<>();

        RestaurantDBClient client = createConnectedClient();

        // try 50 times
        for (int i=0; i<50; ++i) {
            Map<String,?> response = client.getRandomReview(topDogRestaurantName);
            Assert.assertTrue("Review restaurant ids to not match", topDogIdSet.contains(response.get("business_id")));
            responses.add(response);

            if (responses.size() > 3) {
                break;
            }
        }

        client.disconnect();

        Assert.assertTrue("Insufficient randomness (received " + responses.size() + " unique review out of 1000+)", responses.size() > 3);

    }

    @Test(timeout=TIMEOUT_MS)
    public void testConcurrentClients() throws ParseException, IOException, RequestException {

        String benneStr = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/pasta-bene-berkeley\", \"longitude\": -122.2585639, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": \"QQIjsdcokFermi2ugoD6ow\", \"name\": \"Pasta Bene\", \"categories\": [\"Italian\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 4.0, \"city\": \"Berkeley\", \"full_address\": \"2565 Telegraph Ave\\nUC Campus Area\\nBerkeley, CA 94704\", \"review_count\": 283, \"photo_url\": \"http://s3-media3.ak.yelpcdn.com/bphoto/6CMjvONYO6i5-axxbbwhGg/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8639919, \"price\": 2}";
        JSONParser parser = new JSONParser();
        JSONObject benne = (JSONObject)(parser.parse(benneStr));

        RestaurantDBClient client1 = createConnectedClient();
        RestaurantDBClient client2 = createConnectedClient();

        String benneId = (String)(benne.get("business_id"));
        Map<String,?> restaurant = client1.getRestaurant(benneId);
        Map<String,?> review  = client2.getRandomReview((String)(benne.get("name")));

        Assert.assertEquals("Restaurants do not match", benne, restaurant);
        Assert.assertEquals("Review not for given restaurant", benneId, review.get("business_id"));

        client1.disconnect();
        client2.disconnect();
    }

    @Test (timeout=TIMEOUT_MS)
    public void testSingleQuery() throws QueryException, IOException, ParseException {

        String queryStr = "(category(\"MadeUpCategory\") && price(1..3) || category(\"Chinese\"))";

        // solution
        String solStr = "[" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.867768,\"review_count\":10,\"full_address\":\"2516 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":3.5,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/peking-express-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":1,\"name\":\"Peking Express\",\"photo_url\":\"http://s3-media1.ak.yelpcdn.com/bphoto/BPMBr2aiOEpVioLb-RurJQ/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"1E2MQLWfwpsId185Fs2gWw\",\"open\":true,\"longitude\":-122.2581978},\n" +
                "{\"neighborhoods\":[\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.8752015,\"review_count\":71,\"full_address\":\"2507 Hearst Ave\\nUC Campus Area\\nBerkeley, CA 94709\",\"stars\":3.0,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/t-c-garden-restaurant-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":1,\"name\":\"T C Garden Restaurant\",\"photo_url\":\"http://s3-media4.ak.yelpcdn.com/bphoto/v_vEw_Kb7EufdDP1nJ7Npg/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"wIhtFlx1DdDvYH0fjUAtkg\",\"open\":true,\"longitude\":-122.2597581},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.86788,\"review_count\":140,\"full_address\":\"2439 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":3.0,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/sun-hong-kong-restaurant-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":2,\"name\":\"Sun Hong Kong Restaurant\",\"photo_url\":\"http://s3-media3.ak.yelpcdn.com/bphoto/9Cq_YlvoSN1FxY6DZeLYhA/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"8Xq5VtwYjayKlxEY2PipQA\",\"open\":true,\"longitude\":-122.2595134},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.8667955,\"review_count\":67,\"full_address\":\"2488 Channing Way\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":3.5,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/chinese-express-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":1,\"name\":\"Chinese Express\",\"photo_url\":\"http://s3-media4.ak.yelpcdn.com/bphoto/osAqCjzpUzVx-3u0HxaHxQ/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"t-xuA4yR02gud00gTS2iyw\",\"open\":true,\"longitude\":-122.2592389},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.868035,\"review_count\":160,\"full_address\":\"2517A Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":3.0,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/lotus-house-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":2,\"name\":\"Lotus House\",\"photo_url\":\"http://s3-media3.ak.yelpcdn.com/bphoto/w4ig8KmeCt9wYkeYrDehIA/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Food\",\"Coffee & Tea\",\"Chinese\",\"Restaurants\"],\"business_id\":\"XBPMMfMchDlxZG-qSsSdtw\",\"open\":true,\"longitude\":-122.258216},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.868144,\"review_count\":102,\"full_address\":\"2519 Durant Avenue Suite A\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":2.5,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/mandarin-house-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":2,\"name\":\"Mandarin House\",\"photo_url\":\"http://s3-media3.ak.yelpcdn.com/bphoto/59RZguaJAofg4ENMM1H_4g/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"5fneYCWLhgBZQUcNPOch-w\",\"open\":true,\"longitude\":-122.2580885},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.8650499,\"review_count\":67,\"full_address\":\"2502 Telegraph Ave\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":4.0,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/happy-valley-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":2,\"name\":\"Happy Valley\",\"photo_url\":\"http://s3-media4.ak.yelpcdn.com/bphoto/AsdudePLe4plnylNFjJkEA/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Chinese\",\"Restaurants\"],\"business_id\":\"ERRowW4pGO6pK9sVYyA1nQ\",\"open\":true,\"longitude\":-122.2584562},\n" +
                "{\"neighborhoods\":[\"Telegraph Ave\",\"UC Campus Area\"],\"city\":\"Berkeley\",\"latitude\":37.8680415,\"review_count\":66,\"full_address\":\"2517 Durant Ave\\nSte D 2nd Fl\\nTelegraph Ave\\nBerkeley, CA 94704\",\"stars\":2.5,\"type\":\"business\",\"url\":\"http://www.yelp.com/biz/chang-luong-restaurant-berkeley\",\"schools\":[\"University of California at Berkeley\"],\"price\":1,\"name\":\"Chang Luong Restaurant\",\"photo_url\":\"http://s3-media3.ak.yelpcdn.com/bphoto/cM3Nuc5KnRB1vleYyIN1jg/ms.jpg\",\"state\":\"CA\",\"categories\":[\"Dim Sum\",\"Chinese\",\"Restaurants\"],\"business_id\":\"_mv3DhRD3L3okFXYjxX_Cg\",\"open\":true,\"longitude\":-122.2582906}]";

        JSONParser parser = new JSONParser();
        JSONArray solArray = (JSONArray)(parser.parse(solStr));
        HashSet<Object> sol = new HashSet<>(solArray);

        // get response from client
        HashSet<Map<String,?>> responses = new HashSet<>();
        RestaurantDBClient client = createConnectedClient();

        List<JSONObject> response = client.query(queryStr);
        responses.addAll((Collection<? extends Map<String, ?>>) response);

        client.disconnect();

        Assert.assertEquals("Query results do not match", sol,  responses);
    }

    @Test(timeout=HEAVY_TIMEOUT_MS)
    public void testConcurrentQueries() throws QueryException, IOException {

        // test a query K times in a row
        // *should* roughly be scaled by number of available processors
        // e.g. 2 processors -> expected time is 0.5*Kx time for single query
        //
        // To be safe, let's allow a 50% leeway
        //      N processors -> (1.5*K/N)x time for single query
        // then it is a poor concurrency implementation
        int K = 20;
        int R = 20;  // repeat R times to get good estimate of time
        int N = 2;

        double timeFactor = 1.5*K/N;
        // service for running our processes in parallel
        ExecutorService executor = Executors.newFixedThreadPool(N);

        // query to run
        String queryStr = "(category(\"Restaurants\") && price(1..3) || category(\"Chinese\") && rating(3..5)) || in(\"UC Campus Area\") && rating(1..2)";

        // dry-run without timing
        Trigger trigger = new Trigger();  // for synchronizing execution
        TriggeredQuery queryRequest = new TriggeredQuery(trigger, createConnectedClient(), queryStr);
        Future<List<JSONObject>> queryFuture = executor.submit(queryRequest);

        // GO!
        trigger.fire();
        try {
            queryFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Original query failed to run", e);
        }

        //-----------------------------------------------------------
        // Single Query
        //-----------------------------------------------------------
        // run single query once using triggered pattern to get calibration time
        double singleTime = 0;
        for (int i=0; i<R; ++i) {
            trigger = new Trigger();  // for synchronizing execution
            queryRequest = new TriggeredQuery(trigger, createClient(), queryStr);
            queryFuture = executor.submit(queryRequest);

            // GO!
            trigger.fire();
            long start = System.currentTimeMillis();
            try {
                queryFuture.get();
            } catch (Exception e) {
                throw new RuntimeException("Original query failed to run", e);
            }
            long time = System.currentTimeMillis()-start;
            singleTime += time;
        }
        singleTime = singleTime/R;  // average time for single request
        double thresholdTime = timeFactor*singleTime;
        // System.out.println("Single query time: " + singleTime + " ms");
        // System.out.println("Time limit: " + thresholdTime + " ms");


        //-----------------------------------------------------------
        // K - Queries
        //-----------------------------------------------------------
        long ktime = 0;
        for (int j=0; j<R; ++j) {
            // Set up K process
            trigger = new Trigger();  // for synchronizing execution
            ArrayList<Future<?>> callableList = new ArrayList<>();
            for (int i=0; i<K; ++i) {
                queryRequest = new TriggeredQuery(trigger, createClient(), queryStr);
                callableList.add(executor.submit(queryRequest));
            }

            // run all processes
            trigger.fire();
            long start = System.currentTimeMillis();
            for (Future<?> future : callableList) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException("Query failed to run", e);
                }
            }
            long time = System.currentTimeMillis()-start;
            ktime += time;
        }
        ktime = ktime/R;

        if (ktime > thresholdTime) {
            System.out.println("Single query time: " + singleTime + " ms");
            System.out.println("Time limit (K=" + K + "): " + thresholdTime + " ms");
            System.out.println("Actual query time: " + ktime + " ms");
        }
        Assert.assertTrue("Inadequate concurrency implementation (single query time: " + singleTime + ", K=" + K + " time limit: " + thresholdTime + ", actual: " + ktime +")", ktime <= thresholdTime);

        executor.shutdown();
    }

    protected static HashMap<String,JSONObject> readJSON(String file, String keyField) {

        HashMap<String, JSONObject> out = new HashMap<>();

        BufferedReader reader = null;
        try {
            JSONParser parser = new JSONParser();
            reader = new BufferedReader(new FileReader(findFile(file)));
            String line = reader.readLine();

            try {
                Object o = parser.parse(line);
                if (o instanceof JSONObject) {
                    JSONObject jobj = (JSONObject)o;
                    out.put((String)(jobj.get(keyField)), jobj);
                }
            } catch (ParseException e) {
            }


        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return out;
    }

    public static <T> T pick(Random random, T[] set) {
        int idx = random.nextInt(set.length);
        return set[idx];
    }

    public static <T> T pick(Random random, List<? extends T> list) {
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }

    public static String randomString(Random random, int length) {
        final String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<length; ++i) {
            int idx = random.nextInt(charset.length());
            sb.append(charset.charAt(idx));
        }

        return sb.toString();
    }

    @Test(timeout=HEAVY_TIMEOUT_MS)
    public void testHammerTime() throws IOException, ParseException {

        // generate a whole bunch of random requests
        int K = 1000;

        Random random = new Random(System.currentTimeMillis());

        String[] queryTemplates = {
                "in(\"UC Campus Area\") && price(2..4)",
                "name(\"Top Dog\")",
                "category(\"Italian\")",
                "in(\"Telegraph Ave\") && rating(3..5)"
        };

        // load all JSON restaurants, mapped by business id
        HashMap<String,JSONObject> restaurants = readJSON("restaurants.json", "business_id");
        ArrayList<String> restaurantIds = new ArrayList<String>(restaurants.keySet());

        HashSet<JSONObject> addedRestaurants = new HashSet<>();
        HashSet<String> usedIds = new HashSet<String>(restaurantIds);

        // simultaneously connect with 1000 clients, 1000 random commands, all started simultaneously
        // check for server error, and that all added restaurants exist at the end

        // executor
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // trigger for starting requests
        Trigger trigger = new Trigger();

        ArrayList<Future<?>> futures = new ArrayList<>();

        for (int i=0; i<K; ++i) {
            // either query, add restaurant, get restaurant, or random review
            int nextType = random.nextInt(4);
            TriggeredCallable<?> nextRequest = null;
            switch (nextType) {
            case 0: {
                nextRequest = new TriggeredQuery(trigger, createClient(), pick(random, queryTemplates));
                break;
            }
            case 1: {
                // add a restaurant
                // pick random one to use as template
                JSONObject newRestaurant = new JSONObject(restaurants.get(pick(random, restaurantIds)));
                // change name and id
                String uid = null;
                do {
                    uid = randomString(random, 22);
                } while (usedIds.contains(uid));

                newRestaurant.put("business_id", uid);
                String name = (String)(newRestaurant.get("name"));
                newRestaurant.put("name", name + " - " + uid);

                // perturb latitude/longitude for kicks
                Double longitude = (Double)(newRestaurant.get("longitude"));
                longitude += (random.nextDouble()-0.5)/1000;
                newRestaurant.put("longitude", longitude);
                Double latitude = (Double)(newRestaurant.get("latitude"));
                latitude += (random.nextDouble()-0.5)/1000;
                newRestaurant.put("latitude", latitude);

                addedRestaurants.add(newRestaurant);
                usedIds.add(uid);

                nextRequest = new ClientRequestWrapper(trigger, createClient(), RequestType.ADD_RESTAURANT, newRestaurant);
                break;
            }
            case 2: {
                // get restaurant
                String id = pick(random, restaurantIds);
                nextRequest = new ClientRequestWrapper(trigger, createClient(), RequestType.GET_RESTAURANT, id);
                break;
            }
            default:
                // random review
                JSONObject restaurant = restaurants.get(pick(random, restaurantIds));
                String name = (String)restaurant.get("name");
                nextRequest = new ClientRequestWrapper(trigger, createClient(), RequestType.RANDOM_REVIEW, name);
            }

            if (nextRequest!= null) {
                futures.add(executor.submit(nextRequest));
            }
        } // generate K requests

        // GO!!!!
        trigger.fire();
        for (Future<?> fut : futures) {
            try {
                fut.get();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        // now sequentially check that all added restaurants are in database
        RestaurantDBClient client = createConnectedClient();
        for (JSONObject restaurant : addedRestaurants) {
            String id = (String)(restaurant.get("business_id"));
            JSONObject jobj = null;
            try {
                jobj = (JSONObject)(client.getRestaurant(id));
            } catch (RequestException e) {
            }

            Assert.assertEquals("Added restaurant not contained in DB", restaurant, jobj);
        }
        client.disconnect();
        
    }

}
