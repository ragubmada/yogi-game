package game;

import highscore.HighScores;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The YogiGamePanel class represents the panel where the Yogi game is displayed.
 * It manages the game's visuals, user interactions, and timers for the game elements.
 */
public class YogiGamePanel extends JPanel {
    private static final int GRID_SIZE = 15;
    private static final int TILE_SIZE = 50;
    private final YogiGame yogiGame;
    private HighScores highScores;
    private int[][] map;
    private Player player;
    private ArrayList<Patrol> patrols;
    private BufferedImage emptyImage, mountainImage, treeImage, yogiImage, patrolImage, basketImage, gateImage;
    private Timer collCheckTimer;
    private Timer frameTimer;
    private final MovementListener movementListener;

    /**
     * Constructs a YogiGamePanel instance associated with a YogiGame.
     *
     * @param yogiGame The YogiGame instance this panel is associated with.
     */
    public YogiGamePanel(YogiGame yogiGame) {
        this.yogiGame = yogiGame;

        try {
            this.highScores = new HighScores(10);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        setLayout(null);
        setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));
        setFocusable(true);

        movementListener = new MovementListener();
        addKeyListener(movementListener);

        try {
            initGame();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        initTimers();
    }

    /**
     * Handles the player's movement based on keyboard input.
     */
    private class MovementListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_W -> movePlayer(-1, 0);
                case KeyEvent.VK_S -> movePlayer(1, 0);
                case KeyEvent.VK_A -> movePlayer(0, -1);
                case KeyEvent.VK_D -> movePlayer(0, 1);
            }
        }
    }

    /**
     * ActionListener implementation to move patrols on each frame tick.
     */
    private class FrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            movePatrols();
        }

    }

    /**
     * Initializes the game elements including the map, patrols, player, and images.
     *
     * @throws IOException If an I/O exception occurs while loading images or initializing the game.
     */
    private void initGame() throws IOException {
        patrols = new ArrayList<>();
        initMap();
        initPatrols();
        initPlayer();
        initImages();
    }

    /**
     * Initializes the game map based on predefined configurations.
     */
    private void initMap() {
        map = YogiGameMap.getRandomMap();
    }

    /**
     * Initializes the patrols on the map based on the predefined map configuration.
     */
    private void initPatrols() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (map[i][j] == 'v' || map[i][j] == 'h') {
                    patrols.add(new Patrol(i, j, map[i][j] == 'v'));
                }
            }
        }
    }

    /**
     * Retrieves the Player object associated with the game panel.
     *
     * @return The Player object representing the game's player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Initializes the player's position and basket count based on the game map.
     */
    private void initPlayer() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'y') {
                    this.player = new Player(i, j);
                    break;
                }
            }
        }

        for (int[] row : map) {
            for (int j = 0; j < map[0].length; j++) {
                if (row[j] == 'b') {
                    this.player.setBasketCount(this.player.getBasketCount() + 1);
                }
            }
        }
    }

    /**
     * Initializes all images required for the game graphics.
     *
     * @throws IOException If an I/O exception occurs while loading the images.
     */
    private void initImages() throws IOException {
        emptyImage = ImageIO.read(new File("assets/empty.png"));
        mountainImage = ImageIO.read(new File("assets/mountain.png"));
        treeImage = ImageIO.read(new File("assets/tree.png"));
        yogiImage = ImageIO.read(new File("assets/yogi.png"));
        patrolImage = ImageIO.read(new File("assets/patrol.png"));
        basketImage = ImageIO.read(new File("assets/basket.png"));
        gateImage = ImageIO.read(new File("assets/gate.png"));
    }

    /**
     * Paints the game components onto the panel.
     *
     * @param g The Graphics object used to paint the components.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int element = 0; // Default to empty
                if (i < map.length && j < map[0].length) {
                    element = map[i][j];
                }

                BufferedImage imageToDraw = switch (element) {
                    case 't' -> treeImage;
                    case 'm' -> mountainImage;
                    case 'y' -> yogiImage;
                    case 'b' -> basketImage;
                    case 'v', 'h' -> patrolImage;
                    case 'g' -> gateImage;
                    default -> emptyImage;
                };

                g.drawImage(imageToDraw, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }

    /**
     * Moves all patrols on the map based on their predefined behavior.
     */
    private void movePatrols() {
        for (Patrol patrol : patrols) {
            patrol.move(map);
        }
        repaint();
    }

    /**
     * Moves the player based on keyboard input and checks for collisions with patrols.
     *
     * @param dx The change in x-coordinate for the player's movement.
     * @param dy The change in y-coordinate for the player's movement.
     */
    private void movePlayer(int dx, int dy) {
        player.move(dx, dy, map, GRID_SIZE);
        repaint(); // Repaint the panel to reflect changes
    }

    /**
     * Checks for collisions between the player and patrols.
     * If a collision occurs, it handles the player's HP reduction and potential game over.
     */
    private void checkCollision() {
        for (Patrol patrol : patrols) {
            if ((Math.abs(player.getX() - patrol.getX()) <= 1) && (Math.abs(player.getY() - patrol.getY()) <= 1)) {
                // Collision detected, reduce player health and reset player position
                player.setHP(player.getHP() - 1);

                if (player.getHP() == 0) {
                    collCheckTimer.stop();
                    frameTimer.stop();
                    yogiGame.getGameTimer().stop();
                    yogiGame.getInfoLabel().setText(String.format("Score: %d    Game Over!    Health: %d",
                            getPlayer().getScore(),
                            getPlayer().getHP()));

                    try {
                        saveScore();
                    } catch (SQLException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    player.resetPosition(map);
                }
                break;
            }
        }
    }

    /**
     * Retrieves the HighScores object associated with this game panel.
     *
     * @return The HighScores object used in this game.
     */
    public HighScores getHighScores() {
        return highScores;
    }

    /**
     * Saves the player's score into the high scores list after the game ends.
     *
     * @throws SQLException If an SQL exception occurs while saving the score.
     */
    private void saveScore() throws SQLException {
        String name = JOptionPane.showInputDialog(yogiGame,
                "Please enter your name to save your score:",
                "Game Over!", JOptionPane.PLAIN_MESSAGE);

        if (name != null && !name.isEmpty()) {
            name = name.strip();
            highScores.putHighScore(name, player.getScore());
        }
        removeKeyListener(movementListener);
    }

    /**
     * Initializes the timers used for game frame updates and collision checks.
     */
    private void initTimers() {
        frameTimer = new Timer(400, new FrameListener());
        frameTimer.start();

        collCheckTimer = new Timer(10, e -> checkCollision());
        collCheckTimer.start();
    }

    /**
     * Stops all timers used in the game panel.
     */
    public void stopTimers() {
        frameTimer.stop();
        collCheckTimer.stop();
        yogiGame.getGameTimer().stop();
    }
}

