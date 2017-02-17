package grading.staff;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamForwarder implements Runnable {

    InputStream fromStream;
    OutputStream toStream;
    
    public StreamForwarder(InputStream from, OutputStream to) {
        this.fromStream = from;
        this.toStream = to;
    }
    
    @Override
    public void run() {
        boolean done = false;
        while (!done) {
            try {
                int c = fromStream.read();
                if (c == -1) {
                    done = true;
                } else {
                    toStream.write(c);
                }
            } catch (IOException e) {
               done = true;
            }
        }
        
        try {
            toStream.flush();
        } catch (IOException e) {
        }
        
        // close up
        if (fromStream != System.in) {
            try {
                fromStream.close();
            } catch (IOException e) {
            }
        }
        
        if (toStream != System.out) {
            try {
                toStream.close();
            } catch (IOException e) {}
        }
    }
    
}
