package grading.staff;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class TriggeredCallable<T> implements Callable<T> {

    
    /**
     * Class to help trigger simultaneous events on separate threads
     * @author Antonio
     *
     */
    public static class Trigger {
        CountDownLatch latch;
        private volatile boolean fired;
        
        public Trigger() {
            fired = false;
            latch = new CountDownLatch(1);
        }
        
        /**
         * Get ready...
         */
        public void mark() {
            while (!fired) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
            }
        }
        
        /**
         * reload
         */
        public void reset() {
            fired = false;
            latch = new CountDownLatch(1);
        }
        
        /**
         * GO!!
         */
        public void fire() {
            fired = true;
            latch.countDown();
        }
    }
    
    Trigger trigger;
    
    /**
     * Callable that will wait on a given trigger before execution
     */
    public TriggeredCallable(Trigger trigger) {
        this.trigger = trigger;
    }
    
    @Override
    public final T call() throws Exception {
        trigger.mark();
        return callTriggered();
    }
    
    /**
     * Action to perform when triggered
     * @return
     * @throws Exception
     */
    public T callTriggered() throws Exception {
        return null;
    }

}
