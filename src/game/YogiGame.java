package game;

import highscore.HighScoreWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * The YogiGame class represents the main frame for the Yogi Game.
 * It manages the game window, timers, menu bar, and game panel.
 */
public class YogiGame extends JFrame {
    private YogiGamePanel panel;
    private Timer gameTimer;
    private final JLabel infoLabel;

    /**
     * Constructs the YogiGame frame with the game components.
     */
    public YogiGame() {
        setTitle("Yogi Game");

        this.panel = new YogiGamePanel(this);

        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBackground(new Color(239, 155, 179));
        infoPanel.setPreferredSize(new Dimension(0, 22));

        infoLabel = new JLabel();
        infoPanel.add(infoLabel);

        long startTime = System.currentTimeMillis();
        initTimer(startTime, infoLabel);
        gameTimer.start();

        infoPanel.add(new JSeparator());

        setJMenuBar(createJMenuBar());

        add(infoPanel, BorderLayout.PAGE_START);
        add(panel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Retrieves the game timer associated with the YogiGame.
     *
     * @return The game timer for the YogiGame.
     */
    public Timer getGameTimer() {
        return gameTimer;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    /**
     * Initializes the game timer to update the game status and display.
     *
     * @param startTime   The starting time for the game.
     * @param infoLabel  The label to display game information.
     */
    private void initTimer(long startTime, JLabel infoLabel) {
        gameTimer = new Timer(10, e -> {
            long elapsedTimeSeconds = (System.currentTimeMillis() - startTime) / 1000;
            String timerDisplay = String.format("Score: %d    Time: %d seconds    Health: %d",
                    panel.getPlayer().getScore(),
                    elapsedTimeSeconds,
                    panel.getPlayer().getHP());
            infoLabel.setText(timerDisplay);

            if (panel.getPlayer().getBasketCount() == 0) {
                startNewGame(true);
            }

        });
    }

    /**
     * Creates the menu bar for the YogiGame.
     *
     * @return The JMenuBar object for the game menu.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar gameMenuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem exitGame = new JMenuItem("Exit");
        exitGame.addActionListener(e -> System.exit(0));

        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(e -> startNewGame(false));

        JMenuItem highScoresMenu = new JMenuItem("High Scores");
        highScoresMenu.addActionListener(e -> {
            try {
                new HighScoreWindow(panel.getHighScores());
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        });

        gameMenu.add(newGame);
        gameMenu.add(highScoresMenu);

        gameMenu.addSeparator();

        gameMenu.add(exitGame);
        gameMenuBar.add(gameMenu);
        return gameMenuBar;
    }

    /**
     * Starts a new game, optionally carrying over the score and health from the previous game.
     *
     * @param carryOver Determines if the score and health are carried over to the new game.
     */
    private void startNewGame(boolean carryOver) {
        this.panel.stopTimers();
        remove(this.panel);

        if (carryOver) {
            Player player = this.panel.getPlayer();
            this.panel = new YogiGamePanel(this);
            add(this.panel);

            this.panel.getPlayer().setScore(player.getScore());
            this.panel.getPlayer().setHP(player.getHP());

            gameTimer.start();
        } else {
            this.panel = new YogiGamePanel(this);
            add(this.panel);

            long startTime = System.currentTimeMillis();
            initTimer(startTime, infoLabel);
            gameTimer.start();
        }
        this.panel.requestFocusInWindow();
    }
}