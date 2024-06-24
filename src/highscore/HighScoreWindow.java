package highscore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;


/**
 * The HighScoreWindow class creates a window to display high scores.
 * It utilizes a JTable to present the high scores fetched from the HighScores object.
 */
public class HighScoreWindow {
    /**
     * Constructs a HighScoreWindow object with provided high scores.
     * @param highScores The HighScores object containing the high score data.
     * @throws SQLException If an SQL exception occurs while accessing the high score data.
     */
    public HighScoreWindow(HighScores highScores) throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Score");

        for (HighScore highScore : highScores.getHighScores()) {
            model.addRow(new Object[]{highScore.name(), highScore.score()});
        }

        JTable table = new JTable(model);
        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 220);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}