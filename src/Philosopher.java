import java.util.Random;

public class Philosopher implements Runnable{
    // flag for if a philosopher wants to eat.
    private volatile boolean isHungry;
    // flag for starting/stopping the philosopher.
    private volatile boolean running;
    // The table the philosopher is sitting at and position at the table.
    private final Table table;
    private final int spot;

    // the philosopher's thread.
    private final Thread thread;

    // Abstract number of milliseconds that represents the maximum amount
    // of time a philosopher will wait before getting hungry.
    private static final int MAX_WAIT = 5000;
    // how long in milliseconds it takes philosophers to eat
    private static final long EAT_TIME = 1500;

    /**
     *
     * @param t Table, the table the philosopher is at (This table should have the same
     *          amount of chopsticks as philosopher's).
     * @param s Position of the philosopher. Used to determine what chopsticks he can reach.
     */
    public Philosopher(Table t, int s) {
        thread = new Thread(this);
        isHungry = false;
        running = false;
        spot = s;
        table = t;
    }

    /**
     * run method for the philosopher's thread. Will wait for a random time between 0 and
     * MAX_WAIT (in milliseconds) before "becoming hungry" and will then try to
     * acquire some chopsticks using the Table's getChopsticks() method. If it cannot
     * acquire the chopsticks it will busy wait until it can get them. after getting the chopsticks
     * it will "eat" for the EAT_TIME (milliseconds) and then return the chopsticks and go into
     * another cycle.
     */
    public synchronized void run() {
        Random rand = new Random();
        while(running) {
            // wait for a random amount of time until hungry.
            long time = (long) (rand.nextFloat() * MAX_WAIT);
            try {
                wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // become hungry and request chopsticks until given.
            isHungry = true;
            while(isHungry) {
                Chopstick[] chopsticks = table.getChopsticks(spot);
                // if successful in getting chopsticks, "eat" and then return them.
                if(chopsticks != null) {
                    try {
                        System.out.println("Philosopher "+spot+" eating using chopsticks "+chopsticks[0].getSpot()+" "+chopsticks[1].getSpot());
                        wait(EAT_TIME); // "eating"
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // return chopsticks.
                    System.out.println("Philosopher "+spot+" returned chopsticks "+chopsticks[0].getSpot()+" "+chopsticks[1].getSpot());
                    table.returnChopsticks(chopsticks);
                    isHungry = false;
                }
            }
        }
    }

    /**
     * start method for the philosopher's thread.
     */
    public void start() {
        running = true;
        thread.start();
    }

    /**
     * stop method for the philosopher's thread.
     */
    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
