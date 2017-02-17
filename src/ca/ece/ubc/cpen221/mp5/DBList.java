package ca.ece.ubc.cpen221.mp5;

import java.util.Iterator;


/*
 * Thread safe list
 */
public class DBList<T> implements Iterable<T>{
    private int size;
    private DBListInternal<T> list;
    /**
     * Thread Safety Argument:
     * 		This data structure is suitable to use as the internal representation of a database,
     * 		since one can only append to it, not remove from it.  It does not expose its rep (confines
     * 		its fields), and its only return values are immutable.
     */
    
    /**
     * Constructs an empty DBList.
     */
    public DBList(){
        size = 0;
        list = new DBListInternal<T>();
    }
    
    /**
     * Adds a given item to the head of the list
     * 
     * @param item item to add
     */
    public void add(T item){
        list = new DBListInternal<T>(item, list);
        size++;
    }
    
    /**
     * Returns the number of elements in the list.
     * 
     * @return the number of elements in the list
     */
    public int size() {
        return size;
    }
    
    
    /**
     * Gets the element at a specified index in a list.
     * 
     * @param index of desired element
     * 			requires that index be an int < size()
     * @return the element at the specified index in the list
     */
    public T get(int index) {
        DBListInternal<T> tempList = list;
        T head = null;
        while (index >= 0){
            head = tempList.head;
            tempList = tempList.tail;
            index--;
        }
        return head;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new DBListIterator<T>(list,size);
    }
    
    /*
     * Internal representation of a DBList.  This datatype is recursive.
     * 
     * Rep Invariant : each head is an object of type TT or is null; each tail has a head and a tail
     * Abstraction Function : represents a list of objects of type TT
     * 
     */
    private class DBListInternal<TT>{
        private final TT head;
        private final DBListInternal<TT> tail;
        
        public DBListInternal(){
            head = null;
            tail = null;
        }
        
        public DBListInternal(TT head, DBListInternal<TT> tail){
            this.head = head;
            this.tail = tail;
        }
        
    }
    
    /*
     * Internal representation for an iterator object.  This representation allows concurrent traversal
     * and mutation of a DBList.
     * 
     */
    private class DBListIterator<TT> implements Iterator<TT>{
        private DBListInternal<TT> list;
        private int size;
        
        /**
         * Constructs a DBListIterator capable of traversing a DBList.
         * 
         * @param list internal representation of a DBList
         * @param size the size of the DBList's internal representation
         */
        public DBListIterator(DBListInternal<TT> list,int size){
            this.list = list;
            this.size = size;
        }
        
        @Override
        public boolean hasNext() {
            return this.list.head != null;
        }

        @Override
        public TT next() {
            TT head = list.head;
            list = list.tail;
            size--;
            return head;
        }
        
    }
    
}
