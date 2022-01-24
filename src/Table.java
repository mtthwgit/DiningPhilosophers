import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Table {
    // chopsticks is for storing the chopsticks while the philosophers are not eating.
    // 1-# of philosophers for the key while a chopstick for the value.
    public Map<Integer, Chopstick> chopsticks = new HashMap<>();
    // number of philosophers at the table.
    public static final int NUM_PHIL = 5;
    // how long the program will run in milliseconds.
    public static final int RUNTIME = 10000;

    /**
     * Table for philosopher's to dine at. They will use chopsticks they can reach to eat
     * for a random amount of time. Table keeps track of which chopsticks are available and
     * what philosopher's can reach each chopstick (a philosopher can reach a chopstick in front of him
     * or directly to his right or left).
     */
    public Table() {
        for(int i = 0; i < NUM_PHIL; i++) {
            chopsticks.put(i,new Chopstick(i));
        }
    }

    /**
     * Method to be called to get chopsticks for a philosopher to eat
     *
     * @param position the position of the philosopher.
     * @return Chopstick[] the chopsticks the philosopher is getting.
     */
    public synchronized Chopstick[] getChopsticks(int position) {
        // get the indexes.
        int[] ChopstickIndex = new int[3];
        // flag.
        int successes = 0;
        Chopstick[] retChopsticks = new Chopstick[2];
        // for finding which chopsticks the philosopher can reach.
        switch(position) {
            case 0:
                ChopstickIndex[0] = NUM_PHIL-1;
                ChopstickIndex[1] = position;
                ChopstickIndex[2] = position+1;
                break;
            case NUM_PHIL-1:
                ChopstickIndex[0] = position-1;
                ChopstickIndex[1] = position;
                ChopstickIndex[2] = 0;
                break;
            default:
                ChopstickIndex[0] = position-1;
                ChopstickIndex[1] = position;
                ChopstickIndex[2] = position+1;
                break;
        }
        // if a chopstick in the index is not in use, add it to the return list.
        for(int index:ChopstickIndex) {
            if(!chopsticks.get(index).inUse() && successes < 2) {
                retChopsticks[successes++] = chopsticks.get(index);
            }
        }
        // if we got enough chopsticks, let the philosopher have them. Else return null.
        if(successes == 2) {
            for(Chopstick c:retChopsticks) {
                c.changeUse();
            }
            return retChopsticks;
        } else {
            return null;
        }
    }

    /**
     * Method to be called to return chopsticks that a philosopher is done using.
     *
     * @param chopsticks the chopsticks being returned.
     */
    public synchronized void returnChopsticks(Chopstick[] chopsticks) {
        for(Chopstick c:chopsticks) {
            c.changeUse();
        }
    }

    public static void main (String[] args) {
        Table t = new Table();
        Philosopher[] p = new Philosopher[NUM_PHIL];

        for(int i = 0; i < NUM_PHIL; i++) {
            p[i] = new Philosopher(t,i);
        }

        for(Philosopher phil:p) {
            phil.start();
        }

        try {
            sleep(RUNTIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Philosopher phil:p) {
            phil.stop();
        }
    }
}