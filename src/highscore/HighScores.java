package highscore;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The HighScores class manages high score data in a database.
 * It provides methods to retrieve high scores, insert new scores, and manage the score data.
 */
public class HighScores {

    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection;

    /**
     * Constructs a HighScores object with a specified maximum number of scores to retain.
     *
     * @param maxScores The maximum number of high scores to retain.
     * @throws SQLException If an SQL exception occurs during database connection or statement preparation.
     */
    public HighScores(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        Properties connectionProps = new Properties();
        // Add new user -> MySQL workbench (Menu: Server / Users and priviliges)
        //                             Tab: Administrative roles -> Check "DBA" option
        connectionProps.put("user", "root");
        connectionProps.put("password", "admin");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/highscore";
        connection = DriverManager.getConnection(dbURL, connectionProps);

        String insertQuery = "INSERT INTO HIGHSCORES (TIMESTAMP, NAME, SCORE) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    /**
     * Retrieves the high scores from the database.
     *
     * @return An ArrayList of HighScore objects containing the retrieved high scores.
     * @throws SQLException If an SQL exception occurs during the retrieval of high scores.
     */
    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES";
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new HighScore(name, score));
        }
        sortHighScores(highScores);
        return highScores;
    }

    /**
     * Adds a high score to the database if it qualifies to be among the high scores.
     *
     * @param name  The name of the player.
     * @param score The score achieved by the player.
     * @throws SQLException If an SQL exception occurs during the high score insertion process.
     */
    public void putHighScore(String name, int score) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, score);
        } else {
            int leastScore = highScores.getLast().score();
            if (leastScore < score) {
                deleteScores(leastScore);
                insertScore(name, score);
            }
        }
    }

    /**
     * Sorts the high scores in descending order.
     *
     * @param highScores The ArrayList of HighScore objects to be sorted.
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        highScores.sort((t, t1) -> t1.score() - t.score());
    }

    /**
     * Inserts a new high score into the database.
     *
     * @param name  The name of the player achieving the high score.
     * @param score The score achieved by the player.
     * @throws SQLException If an SQL exception occurs during the insertion of the high score.
     */
    private void insertScore(String name, int score) throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        insertStatement.setTimestamp(1, ts);
        insertStatement.setString(2, name);
        insertStatement.setInt(3, score);
        insertStatement.executeUpdate();
    }

    /**
     * Deletes all the high scores with a specific score value.
     *
     * @param score The score value for which corresponding high scores are to be deleted.
     * @throws SQLException If an SQL exception occurs during the deletion of high scores.
     */
    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
}
