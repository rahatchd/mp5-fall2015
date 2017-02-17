package ca.ece.ubc.cpen221.mp5;

import java.io.IOException;

public class MainServer {
public static void main(String[] args) throws IOException {
        int port = Integer.valueOf(args[0]).intValue();
        String restaurantFile = args[1];
        String reviewFile = args[2];
        String userFile = args[3];

        RestaurantDBServer rdbs = new RestaurantDBServer(port,restaurantFile,reviewFile,userFile);
    }

}
