package grading.test;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;

public class QueryTests extends LocalTestBase {
   
    // time limit
    static final int TIMEOUT_MS = 10000;
    
    @Test(timeout=TIMEOUT_MS)
    public void testIn() {
        
        String queryStr = "in(\"Telegraph Ave\")";
        Set<Restaurant> r = db.query(queryStr);
        Assert.assertEquals("Sizes do not match", 76, r.size());
    }
    
    @Test(timeout=TIMEOUT_MS)
    public void testPrice() {
        String queryStr = "price(1..2)";
        Set<Restaurant> r = db.query(queryStr);
        Assert.assertEquals("Sizes do not match", 115, r.size());
    }
    
    @Test(timeout=TIMEOUT_MS)
    public void testProvidedExample() {
        String queryStr = "in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(1..2)";
        Set<Restaurant> r = db.query(queryStr);
        Assert.assertEquals("Sizes do not match", 9, r.size());
    }
    
    @Test(timeout=TIMEOUT_MS)
    public void testAndAnd() {
        String queryStr = "in(\"Telegraph Ave\") && category(\"Chinese\") && price(1..2)";
        Set<Restaurant> r = db.query(queryStr);
        Assert.assertEquals("Sizes do not match", 7, r.size());
    }
    
    @Test(timeout=TIMEOUT_MS)
    public void testComplex() {
        String queryStr = "in(\"Downtown Berkeley\") && price(2..4) || (category(\"Italian\")) && rating(3..5) || category(\"Delis\") && name(\"Hummingbird Cafe\")";
        Set<Restaurant> r = db.query(queryStr);
        Assert.assertEquals("Sizes do not match", 13, r.size());
    }
    
}
