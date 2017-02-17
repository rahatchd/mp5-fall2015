package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.DBList;

public class DBListTest {
    
    @Test
    public void testAdd(){
       DBList<Integer> list = new DBList<Integer>();
       assertEquals(0,list.size());
       list.add(5);
       list.add(10);
       list.add(20);
       list.add(1292);
       list.add(3);
       assertEquals(5,list.size());
    }
    
    @Test
    public void testIterator(){
        DBList<Integer> list = new DBList<Integer>();
        list.add(5);
        list.add(10);
        list.add(20);
        list.add(1292);
        list.add(3);
        Iterator<Integer> iter = list.iterator();
        assert iter.hasNext() == true;
        assertEquals(3,iter.next().intValue());
        assertEquals(1292,iter.next().intValue());
        assertEquals(20,iter.next().intValue());
        assertEquals(10,iter.next().intValue());
        assertEquals(5,iter.next().intValue());
        assert iter.hasNext() == false;
        
    }
    
    @Test
    public void testGet(){
        DBList<Integer> list = new DBList<Integer>();
        list.add(5);
        list.add(10);
        list.add(20);
        list.add(1292);
        list.add(3);
        
        assertEquals(3,list.get(0).intValue());
        assertEquals(20,list.get(2).intValue());
        assertEquals(5,list.get(4).intValue());
        
    }

}
