package pw.cinque.keystrokesmod.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is used to keep track of the player's clicking speed (clicks per second).
 */
public class ClickCounter {

    private final Queue<Long> clicks = new LinkedList<>();

    /**
     * Register a new click.
     */
    public void onClick() {
        clicks.add(System.currentTimeMillis() + 1000L);
    }

    /**
     * Get the amount of clicks registered in the past second.
     *
     * @return The clicks per second
     */
    public int getCps() {
        long time = System.currentTimeMillis();

        while (!clicks.isEmpty() && clicks.peek() < time) {
            clicks.remove();
        }

        return clicks.size();
    }

}
