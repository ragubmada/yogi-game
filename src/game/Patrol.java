package game;


/**
 * The Patrol class represents a patrol within the Yogi game.
 * It manages the movement and position of patrols on the game map.
 */
public class Patrol {
    private int x, y;
    private final boolean isVertical;
    private int direction; // 1 for positive movement, -1 for negative movement


    /**
     * Constructs a Patrol object with initial position and movement direction.
     *
     * @param x          The initial x-coordinate of the patrol.
     * @param y          The initial y-coordinate of the patrol.
     * @param isVertical Determines if the patrol moves vertically or horizontally.
     */
    public Patrol(int x, int y, boolean isVertical) {
        this.x = x;
        this.y = y;
        this.isVertical = isVertical;
        this.direction = 1; // Initially moving in the positive direction
    }

    /**
     * Retrieves the current x-coordinate of the patrol.
     *
     * @return The x-coordinate of the patrol.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the current y-coordinate of the patrol.
     *
     * @return The y-coordinate of the patrol.
     */
    public int getY() {
        return y;
    }

    /**
     * Moves the patrol on the map based on its defined movement pattern.
     *
     * @param map The map representing the game grid.
     */
    public void move(int[][] map) {
        int currentX = x;
        int currentY = y;

        // Determine movement based on vertical or horizontal patrol
        if (isVertical) {
            currentX += direction;
        } else {
            currentY += direction;
        }

        // Check if the next step encounters an obstacle or reaches the grid's edge
        if (currentX < 0 || currentX >= map.length || currentY < 0 || currentY >= map[0].length ||
                map[currentX][currentY] == 't' || map[currentX][currentY] == 'm' || map[currentX][currentY] == 'b') {
            direction *= -1;
        } else if (map[currentX][currentY] == 'v' || map[currentX][currentY] == 'h') {
            direction *= -1;
        } else {
            // Update the map and position
            map[x][y] = 0; // Clear previous position
            x = currentX;
            y = currentY;
            map[x][y] = isVertical ? 'v' : 'h'; // Update patrol position on the map
        }
    }
}