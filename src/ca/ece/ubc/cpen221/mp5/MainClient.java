package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
public static void main(String[] args) throws IOException {
        
        System.out.println("Connecting to server...");
        Socket socket = new Socket(args[0],Integer.parseInt(args[1]));
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);
        
        System.out.println("Connection established!");
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            System.out.println("Enter a query:");
            String clientInput = scanner.nextLine();
            
            out.println(clientInput);
            out.flush();
            
            System.out.println(in.readLine());
        }
        
    }
}
