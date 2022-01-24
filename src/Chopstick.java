public class Chopstick {
    public boolean inUse;
    public int spot;

    /**
     * Used by a philosopher to eat. Mostly just a "flag" to tell the philosopher
     * if they can eat or not.
     * @param s The position the chopstick is at. Used to determine which
     *          philosopher's can reach it.
     */
    public Chopstick(int s) {
        spot = s;
        inUse = false;
    }

    /**
     * To check if the chopstick is being used or not.
     * @return inUse boolean.
     */
    public boolean inUse() {
        return inUse;
    }

    /**
     * switch inUse to true from false or false from true.
     */
    public void changeUse() {
        inUse = !inUse;
    }

    /**
     * To get the spot the chopstick should be.
     * @return spot integer.
     */
    public int getSpot() {
        return spot;
    }
}
