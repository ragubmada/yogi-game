package game;

/**
 * The Player class represents the player in the Yogi game.
 * It manages the player's position, score, health points, and movement.
 */
public class Player {
    private final int initialX;
    private final int initialY;
    private int x, y;
    private int score;
    private int HP;
    private int basketCount;

    /**
     * Constructs a Player object with initial position and default score and health points.
     *
     * @param x The initial x-coordinate of the player.
     * @param y The initial y-coordinate of the player.
     */
    public Player(int x, int y) {
        this.initialX = x;
        this.initialY = y;

        this.x = x;
        this.y = y;

        this.score = 0;
        this.HP = 3;
    }

    /**
     * Retrieves the current x-coordinate of the player.
     *
     * @return The x-coordinate of the player.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Retrieves the current y-coordinate of the player.
     *
     * @return The y-coordinate of the player.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Retrieves the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the player's score to a specified value.
     *
     * @param score The new score value to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves the player's current health points.
     *
     * @return The player's health points.
     */
    public int getHP() {
        return this.HP;
    }

    /**
     * Sets the player's health points to a specified value.
     *
     * @param hp The new health points value to set.
     */
    public void setHP(int hp) {
        this.HP = hp;
    }

    /**
     * Retrieves the current basket count held by the player.
     *
     * @return The player's basket count.
     */
    public int getBasketCount() {
        return basketCount;
    }

    /**
     * Sets the basket count held by the player.
     *
     * @param basketCount The new basket count value to set.
     */
    public void setBasketCount(int basketCount) {
        this.basketCount = basketCount;
    }

    /**
     * Moves the player on the map based on specified dx and dy values.
     * It also manages score updates and collision checks with game elements.
     *
     * @param dx       The change in the x-coordinate for player movement.
     * @param dy       The change in the y-coordinate for player movement.
     * @param map      The map representing the game grid.
     * @param gridSize The size of the game grid.
     */
    public void move(int dx, int dy, int[][] map, int gridSize) {
        int newX = x + dx;
        int newY = y + dy;

        if (isValidMove(newX, newY, map, gridSize)) {

            if (map[newX][newY] == 'b') {
                score++;
                basketCount--;
            }

            map[x][y] = (x == initialX && y == initialY) ? 'g' : 0;
            x = newX;
            y = newY;
            map[x][y] = 'y';
        }
    }

    /**
     * Checks if the intended move for the player is valid within the game grid and avoids collisions.
     *
     * @param newX     The new x-coordinate after the intended move.
     * @param newY     The new y-coordinate after the intended move.
     * @param map      The map representing the game grid.
     * @param gridSize The size of the game grid.
     * @return True if the move is valid and doesn't cause collision, otherwise False.
     */
    private boolean isValidMove(int newX, int newY, int[][] map, int gridSize) {
        return newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize &&
                map[newX][newY] != 't' && map[newX][newY] != 'm' &&
                map[newX][newY] != 'v' && map[newX][newY] != 'h';
    }

    /**
     * Resets the player's position on the map to its initial position.
     *
     * @param map The map representing the game grid.
     */
    public void resetPosition(int[][] map) {
        map[x][y] = 0;
        this.x = initialX;
        this.y = initialY;
        map[x][y] = 'y';
    }
}
